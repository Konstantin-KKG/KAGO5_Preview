package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArrayObject {
    private int handle;
    public VertexArrayObject(BufferObject vbo,BufferObject ebo) {
        handle = glGenVertexArrays();
        Bind();
        vbo.Bind();
        ebo.Bind();

    }
    public void AttributePointer(int index, int size){
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(index,size, GL_FLOAT,false,0,0L);
    }
    public void Bind(){
        glBindVertexArray(handle);
    }
}
