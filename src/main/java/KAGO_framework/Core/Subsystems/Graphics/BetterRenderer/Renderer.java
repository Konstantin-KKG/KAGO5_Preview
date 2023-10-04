package KAGO_framework.Core.Subsystems.Graphics.BetterRenderer;

import KAGO_framework.Core.Subsystems.Graphics.BetterRenderer.Shader.Shader;
import KAGO_framework.Core.Subsystems.Graphics.Renderer.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import MyProject.Config;
import glm_.vec2.Vec2;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Renderer extends RendererBase {
    private long windowHandle;
    private final Shader shader;
    ArrayList<int[]> rectanglesData = new ArrayList<>();
    public Renderer (Shader shader) {
        this.shader = shader;
    }

    @Override
    public void Construct() {
        windowHandle = Window.GetWindowHandle();

        this.shader.start();
        DrawRectangle(new Vec2(0, 0), new Vec2(100, 100), new Color(187, 10, 127), true);
    }

    @Override
    public void RenderFrame() {
        GLFW.glfwSwapBuffers(windowHandle);
        glClear(GL33.GL_COLOR_BUFFER_BIT);
        glClearColor(0.2f, 0, 0.2f, 1);

        for (int[] rectangle : rectanglesData) {
            render (rectangle[0], rectangle[1]);
        }
    }

    @Override
    public void Deconstruct() {
        shader.stop();
        RenderManager.cleanUp();
    }

    @Override
    public void DrawRectangle(Vec2 pos, Vec2 size, Color color, boolean fill) {
        float[] vertices = {
                pos.getX() / Config.WINDOW_WIDTH - 1f, -(pos.getY() + size.getY()) / Config.WINDOW_HEIGHT + 1f, 0,
                pos.getX() / Config.WINDOW_WIDTH - 1f, pos.getY() / Config.WINDOW_HEIGHT + 1f, 0,
                (pos.getX() + size.getX()) / Config.WINDOW_WIDTH - 1f, pos.getY() / Config.WINDOW_HEIGHT + 1f, 0,
                (pos.getX() + size.getX()) / Config.WINDOW_WIDTH - 1f, -(pos.getY() + size.getY()) / Config.WINDOW_HEIGHT + 1f, 0
        };
        int[] indices = {
                0,1,2,
                0,2,3
        };
        rectanglesData.add(RenderManager.loadToVAO(vertices, indices));
    }

    private void render(int vaoID, int vertexCount) {
        GL30.glBindVertexArray(vaoID);
        GL30.glEnableVertexAttribArray(0);
        GL30.glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void DrawLine(Vec2 start, Vec2 end, Color color, int thickness) {
    }

    @Override
    public void DrawCircle(Vec2 pos, float radius, Color color, boolean fill) {

    }

    @Override
    public void DrawSprite() {

    }

    @Override
    public void DrawMesh() {

    }

}
