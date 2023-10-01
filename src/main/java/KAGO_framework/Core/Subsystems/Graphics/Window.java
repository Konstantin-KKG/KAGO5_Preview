package KAGO_framework.Core.Subsystems.Graphics;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import MyProject.Config;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;
import java.nio.IntBuffer;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private static long windowHandle;

    public static void Construct() {
        if (windowHandle != 0) {
            Debug.Log("You cannot create multiple windows.", LogType.WARNING);
            return;
        }

        // TODO: Replace with our own debugger
        // Print glfw errors
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            Debug.Log("Couldn't initialize GLFW.", LogType.FATAL);
            throw new RuntimeException();
        }

        // Window hints
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);

        // Create window
        CharSequence windowTitleSeq = new StringBuilder(Config.WINDOW_TITLE);
        windowHandle = GLFW.glfwCreateWindow(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, windowTitleSeq, NULL, NULL);

        if (windowHandle == NULL) {
            Debug.Log("Couldn't create GLFW window.", LogType.FATAL);
            throw new RuntimeException();
        }

        // Setup key callback // TODO: Change Key Callback
        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        // Center window
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Alloc ints
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get: window size, resolution of primary monitor
            GLFW.glfwGetWindowSize(windowHandle, pWidth, pHeight);
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center window
            GLFW.glfwSetWindowPos(
                    windowHandle,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        // Final settings, set visible
        GLFW.glfwShowWindow(windowHandle);
    }

    public static void Deconstruct() {
        // Free the window callback & destroy the window
        Callbacks.glfwFreeCallbacks(windowHandle);
        GLFW.glfwDestroyWindow(windowHandle);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public static long GetWindowHandle() {
        return windowHandle;
    }
}
