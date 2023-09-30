package KAGO_framework.Core.Subsystems;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Is used to manage all Subsystems
 * @author Kosta, Habib, Maxim and Julius
 * @since 28.09.2023
 */
public class SubsystemComponentDistributor {
    private static final String COMPONENTS_PACKAGE_URL = "KAGO_framework.Core.Components";
    private static final String COMPONENT_HANDLERS_PACKAGE_URL = "KAGO_framework.Core.Subsystems";

    private static HashMap<Type, ComponentHandler> componentHandlerHashMap = new HashMap<>();

    public static void Initialize() {
        Type[] componentTypes = reflectOnComponents();
        Type[] componentHandlers = reflectOnComponentHandlers();
        Debug.Log(String.format("Loaded: %d componentType(s), %d componentHandler(s).", componentTypes.length, componentHandlers.length), LogType.SUCCESS);

        populateComponentHandlerHashMap(componentTypes, componentHandlers);
        Debug.Log(String.format("Cached: %d (componentType, componentHandler) pairs.", componentHandlerHashMap.size()), LogType.SUCCESS);
    }

    /**
     * Forwards the component to its corresponding handler
     * @param component what component should be forwarded
     */
    public static void Distribute(Component component) {
        ComponentHandler handler = componentHandlerHashMap.get(component.getClass());

        if(handler != null)
            handler.ExecLogic(component);
        else
            Debug.Log(String.format("No componentHandler found for component: %s", component.getClass()), LogType.WARNING);
    }

    /**
     * Creates for each ComponentType a corresponding ComponentHandler (if available in the componentHandlerTypes)
     * Adds them as a key, value pair to the componentHandlerHashMap
     * @param componentTypes all componentTypes
     * @param componentHandlerTypes all componentHandlerTypes (corresponding to the componentTypes)
     */
    private static void populateComponentHandlerHashMap(Type[] componentTypes, Type[] componentHandlerTypes) {
        for (Type componentType : componentTypes)
            for (Type componentHandlerType : componentHandlerTypes) {
                // Filter name of types
                String[] componentTypeNameSubstrings = componentType.getTypeName().split("\\.");
                String[] componentHandlerTypeNameSubstrings = componentHandlerType.getTypeName().split("\\.");

                String componentTypeName = componentTypeNameSubstrings[componentTypeNameSubstrings.length - 1];
                String componentHandlerTypeName = componentHandlerTypeNameSubstrings[componentHandlerTypeNameSubstrings.length - 1];

                // If the componentType does not match this componentHandlerType's name, continue to the next componentHandlerType
                if (!componentTypeName.equals(componentHandlerTypeName.replace("Handler", "")))
                    continue;

                ComponentHandler componentHandlerInstance;

                // Create componentHandler instance
                try {
                    Class<?> componentHandlerClass = Class.forName(componentHandlerType.getTypeName());
                    Constructor<?> componentHandlerConstructor = componentHandlerClass.getDeclaredConstructor();

                    Object componentHandlerObject = componentHandlerConstructor.newInstance();
                    componentHandlerInstance = (ComponentHandler) componentHandlerObject;
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    Debug.Log(String.format("Couldn't create an instance of the componentHandlerType: %s", componentHandlerType.getTypeName()), LogType.ERROR);
                    e.printStackTrace();
                    continue;
                }

                // Add pair to componentHandlerHashMap
                componentHandlerHashMap.put(componentType, componentHandlerInstance);
            }
    }

    /**
     * Returns all Component classes from the Components Package
     * @return an array of all component types
     */
    private static Type[] reflectOnComponents() {
        return findClasses(COMPONENTS_PACKAGE_URL, Component.class);
    }

    /**
     * Returns all ComponentHandlers from the Subsystems Package and child Packages
     * @return an array of all ComponentHandlers
     * @see ComponentHandler
     */
    private static Type[] reflectOnComponentHandlers() {
        return findClasses(COMPONENT_HANDLERS_PACKAGE_URL, ComponentHandler.class, "Handler");
    }

    /**
     * Finds all classes in a package (and their child packages) which have a common parent class
     * @param packageName the package to search in
     * @param commonSuperClassType the common parent class type
     * @param classNameSuffix a suffix to the class name
     * @return an array of all found class types
     */
    private static Type[] findClasses(String packageName, Type commonSuperClassType, String... classNameSuffix) {
        // Specify the package name where classes are located
        String packagePath = packageName.replace('.', '/');

        // Get the class loader for the current thread
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Create and check the URL of the package location
        URL packageUrl = classLoader.getResource(packagePath);
        if (packageUrl == null) {
            Debug.Log(String.format("Couldn't find package by URL: %s", packagePath), LogType.FATAL);
            throw new RuntimeException();
        }

        // Get the list of all files in the package directory
        File[] allFiles = getFiles(packageUrl);

        // Create a list for all subclasses
        List<Class<?>> subclasses = new ArrayList<>();

        // Iterate through all packages and all files in those packages
        for (File file : allFiles) {
            String fileName = file.getName();

            // Check if the file is a class
            if (!file.getName().endsWith(".class"))
                continue;

            // Filter for class name suffix
            String rawFileName = fileName.replace(".class", "");
            if (classNameSuffix.length != 0)
                if (!rawFileName.endsWith(classNameSuffix[0]))
                    continue;

            // Exclude "ComponentHandler"
            if(rawFileName.equals("ComponentHandler"))
                continue;

            // Extract filePath (URL format) / className
            String filePath = file.getPath().replace("\\",".").replace(".class","");
            String className = filePath.substring(filePath.indexOf(packageName));

            // Load current file class
            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                Debug.Log(String.format("Couldn't load class by name: %s.", className), LogType.ERROR);
                e.printStackTrace();
                continue;
            }

            // Load the common parent class given by the parameter
            Class<?> commonSuperClass = (Class<?>) commonSuperClassType;

            // Check if the possible child class is a subclass of the common parent class and add it to the list
            if (commonSuperClass.isAssignableFrom(clazz) && !clazz.equals(commonSuperClass))
                subclasses.add(clazz);
        }

        // Convert arraylist to type array
        Type[] types = new Type[subclasses.size()];
        subclasses.toArray(types);
        return types;
    }

    /**
     * Returns all nested Files in a package
     * Acts as an entry point for scanFiles()
     * @param packageUrl The package to search in
     * @return an array of all files in packageUrl
     */
    private static File[] getFiles(URL packageUrl) {
        ArrayList<File> files = new ArrayList<>();

        if (!packageUrl.getProtocol().equals("file")) {
            Debug.Log(String.format("The given URL: (%s) does not contain a file.", packageUrl), LogType.FATAL);
            throw new RuntimeException();
        }

        File file = new File(URLDecoder.decode(packageUrl.getFile(), StandardCharsets.UTF_8));
        scanFiles(file, files);

        return files.toArray(new File[0]);
    }

    /**
     * Recursively scans files and adds them to the provided array list
     * Should Only be Invoked by getFiles()
     * @param directory The directory to scan
     * @param files Where Files of directory should be added to
     */
    private static void scanFiles(File directory, ArrayList<File> files) {
        File[] dirFiles = directory.listFiles();

        // Check if the directory is empty
        if (dirFiles != null)
            for (File file : dirFiles) {
                // If File is a folders scan all files inside them
                if (file.isDirectory())
                    scanFiles(file, files);
                else
                    files.add(file);
            }
    }
}

