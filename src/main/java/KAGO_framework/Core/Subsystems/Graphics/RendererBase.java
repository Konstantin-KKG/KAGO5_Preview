package KAGO_framework.Core.Subsystems.Graphics;

public abstract class RendererBase {
    // Create or Manage: window context
    public abstract void Construct();
    public abstract void RenderFrame();
    public abstract void Deconstruct();

    // Draw:
    // - Lines,
    // - Squares,
    // - Circles,

    // - Sprites,
    // - 3D Models (If supported)
}
