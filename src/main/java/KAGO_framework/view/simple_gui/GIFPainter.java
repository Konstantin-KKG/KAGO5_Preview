package KAGO_framework.view.simple_gui;

import KAGO_framework.model.GraphicalObject;
import KAGO_framework.model.abitur.datenstrukturen.List;
import KAGO_framework.view.DrawTool;

import javax.swing.*;

/**
 * Dient zur Anzeige eines .gif-Bildes, das auch animiert sein kann.
 * Das Objekt kann beliebig viele .gif-Dateien verwalten, die auf Wunsch jeweils
 * gewechselt werden können.
 */
public class GIFPainter extends GraphicalObject {

    private List<ImageIcon> images;
    private int amount;

    /**
     * Erzeugt ein neues Objekt zur Anzeige eines GIF
     * @param imagePath der relative Pfad zur Bilddatei
     * @param x die x-Koordinate der oberen linken Ecke
     * @param y die y-Koordinate der oberen linken Ecke
     */
    public GIFPainter(String imagePath, double x, double y){
        images = new List<>();
        addImage(imagePath);
        images.toFirst();
        this.x = x;
        this.y = y;
    }

    /**
     * Fügt den anzeigbaren Bildern des Objekts eines hinzu
     * @param imagePath der Pfad zum zu ergänzenden GIF
     */
    public void addImage(String imagePath){
        images.append(new ImageIcon(imagePath));
        amount++;
    }

    /**
     * Ändert die Anzeige auf ein anderes GIF des Objekts
     * @param i der Index des GIFs
     */
    public void setImageByIndex(int i){
        if(!images.isEmpty() && i <= amount){
            int current = 1;
            images.toFirst();
            while(current < i){
                images.next();
            }
        }
    }

    @Override
    public void draw(DrawTool drawTool) {
        if(images.hasAccess()) images.getContent().paintIcon(drawTool.getParent(), drawTool.getGraphics2D(), (int)x, (int)y);
    }

    @Override
    public void update(double dt) {

    }
}
