package KAGO_framework.Core.Subsystems;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        System.out.println(componentTypes.length);

        for (Type component : componentTypes) {
            System.out.println(component.getTypeName());
        }

        for (Type componentHandler : componentHandlers) {
            System.out.println(componentHandler.getTypeName());
        }
    }

    public static void ResolveComponent(Component component) {
        // TODO: Replace null
        componentHandlerHashMap.get(null).ExecLogic(component);
    }

    private static Type[] reflectOnComponents() {
        String packageName = "KAGO_framework.Core.Components";
        Type[] allComponents = findClasses(packageName, Component.class);
        return allComponents;
    }

    private static Type[] reflectOnComponentHandlers() {
        String packageName = "KAGO_framework.Core.Subsystems";
        Type[] allComponentHandlers  = findClasses(packageName, ComponentHandler.class);
        return allComponentHandlers;
    }

    private static Type[] findClasses(String packageName, Type commonSuperClassType) {
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
            String decodedPackagePath = URLDecoder.decode(packageUrl.getFile(), StandardCharsets.UTF_8);
            File[] allFiles = getFiles(packageUrl); // new File[] { new File(decodedPackagePath) };

            // Create a list for all useful classes
            List<Class<?>> subclasses = new ArrayList<>();

            // Iterate through all packages and all files in those packages
            for (File file : allFiles) {
                String fileName = file.getName();
                //Check if the file is a class
                if (!file.getName().endsWith(".class"))
                    continue;

                // Extract class name from the file name
                String className = packageName + "." + fileName.substring(0, fileName.length() - 6);

                System.out.println(file.getName());

                // Load the class, check if it is a subclass of Component and add it to the list
                Class<?> clazz = Class.forName(className); // TODO: This line does tend to randomly fail, pls help :
                /*
                    So practically .forName() has to have access to everything related to the class in order to load it
                    some of ur classes may or may not have dependencies outside our scope
                */

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

