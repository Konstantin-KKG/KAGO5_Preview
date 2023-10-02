package KAGO_framework.Core.Subsystems.Graphics.Datatypes;

import glm_.vec2.Vec2;
import java.awt.Color;

public class Rectangle {
    public Rectangle(Vec2 position, Vec2 size, Color color, boolean fill) {
        this.position = position;
        this.size = size;
        this.color = color;
        this.fill = fill;
    }

    public Vec2 position;
    public Vec2 size;
    public Color color;
    public boolean fill;
}
