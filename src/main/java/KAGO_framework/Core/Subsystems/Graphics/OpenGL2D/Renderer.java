package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import KAGO_framework.Core.Subsystems.Graphics.RendererBase;
import MyProject.Config;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Renderer extends RendererBase {


    private int timer;
    private long window;
    private int shaderProgram;
    private ArrayList<GraphicsObj> objects = new ArrayList<>();
    private final float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };
    private final float[] vertices2 = {
            0.5f, 0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };
    private final int[] indices = {0, 1, 2};

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
        for (GraphicsObj object : objects) {
            object.Bind();
            glDrawElements(object.drawMode, object.getIndicesLength(), GL_UNSIGNED_INT,0L);
        }


        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
        glfwSwapBuffers(window); // swap the color buffers

        //}
    }

    @Override
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

}

