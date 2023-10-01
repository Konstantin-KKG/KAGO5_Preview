package KAGO_framework.Core.Subsystems.Graphics;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import MyProject.Config;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;
import java.nio.IntBuffer;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public static Window currentWindow;
    private long window;

    public static void CreateWindow() {
        if (currentWindow != null) {
            Debug.Log("You cannot create multiple windows.", LogType.WARNING);
            return;
        }

        currentWindow = new Window();
    }

    private Window() {
        init();
        loop();

        // Free the window callback & destroy the window
        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);


        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    private void init() {
        // TODO: Replace with our own debugger
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
        window = GLFW.glfwCreateWindow(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT, windowTitleSeq, NULL, NULL);

        if (window == NULL) {
            Debug.Log("Couldn't create GLFW window.", LogType.FATAL);
            throw new RuntimeException();
        }

        // Setup key callback
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        // Center window
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Alloc ints
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            // Get: window size, resolution of primary monitor
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center window
            GLFW.glfwSetWindowPos(
                    window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2
            );
        }

        // Final settings, set visible
        GLFW.glfwShowWindow(window);
    }

    private void loop() {
        while (!GLFW.glfwWindowShouldClose(window)) {

            GLFW.glfwPollEvents();
        }
    }
}
