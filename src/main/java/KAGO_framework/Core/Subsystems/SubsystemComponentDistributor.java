package KAGO_framework.Core.Subsystems;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;

import static KAGO_framework.Utilities.ClassFinder.FindClasses;

/**
 * Is used to manage all Subsystems
 * @author Kosta, Habib, Maxim and Julius
 * @since 28.09.2023
 */
public class SubsystemComponentDistributor {
    private static final String COMPONENTS_PACKAGE_URL = "KAGO_framework.Core.Components";
    private static final String COMPONENT_HANDLERS_PACKAGE_URL = "KAGO_framework.Core.Subsystems";

    private static HashMap<Type, ComponentHandler> COMPONENT_HANDLER_HASH_MAP = new HashMap<>();

    public static void Initialize() {
        Type[] componentTypes = reflectOnComponents();
        Type[] componentHandlers = reflectOnComponentHandlers();
        Debug.Log(String.format("Loaded: %d componentType(s) and %d componentHandler(s).", componentTypes.length, componentHandlers.length), LogType.SUCCESS);

        populateComponentHandlerHashMap(componentTypes, componentHandlers);
        Debug.Log(String.format("Cached: %d (componentType, componentHandler) pairs.", COMPONENT_HANDLER_HASH_MAP.size()), LogType.SUCCESS);
    }

    /**
     * Forwards the component to its corresponding handler
     * @param component what component should be forwarded
     */
    public static void Distribute(Component component) {
        ComponentHandler handler = COMPONENT_HANDLER_HASH_MAP.get(component.getClass());

        if(handler != null)
            handler.ExecLogic(component);
        else
            Debug.Log(String.format("No componentHandler found for the component: %s", component.getClass()), LogType.WARNING);
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
                COMPONENT_HANDLER_HASH_MAP.put(componentType, componentHandlerInstance);
            }
    }

    private static Type[] reflectOnComponents() {
        return FindClasses(COMPONENTS_PACKAGE_URL, Component.class);
    }

    private static Type[] reflectOnComponentHandlers() {
        return FindClasses(COMPONENT_HANDLERS_PACKAGE_URL, ComponentHandler.class, "Handler");
    }
}

