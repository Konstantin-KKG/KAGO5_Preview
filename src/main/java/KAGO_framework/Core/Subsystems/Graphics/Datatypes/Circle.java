package KAGO_framework.Core.Subsystems.Graphics.Datatypes;

import glm_.vec2.Vec2;
import java.awt.Color;

public class Circle {
    public Circle(Vec2 position, float radius, Color color, boolean fill) {
        this.position = position;
        this.radius = radius;
        this.color = color;
        this.fill = fill;
    }

    public Vec2 position;
    public float radius;
    public Color color;
    public boolean fill;
}
