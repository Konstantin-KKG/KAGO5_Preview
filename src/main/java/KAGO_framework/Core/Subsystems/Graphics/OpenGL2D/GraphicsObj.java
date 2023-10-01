package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

public class GraphicsObj {
    private VertexArrayObject VAO;
    private BufferObject VBO,EBO;
    public int drawMode;
    private int[] indices;
    public GraphicsObj(float[] vertices,int[] indices,int drawMode){
        this.drawMode = drawMode;
        this.indices = indices;
        VBO = new BufferObject(GL_ARRAY_BUFFER,vertices);
        EBO = new BufferObject(GL_ELEMENT_ARRAY_BUFFER,indices);
        VAO = new VertexArrayObject(VBO,EBO);
        VAO.AttributePointer(0,3);
    }
    public void Bind(){
        VAO.Bind();
        VBO.Bind();
        EBO.Bind();
    }
    public int getIndicesLength(){
        return indices.length;
    }
}
