package KAGO_framework.control;

import KAGO_framework.view.DrawTool;

/**
 * Interface für Objekte, die man zeichnen und steuern kann.
 * Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public interface Drawable {

    void draw(DrawTool drawTool);

    void update(double dt);

}
