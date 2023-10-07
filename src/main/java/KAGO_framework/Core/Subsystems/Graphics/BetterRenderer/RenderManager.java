package KAGO_framework.Core.Subsystems.Graphics.BetterRenderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;

public class RenderManager {

    private static ArrayList<Integer> vaos = new ArrayList<>();
    static ArrayList<Integer> vbos = new ArrayList<>();

    static int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    static int loadToVAO(){
        int vaoID = createVAO();
        bindIndicesBuffer(1024);
        storeDataInAttributeList(0,3, 0, 1024);
        storeDataInAttributeList(1,4, 3*Float.BYTES, 1024);
        unbindVAO();
        return vaoID;
    }

    static void storeDataInAttributeList(int attributeNumber, int coordinateSize, int pointer, int memoryAlloc) {
        int vboID = GL30.glGenBuffers();
        vbos.add(vboID);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboID);

        FloatBuffer vertices = MemoryUtil.memAllocFloat(memoryAlloc);
        long size = (long) vertices.capacity() * Float.BYTES;
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, size, GL30.GL_DYNAMIC_DRAW);
        GL30.glVertexAttribPointer(attributeNumber, coordinateSize, GL33.GL_FLOAT, false, 7*Float.BYTES, pointer);
        GL30.glBindBuffer(GL33.GL_ARRAY_BUFFER, 0);
    }

    static void populateDynamicFloatBuffer(int vboID, float[] data) {
        GL30.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboID);
        GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, data);
    }
    static void populateDynamicIntBuffer(int vboID, int[] data) {
        GL30.glBindBuffer(GL33.GL_ARRAY_BUFFER, vboID);
        GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, data);
    }

    static void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private static void bindIndicesBuffer(int memoryAlloc) {
        int vboID = GL33.glGenBuffers();
        vbos.add(vboID);
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer buffer = MemoryUtil.memAllocInt(memoryAlloc);
        long size = (long) buffer.capacity() * Integer.BYTES;
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, size, GL33.GL_DYNAMIC_DRAW);
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
