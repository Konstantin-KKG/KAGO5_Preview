package KAGO_framework.Core.Subsystems.Graphics.Renderer;

import glm_.vec2.Vec2;
import java.awt.Color;

public abstract class RendererBase {
    // Create or Manage: window context
    public abstract void Construct();
    public abstract void RenderFrame();
    public abstract void Deconstruct();

    // Draw:
    public abstract void DrawLine(Vec2 start, Vec2 end, Color color, int thickness);
    public abstract void DrawRectangle(Vec2 pos, Vec2 size, Color color, boolean fill);
    public abstract void DrawCircle(Vec2 pos, float radius, Color color, boolean fill);

    public abstract void DrawSprite();
    public abstract void DrawMesh();
}
