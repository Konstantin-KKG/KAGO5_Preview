package KAGO_framework.Core.Subsystems.Graphics.OpenGL2D;

import KAGO_framework.Core.GameManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Renderer extends KAGO_framework.Core.Subsystems.Graphics.RendererBase {

    private VertexArrayObject VAO;
    private BufferObject VBO,EBO;
    private long window;
    private int shaderProgram;
    private final float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };
    private final int[] indices = {0, 1, 2};
    public Renderer(){
    }
    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        // Create the window
        window = glfwCreateWindow(800, 800, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();


        glViewport(0, 0, 800, 800);

        boolean success = createShaders();
        if(!success)
            System.err.println("Failed to Create/Compile Shaders");

        glUseProgram(shaderProgram);

        VBO = new BufferObject(GL_ARRAY_BUFFER,vertices);
        EBO = new BufferObject(GL_ELEMENT_ARRAY_BUFFER,indices);
        VAO = new VertexArrayObject(VBO,EBO);
        VAO.AttributePointer(0,3);

        // Set the clear color
        glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    }

    public void loop() {
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer



            glUseProgram(shaderProgram);
            VAO.Bind();

            glDrawElements(GL_TRIANGLES,vertices.length,GL_UNSIGNED_INT ,0);

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
            glfwSwapBuffers(window); // swap the color buffers

        }
    }
    //TODO Integrate Logging
    private boolean createShaders(){
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
        glShaderSource(vertexShader,vertexShaderSource);
        glCompileShader(vertexShader);

        int success = glGetShaderi(vertexShader ,GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
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
        success = glGetShaderi(fragmentShader ,GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            System.out.println("Compilation Fail(FragmentShader)");
            System.out.println(glGetShaderInfoLog(fragmentShader));
            failed = true;
        }

        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragmentShader);

        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success == GL_FALSE) {
            System.out.println("Link Fail(ShaderProgram)");
            System.out.println(glGetProgramInfoLog(shaderProgram));
            failed = true;
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
        return !failed;
    }

}

