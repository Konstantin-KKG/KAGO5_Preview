package KAGO_framework.Core.Subsystems;

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
public class SubsystemResolver {
    private static final String COMPONENTS_PACKAGE_URL = "KAGO_framework.Core.Components";
    private static final String COMPONENT_HANDLERS_PACKAGE_URL = "KAGO_framework.Core.Subsystems";

    private static HashMap<Type, ComponentHandler> componentHandlerHashMap = new HashMap<>();

    public static void Initialize() {
        Type[] componentTypes = reflectOnComponents();
        Type[] componentHandlers = reflectOnComponentHandlers();

        System.out.println("Loaded " + (componentTypes.length + componentHandlers.length) + " Type(s)");

        populateComponentHandlerHashMap(componentTypes, componentHandlers);
    }

    /**
     * Forwards the component to its corresponding handler
     * @param component what component should be forwarded
     */
    public static void ResolveComponent(Component component) {
        // TODO: Error Handling
        ComponentHandler handler =  componentHandlerHashMap.get(component.getClass());
        if(handler != null)
            handler.ExecLogic(component);
        else
            System.err.println("There is no ComponentHandler for: " + component.getClass());
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

                // Finds a component, componentHandler pair
                // Example: Test = TestHandler (true)
                if (componentTypeName.equals(componentHandlerTypeName.replace("Handler", ""))) {
                    try {
                        // Create componentHandler instance
                        Class<?> componentHandlerClass = Class.forName(componentHandlerType.getTypeName());
                        Constructor<?> componentHandlerConstructor = componentHandlerClass.getDeclaredConstructor();

                        Object componentHandlerObject = componentHandlerConstructor.newInstance();
                        ComponentHandler componentHandler = (ComponentHandler) componentHandlerObject;

                        // Add pair to componentHandlerHashMap
                        componentHandlerHashMap.put(componentType, componentHandler);
                    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                        System.err.println("Couldn't create instance of a componentHandlerType: \n" + componentHandlerType.getTypeName());
                        throw new RuntimeException(e);
                    }
                }
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
        // Create a temporary ArrayList to fill
        ArrayList<Type> typeArrayList = new ArrayList<>();

        // Specify the package name where classes are located
        String packagePath = packageName.replace('.', '/');

        // Get the class loader for the current thread
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            // Create and check the URL of the package location
            URL packageUrl = classLoader.getResource(packagePath);
            assert packageUrl != null : "FUCK! Package URL not found.";

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
                Class<?> clazz = Class.forName(className);

                // Load the common parent class given by the parameter
                Class<?> commonSuperClass = (Class<?>) commonSuperClassType;

                // Check if the possible child class is a subclass of the common parent class and add it to the list
                if (commonSuperClass.isAssignableFrom(clazz) && !clazz.equals(commonSuperClass))
                    subclasses.add(clazz);
            }

            typeArrayList.addAll(subclasses);

        } catch (ClassNotFoundException | NullPointerException e) {
            System.err.println("SubsystemResolver could not load a class by name (String).");
            e.printStackTrace();
        }

        // Convert arraylist to type array
        Type[] types = new Type[typeArrayList.size()];
        typeArrayList.toArray(types);
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

        assert packageUrl.getProtocol().equals("file") : "The given URL does not contain a file";

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

        //Check if the directory is empty
        if (dirFiles != null) {
            for (File file : dirFiles) {
                //If File is a folders scan all files inside them
                if (file.isDirectory()) {
                    scanFiles(file, files);
                } else {
                    files.add(file);
                }
            }
        }
    }
}

