package KAGO_framework.Core.Subsystems.Graphics.Renderer.OpenGL2D;

import KAGO_framework.Core.Subsystems.Graphics.Datatypes.Circle;
import KAGO_framework.Core.Subsystems.Graphics.Datatypes.Line;
import KAGO_framework.Core.Subsystems.Graphics.Datatypes.Rectangle;
import KAGO_framework.Core.Subsystems.Graphics.Renderer.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import glm_.vec2.Vec2;
import org.lwjgl.glfw.GLFW;
import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public class Renderer extends RendererBase {
    private long windowHandle;
    private int shaderProgram;

    ArrayList<Line> linesData = new ArrayList<>();
    ArrayList<Rectangle> rectanglesData = new ArrayList<>();
    ArrayList<Circle> circlesData = new ArrayList<>();

    /*
        Render Logic:
     */
    @Override
    public void Construct() {
        windowHandle = Window.GetWindowHandle();

        // Load shader
        shaderProgram = loadShaders();
    }

    @Override
    public void RenderFrame() {
        glClear(GL_COLOR_BUFFER_BIT);

        // Debug
        glUseProgram(shaderProgram);
        glDrawLines(linesData);
        glDrawRectangles(rectanglesData);
        glUseProgram(0);

        // Swap buffers
        GLFW.glfwSwapBuffers(windowHandle);

        // Clear Data
        linesData.clear();
        rectanglesData.clear();
        circlesData.clear();
    }

    @Override
    public void Deconstruct() {
        // Cleanup shaders
        glDeleteProgram(shaderProgram);
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

        // Compile shaders
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderCode);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderCode);
        glCompileShader(fragmentShader);

        // Link shaders
        int shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);

        // Detach & Delete
        glDetachShader(shaderProgram, vertexShader);
        glDetachShader(shaderProgram, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

        return shaderProgram;
    }

    private void glDrawLines(ArrayList<Line> lines) {
        int[] VAOs = new int[lines.size()], VBOs = new int[lines.size()];

        // Create VAOs & VBOs
        for (int i = 0; i < lines.size(); i++) {
            Line currentLine = lines.get(i);

            float[] vertices = {
                    currentLine.start.getX(), currentLine.start.getY(),
                    currentLine.end.getX(), currentLine.end.getY()
            };

            VAOs[i] = glGenVertexArrays();
            VBOs[i] = glGenBuffers();

            glBindVertexArray(VAOs[i]);
            glBindBuffer(GL_ARRAY_BUFFER, VBOs[i]);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        // Draw
        for (int i = 0; i < lines.size(); i++) {
            glBindVertexArray(VAOs[i]);
            glDrawArrays(GL_LINES, 0, 2);
        }
    }

    private void glDrawRectangles(ArrayList<Rectangle> rectangles) {
        int[] VAOs = new int[rectangles.size()];
        int[] VBOs = new int[rectangles.size()];
        int[] EBOs = new int[rectangles.size()];

        // Create VAOs & VBOs
        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle rect = rectangles.get(i);

            float[] vertices = {
                    rect.position.getX(), rect.position.getY(),
                    rect.position.getX() + rect.size.getX(), rect.position.getY(),
                    rect.position.getX() + rect.size.getX(), rect.position.getY() + rect.size.getY(),
                    rect.position.getX(), rect.position.getY() + rect.size.getY()
            };

            int[] indicies = {
                    0, 1, 2,
                    0, 2, 3
            };

            VAOs[i] = glGenVertexArrays();
            VBOs[i] = glGenBuffers();
            EBOs[i] = glGenBuffers();

            glBindVertexArray(VAOs[i]);
            glBindBuffer(GL_ARRAY_BUFFER, VBOs[i]);
            glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[i]);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicies, GL_STATIC_DRAW);

            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
            glEnableVertexAttribArray(0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        // Draw
        for (int i = 0; i < rectangles.size(); i++) {
            glBindVertexArray(VAOs[i]);
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        }
    }

    /*
        Draw Methods:
     */
    @Override
    public void DrawLine(Vec2 start, Vec2 end, Color color, int thickness) {
        linesData.add(new Line(start, end, color, thickness));
    }

    @Override
    public void DrawRectangle(Vec2 pos, Vec2 size, Color color, boolean fill) {
        rectanglesData.add(new Rectangle(pos, size, color, fill));
    }

    @Override
    public void DrawCircle(Vec2 pos, float radius, Color color, boolean fill) {
        circlesData.add(new Circle(pos, radius, color, fill));
    }

    @Override
    public void DrawSprite() {

    }

    @Override
    public void DrawMesh() {

    }
}
