package KAGO_framework.Core.Subsystems.Graphics.Renderer.OpenGL2D;

import KAGO_framework.Core.Subsystems.Graphics.Renderer.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL33.*;

public class Renderer extends RendererBase {
    private long windowHandle;

    // Render Logic
    @Override
    public void Construct() {
        windowHandle = Window.GetWindowHandle();
    }

    @Override
    public void RenderFrame() {
        glClear(GL_COLOR_BUFFER_BIT);



        glUseProgram(0);

        // Swap buffers
        GLFW.glfwSwapBuffers(windowHandle);
    }

    @Override
    public void Deconstruct() {

    }
}
