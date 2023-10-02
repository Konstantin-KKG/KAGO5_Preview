package KAGO_framework.Core.Subsystems.Graphics;

import KAGO_framework.Core.Subsystems.Graphics.Renderer.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Renderer.RendererSetup;
import KAGO_framework.Core.Subsystems.SubsystemEntrypoint;
import org.lwjgl.glfw.GLFW;

public class Entrypoint extends SubsystemEntrypoint {
    private static long windowHandle;
    public static RendererBase rendererInUse;

    @Override
    public void Start() {
        // Create window
        Window.Construct();
        windowHandle = Window.GetWindowHandle();

        // Create renderer
        rendererInUse = RendererSetup.CreateBackendSpecificRenderer(windowHandle);
        rendererInUse.Construct();

        // Show window
        GLFW.glfwShowWindow(windowHandle);
    }

    @Override
    public void Update() {
        rendererInUse.RenderFrame();
    }

    @Override
    public void Stop() {
        rendererInUse.Deconstruct();
        Window.Deconstruct();
    }
}
