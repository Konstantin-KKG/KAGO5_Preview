package KAGO_framework.Core.Subsystems.Graphics.BetterRenderer;

import KAGO_framework.Core.Subsystems.Graphics.BetterRenderer.Shader.Shader;
import KAGO_framework.Core.Subsystems.Graphics.Renderer.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import MyProject.Config;
import glm_.vec2.Vec2;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Renderer extends RendererBase {
    private long windowHandle;
    private final Shader shader;
    float[] rectangleVertices = new float[0];
    int rectanglesVAO;
    public Renderer (Shader shader) {
        this.shader = shader;
    }

    @Override
    public void Construct() {
        windowHandle = Window.GetWindowHandle();

        this.shader.start();
        for (int i = 0; i < 10; i++) {
            DrawRectangle(new Vec2(i*100, i*100), new Vec2(100, 100), new Color(0, 0, 0), true);
        }
        System.out.println(Arrays.toString(rectangleVertices));
        System.out.println("rectangleVertices length: " + rectangleVertices.length);
        System.out.println(Arrays.toString(generateIndices(rectangleVertices.length/2)));
        System.out.println("Indices length: " + generateIndices(rectangleVertices.length/2).length);
        rectanglesVAO = RenderManager.prepare(rectangleVertices, generateIndices(rectangleVertices.length/2))[0];
    }

    @Override
    public void RenderFrame() {
        GLFW.glfwSwapBuffers(windowHandle);
        glClear(GL33.GL_COLOR_BUFFER_BIT);
        glClearColor(0.2f, 0, 0.2f, 1);

        render(rectanglesVAO, rectangleVertices.length);
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
                pos.getX() / Config.WINDOW_WIDTH - 1f, -pos.getY() / Config.WINDOW_HEIGHT + 1f, 0,
                (pos.getX() + size.getX()) / Config.WINDOW_WIDTH - 1f, -pos.getY() / Config.WINDOW_HEIGHT + 1f, 0,
                (pos.getX() + size.getX()) / Config.WINDOW_WIDTH - 1f, -(pos.getY() + size.getY()) / Config.WINDOW_HEIGHT + 1f, 0
        };
        rectangleVertices = mergeFloatArr(rectangleVertices, vertices);
    }
    private float[] mergeFloatArr (float[] firstArr, float[] secondArr) {
        float[] result = new float[firstArr.length + secondArr.length];
        System.arraycopy(firstArr, 0, result, 0, firstArr.length);
        System.arraycopy(secondArr, 0, result, firstArr.length, secondArr.length);
        return result;
    }

    private int[] generateIndices(int quadCount) {
        int[] indices = new int[quadCount];
        for (int i = 0; i < quadCount/6; i++) {
            int offsetArrayIndex = 6 * i;
            int offset = 4 * i;

            indices[offsetArrayIndex] = offset;
            indices[offsetArrayIndex + 1] = offset + 1;
            indices[offsetArrayIndex + 2] = offset + 2;

            indices[offsetArrayIndex + 3] = offset;
            indices[offsetArrayIndex + 4] = offset + 2;
            indices[offsetArrayIndex + 5] = offset + 3;
        }

        return indices;
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
