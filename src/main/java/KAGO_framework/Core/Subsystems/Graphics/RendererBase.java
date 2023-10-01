package KAGO_framework.Core.Subsystems.Graphics;

public abstract class RendererBase {
    // Create or Manage: window context

    public abstract void DrawLine(float x1, float y1,float x2,float y2);
    public abstract void DrawTriangle(float x1,float y1,float x2,float y2,float x3,float y3);
    public abstract void DrawRectangle(float x,float y,float width,float height);
    // Draw:
    // - Lines,
    // - Squares,
    // - Circles,

    // - Sprites,
    // - 3D Models (If supported)
}
