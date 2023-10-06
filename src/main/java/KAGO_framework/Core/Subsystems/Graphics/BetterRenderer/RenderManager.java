package KAGO_framework.Core.Subsystems.Graphics.BetterRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class RenderManager {

    private static ArrayList<Integer> vaos = new ArrayList<>();
    private static ArrayList<Integer> vbos = new ArrayList<>();

    static int[] prepare(float[] vertices, int[] indices) {
        return RenderManager.loadToVAO(vertices, indices);
    }
    static int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    static int[] loadToVAO(float[] positions, int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3, positions);
        unbindVAO();
        return new int[]{vaoID, indices.length};
    }

    static void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = GL30.glGenBuffers();
        vbos.add(vboID);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL33.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attributeNumber, coordinateSize, GL33.GL_FLOAT, false, 0, 0);
        GL30.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
    }

    static void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private static void bindIndicesBuffer(int[] indices) {
        int vboID = GL33.glGenBuffers();
        vbos.add(vboID);
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, buffer, GL33.GL_STATIC_DRAW);
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public static void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
    }
}
