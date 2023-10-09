package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import KAGO_framework.Core.Subsystems.Graphics.RendererBase;
import MyProject.Config;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer extends RendererBase {

    private int timer;
    private final long window;
    private int shaderProgram;
    private ArrayList<GraphicsObj> objects = new ArrayList<>();
    private ArrayList<Float> triangleVertices = new ArrayList<>();
    private ArrayList<Integer> triangleIndices = new ArrayList<>();
    private ArrayList<Float> lineVertices = new ArrayList<>();
    private ArrayList<Integer> lineIndices = new ArrayList<>();

    public Renderer(long windowHandle) {
        this.window = windowHandle;
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        glViewport(0, 0, Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        boolean success = createShaders();
        if (!success)
            System.err.println("Failed to Create/Compile Shaders");

        glUseProgram(shaderProgram);

        /*objects.add(new GraphicsObj(vertices,indices));
        objects.add(new GraphicsObj(vertices2,indices));*/
        // Set the clear color
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    }

    public void Render() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        //while ( !glfwWindowShouldClose(window)) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        glUseProgram(shaderProgram);
        /*for (GraphicsObj object : objects) {
            object.Bind();
            glDrawElements(object.drawMode, object.getIndicesLength(), GL_UNSIGNED_INT,0L);
        }*/
        //Draw Lines
        BufferObject vbo = new BufferObject(GL_ARRAY_BUFFER,ListToFloatArray(lineVertices));
        BufferObject ebo = new BufferObject(GL_ELEMENT_ARRAY_BUFFER,ListToIntArray(lineIndices));

        VertexArrayObject vao = new VertexArrayObject(vbo,ebo);
        vao.AttributePointer(0,3);
        vao.Bind();
        vbo.Bind();
        ebo.Bind();
        glDrawElements(GL_LINES,lineIndices.size(),GL_UNSIGNED_INT,NULL);


        //Draw Triangles
        vbo = new BufferObject(GL_ARRAY_BUFFER,ListToFloatArray(triangleVertices));
        ebo = new BufferObject(GL_ELEMENT_ARRAY_BUFFER,ListToIntArray(triangleIndices));

        vao = new VertexArrayObject(vbo,ebo);
        vao.AttributePointer(0,3);
        vao.Bind();
        vbo.Bind();
        ebo.Bind();
        glDrawElements(GL_TRIANGLES,triangleIndices.size(),GL_UNSIGNED_INT,NULL);




        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
        glfwSwapBuffers(window); // swap the color buffers

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
        //}
    }
    @Override
    public void DrawLine(float x1, float y1, float x2, float y2) {
        lineIndices.addAll(List.of(0 + lineVertices.size(),1 + lineVertices.size()));
        lineVertices.addAll(List.of(
                x1, y1, 0.0f,
                x2, y2, 0.0f));
    }

    @Override
    public void DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        triangleIndices.addAll(List.of(0 + triangleVertices.size()/3, 1 + triangleVertices.size()/3, 2 + triangleVertices.size()/3));
        triangleVertices.addAll(List.of(
                x1, y1, 0.0f,
                x2, y2, 0.0f,
                x3, y3, 0.0f
        ));
    }

    @Override
    public void DrawRectangle(float x, float y, float width, float height) {
        float[] verts = new float[]{
                x, y, 0f,
                x, y - height, 0f,
                x + width, y, 0f,
                x + width, y - height, 0f
        };
        int[] inds = new int[]{0, 1, 3, 3, 2, 0};
        objects.add(new GraphicsObj(verts, inds, GL_TRIANGLES));
    }
    /*@Override
    public void DrawLine(float x1, float y1, float x2, float y2) {
        float[] verts = new float[]{
                x1, y1, 0.0f,
                x2, y2, 0.0f};
        int[] inds = new int[]{0, 1};
        objects.add(new GraphicsObj(verts, inds, GL_LINES));
    }

    @Override
    public void DrawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        float[] verts = new float[]{
                x1, y1, 0.0f,
                x2, y2, 0.0f,
                x3, y3, 0.0f
        };
        int[] inds = new int[]{0, 1, 2};
        objects.add(new GraphicsObj(verts, inds, GL_TRIANGLES));
    }

    @Override
    public void DrawRectangle(float x, float y, float width, float height) {
        float[] verts = new float[]{
                x, y, 0f,
                x, y - height, 0f,
                x + width, y, 0f,
                x + width, y - height, 0f
        };
        int[] inds = new int[]{0, 1, 3, 3, 2, 0};
        objects.add(new GraphicsObj(verts, inds, GL_TRIANGLES));
    }
*/
    //TODO Integrate Logging
    private boolean createShaders() {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        boolean failed = false;
        //Vertex Shader Source Code
        String vertexShaderSource = """
                #version 330 core
                layout (location = 0) in vec3 aPos;
                void main()
                {
                   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
                }\0""";
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        int success = glGetShaderi(vertexShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            System.out.println("Compilation Fail(Vertex)");
            System.out.println(glGetShaderInfoLog(vertexShader));
            failed = true;
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        //Fragment Shader source code
        String fragmentShaderSource = """
                #version 330 core
                out vec4 FragColor;
                                
                void main()
                {
                    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
                }
                \0""";
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        success = glGetShaderi(fragmentShader, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            System.out.println("Compilation Fail(FragmentShader)");
            System.out.println(glGetShaderInfoLog(fragmentShader));
            failed = true;
        }

        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);

        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            System.out.println("Link Fail(ShaderProgram)");
            System.out.println(glGetProgramInfoLog(shaderProgram));
            failed = true;
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        return !failed;
    }


    private <E extends Number> float[] ListToFloatArray(List<E> list){
        float[] array = new float[list.size()];
        int i = 0;

        for (E number : list) {
            array[i++] = number != null ? number.floatValue() : 0f;
        }
        return array;
    }
    private <E extends Number> int[] ListToIntArray(List<E> list){
        int[] array = new int[list.size()];
        int i = 0;

        for (E number : list) {
            array[i++] = number != null ? number.intValue() : 0;
        }
        return array;
    }
}

