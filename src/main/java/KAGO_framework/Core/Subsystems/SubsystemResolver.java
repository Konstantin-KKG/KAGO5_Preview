package KAGO_framework.Core.Subsystems;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Used to manage all Subsystems
 * @author Kosta, Habib, Maxim (and Julius(Java-Doc-Help))
 * @since 28.09.2023
 */
public class SubsystemResolver {
    private static final String COMPONENTS_PACKAGE_URL = "KAGO_framework.Core.Components";
    private static final String COMPONENT_HANDLERS_PACKAGE_URL = "KAGO_framework.Core.Subsystems";

    private static HashMap<Type, ComponentHandler> componentHandlerHashMap = new HashMap<>();

    public static void Initialize() {
        Type[] componentTypes = reflectOnComponents();
        Type[] componentHandlers = reflectOnComponentHandlers();

        // Currently debug code
        System.out.println("Loaded " + (componentTypes.length + componentHandlers.length) + " Type(s)");

        for (Type component : componentTypes)
            System.out.println(component.getTypeName());

        for (Type componentHandler : componentHandlers)
            System.out.println(componentHandler.getTypeName());
    }

    /**
     * Forwards the component to its corresponding handler
     * @param component what component should be forwarded
     */
    public static void ResolveComponent(Component component) {
        // TODO: Replace null
        componentHandlerHashMap.get(null).ExecLogic(component);
    }

    /**
     * Returns all Component classes from the Components Package
     * @return an array of all component types
     */
    private static Type[] reflectOnComponents() {
        Type[] allComponents = findClasses(COMPONENTS_PACKAGE_URL, Component.class);
        return allComponents;
    }

    /**
     * Returns all ComponentHandlers from the Subsystems Package and child Packages
     * @return an array of all ComponentHandlers
     * @see ComponentHandler
     */
    private static Type[] reflectOnComponentHandlers() {
        Type[] allComponentHandlers = findClasses(COMPONENT_HANDLERS_PACKAGE_URL, ComponentHandler.class, "Handler");
        return allComponentHandlers;
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

                // Extract class name from the file name
                //Get File Path and turn it into class format
                String filePath = file.getAbsolutePath().replace("\\",".").replace(".class","");
                //Remove everything before packageName before filePath
                String className = filePath.substring(filePath.indexOf(packageName));
                System.out.println(className);

                // Load the possible child class
                /*
                    So basically .forName() has to have access to the class' parent class .class file
                    in order to load its src code.
                    Some of our classes may or may not have parent classes outside our ./out scope
                    TODO: same problem but different :c
                */
                Class<?> clazz = Class.forName(className);

                // Load the common parent class given by the parameter
                Class<?> commonSuperClass = (Class<?>) commonSuperClassType;
                // Check if the possible child class is a subclass of the common parent class and add it to the list
                if (commonSuperClass.isAssignableFrom(clazz) && !clazz.equals(commonSuperClass))
                    subclasses.add(clazz);
            }

            // Convert from classes to types
            for (Class<?> subclass : subclasses) {
                Type classType = subclass;
                typeArrayList.add(classType);
            }
        } catch (ClassNotFoundException | NullPointerException e) {
            System.out.println("SubsystemResolver could not load a class by name (String).");
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
        String protocol = packageUrl.getProtocol();

        if (protocol.equals("file")) {
            File file = new File(URLDecoder.decode(packageUrl.getFile(), StandardCharsets.UTF_8));
            scanFiles(file, files);
        }

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
            //Go Through all Files in directory
            for (File file : dirFiles) {
                //If File has sub-folders scan all of them
                if (file.isDirectory()) {
                    scanFiles(file, files);
                } else {
                    //Add Files to given Arraylist if it's not a folder
                    files.add(file);
                }
            }
        }
    }
}

