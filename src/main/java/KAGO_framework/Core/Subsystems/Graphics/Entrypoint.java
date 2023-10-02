package KAGO_framework.Core.Subsystems.Graphics;

import KAGO_framework.Core.Subsystems.SubsystemEntrypoint;
import org.lwjgl.glfw.GLFW;

public class Entrypoint extends SubsystemEntrypoint {
    public static RendererBase rendererInUse;

    @Override
    public void Start() {
        Window.Construct();
        rendererInUse = RendererSetup.CreateRenderer();

        GLFW.glfwShowWindow(Window.GetWindowHandle());
    }

    @Override
    public void Update() {

    }
}
