package KAGO_framework.Public.Graphics2D;

import KAGO_framework.Private.Renderer.Legacy2D.DrawTool;

/**
 * Interface für Objekte, die man zeichnen und steuern kann.
 * Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public interface Drawable {

    void draw(DrawTool drawTool);

    void update(double dt);

}
