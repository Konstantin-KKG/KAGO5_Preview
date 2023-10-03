package KAGO_framework.Core.Subsystems.Graphics.Renderer;

import glm_.vec2.Vec2;
import java.awt.Color;

public abstract class RendererBase {
    // Create or Manage: window context
    public abstract void Construct();
    public abstract void RenderFrame();
    public abstract void Deconstruct();
}
