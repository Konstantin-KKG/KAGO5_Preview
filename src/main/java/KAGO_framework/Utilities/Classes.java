package KAGO_framework.Utilities;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Is used to find specific classes in the project, based on different criteria
 * @author Maxim, Kosta, Habib
 * @since 30.09.2023
 */
public class Classes {

    /**
     * Finds all classes in a package (and their child packages) which share a common parent class using reflection
     * @param packageName the package to search in
     * @param commonSuperClassType the common parent class type
     * @param classNameSuffix a suffix to the class name
     * @return an array of all found class types
     */
    public static Type[] FindClasses(String packageName, Type commonSuperClassType, String... classNameSuffix) {
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

    public static String GetPackageNameOfClass(Class<?> clazz) {
        Package pkg = clazz.getPackage();

        if (pkg == null) {
            Debug.Log("Couldn't find package of class", LogType.WARNING);
            return "[MISSING_PACKAGE_NAME]";
        }

        return pkg.getName();
    }
}
