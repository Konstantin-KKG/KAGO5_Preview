package KAGO_framework.Core.Subsystems.Graphics.Datatypes;

import glm_.vec2.Vec2;
import java.awt.Color;

public class Line {
    public Line(Vec2 start, Vec2 end, Color color, int thickness) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.thickness = thickness;
    }

    public Vec2 start;
    public Vec2 end;
    public Color color;
    public int thickness;
}
