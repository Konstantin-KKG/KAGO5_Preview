package KAGO_framework.Core.Subsystems;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Utilities.Classes;

/**
 * Is used to initialize all SubsystemEntrypoints
 * @author Kosta, Habib
 * @since 29.09.2023
 */
public class SubsystemManager {
    private static final String ENTRYPOINTS_PACKAGE_URL = "KAGO_framework.Core.Subsystems";

    private static SubsystemEntrypoint[] subsystemEntrypoints;

    public static void Initialize() {
        Type[] subsystemEntrypointClasses = reflectOnEntrypoints();
        subsystemEntrypoints = createEntrypoints(subsystemEntrypointClasses);
        startSubsystems(subsystemEntrypoints);

        Debug.Log("Finished initializing all Subsystems.", LogType.SUCCESS);
    }

    public static void UpdateSubsystems() {
        for (SubsystemEntrypoint subsystemEntrypoint : subsystemEntrypoints)
            subsystemEntrypoint.Update();
    }

    public static void StopSubsystems() {
        for (SubsystemEntrypoint subsystemEntrypoint : subsystemEntrypoints)
            subsystemEntrypoint.Stop();
    }

    private static Type[] reflectOnEntrypoints() {
        return Classes.FindClasses(ENTRYPOINTS_PACKAGE_URL, SubsystemEntrypoint.class);
    }

    private static SubsystemEntrypoint[] createEntrypoints(Type[] entrypointTypes) {
        ArrayList<SubsystemEntrypoint> entrypointsArrayList = new ArrayList<>();

        // Create entrypoint instances
        for (Type entrypoint: entrypointTypes) {
            SubsystemEntrypoint entrypointInstance;

            try {
                Class<?> entrypointClass = Class.forName(entrypoint.getTypeName());
                Constructor<?> entrypointConstructor = entrypointClass.getDeclaredConstructor();

                Object entrypointObject = entrypointConstructor.newInstance();
                entrypointInstance = (SubsystemEntrypoint) entrypointObject;
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                Debug.Log(String.format("Couldn't create an instance of the SubsystemEntrypoint: %s", entrypoint.getTypeName()), LogType.ERROR);
                e.printStackTrace();
                continue;
            }

            entrypointsArrayList.add(entrypointInstance);
        }

        // Convert ArrayList to Array
        SubsystemEntrypoint[] entrypoints = new SubsystemEntrypoint[entrypointsArrayList.size()];
        entrypointsArrayList.toArray(entrypoints);
        return entrypoints;
    }

    private static void startSubsystems(SubsystemEntrypoint[] subsystemEntrypoints){
        for (SubsystemEntrypoint entrypoint: subsystemEntrypoints) {
            entrypoint.Start();

            // Log the started subsystem
            String packageName = Classes.GetPackageNameOfClass(entrypoint.getClass())
                    .replace(ENTRYPOINTS_PACKAGE_URL, "")
                    .substring(1);
            String debugMessage = String.format("Initialized (%s) Subsystem.", packageName);
            Debug.Log(debugMessage, LogType.SUCCESS);
        }
    }
}
