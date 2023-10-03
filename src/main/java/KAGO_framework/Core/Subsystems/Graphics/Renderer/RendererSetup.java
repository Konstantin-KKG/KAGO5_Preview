package KAGO_framework.Core.Subsystems.Graphics.Renderer;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import MyProject.Config;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33.*;

public class RendererSetup {
    public static RendererBase CreateBackendSpecificRenderer(long windowHandle) {
        return switch (Config.RENDERER_OPTION) {
            case OPENGL_2D -> createOpenGLRenderer(windowHandle);
            case VULKAN_2D -> createVulkanRenderer(windowHandle);
        };
    }

    private static RendererBase createOpenGLRenderer(long windowHandle) {
        Debug.Log("Creating (user configured) OpenGL backend Renderer.", LogType.PROCESS);

        // (GLFW) Setup OpenGL context & V-Sync setting
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(0);

        // (OpenGL) Set context & Viewport
        GL.createCapabilities();
        glViewport(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

        return new KAGO_framework.Core.Subsystems.Graphics.Renderer.OpenGL2D.Renderer();
    }

    private static RendererBase createVulkanRenderer(long windowHandle) {
        Debug.Log("Vulkan backend is not yet implemented.", LogType.WARNING);

        return new KAGO_framework.Core.Subsystems.Graphics.Renderer.Vulkan2D.Renderer();
    }
}
