package KAGO_framework.Core.Subsystems.Graphics.BetterRenderer.Shader;

public class Shader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/main/java/KAGO_framework/Core/Subsystems/Graphics/BetterRenderer/Shader/VertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/java/KAGO_framework/Core/Subsystems/Graphics/BetterRenderer/Shader/FragmentShader.txt";

    // Space for possible uniform locations (becomes especially relevant when rendering 3D Entities)

    public Shader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "v_position");
        super.bindAttribute(1, "v_color");
    }
}