package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import KAGO_framework.Core.Subsystems.Graphics.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Renderer extends RendererBase {
    private long windowHandle;
    private int shaderProgram;

    private int vao;
    private int vbo;

    private float[] vertices = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.0f,  0.5f
    };

    @Override
    public void Construct() {
        windowHandle = Window.GetWindowHandle();

        // Load shader
        shaderProgram = loadShaders();

        // Create a VAO (Vertex Array Object) and bind it
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        // Create a VBO (Vertex Buffer Object) and bind it
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        // Upload the vertex data to the VBO
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Specify the layout of the vertex data
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // Unbind the VBO and VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    public void RenderFrame() {
        glClear(GL_COLOR_BUFFER_BIT);

        // Use the shader program
        glUseProgram(shaderProgram);

        // Bind the VAO and draw the triangle
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glBindVertexArray(0);

        glUseProgram(0);
        GLFW.glfwSwapBuffers(windowHandle);
    }

    @Override
    public void Deconstruct() {
        // Cleanup shaders
        glDeleteProgram(shaderProgram);

        // Cleanup VAO and VBO
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
    }

    private int loadShaders() {
        // Vertex shader source code
        String vertexShaderCode = "#version 330 core\n" +
                "layout(location = 0) in vec3 position;\n" +
                "void main()\n" +
                "{\n" +
                "   gl_Position = vec4(position, 1.0);\n" +
                "}\n";

        // Fragment shader source code
        String fragmentShaderCode = "#version 330 core\n" +
                "out vec4 color;\n" +
                "void main()\n" +
                "{\n" +
                "   color = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                "}\n";

        // Compile and link shaders
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderCode);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderCode);
        glCompileShader(fragmentShader);

        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        // Detach
        glDetachShader(shaderProgram, vertexShader);
        glDetachShader(shaderProgram, fragmentShader);

        // Delete
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        return shaderProgram;
    }
}
