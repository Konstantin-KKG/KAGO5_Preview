package KAGO_framework.Core.Subsystems.Graphics;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import MyProject.Config;

public class RendererSetup {
    public static RendererBase CreateRenderer() {
        return switch (Config.RENDERER_OPTION) {
            case OPENGL_2D -> createOpenGLRenderer();
            case VULKAN_2D -> createVulkanRenderer();
        };
    }

    private static RendererBase createOpenGLRenderer() {
        Debug.Log("Creating (user configured) OpenGL backend Renderer.", LogType.PROCESS);
        return null;
    }

    private static RendererBase createVulkanRenderer() {
        Debug.Log("Creating (user configured) Vulkan backend Renderer.", LogType.PROCESS);
        return null;
    }
}
