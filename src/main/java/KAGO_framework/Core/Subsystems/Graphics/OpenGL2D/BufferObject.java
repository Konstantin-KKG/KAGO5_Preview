package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class BufferObject {
    private final int handle;
    private final int target;
    public BufferObject(int target,float[] data){
        handle = glGenBuffers();
        this.target = target;
        Bind();
        glBufferData(target, data ,GL_DYNAMIC_DRAW);
    }
    public BufferObject(int target,int[] data){
        handle = glGenBuffers();
        this.target = target;
        Bind();
        glBufferData(target, data ,GL_DYNAMIC_DRAW);
    }
    public BufferObject(int target,double[] data){
        handle = glGenBuffers();
        this.target = target;
        Bind();
        glBufferData(target, data ,GL_DYNAMIC_DRAW);
    }
    public void Bind(){
        glBindBuffer(target, handle);
    }
}
