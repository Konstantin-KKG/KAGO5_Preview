package KAGO_framework.view;

import KAGO_framework.Config;

import javax.swing.*;

/** Entspricht einem Fenster, das DrawingPanels beinhalten kann.
 *  Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public class DrawFrame extends JFrame {

    // Attribute

    // Referenzen
    private DrawingPanel activePanel;           // Das im Moment sichtbare DrawingPanel

    /**
     * Konstruktor
     * @param name Der Titel des Fensters
     * @param x Die obere linke x-Koordinate des Fensters bzgl. des Bildschirms
     * @param y Die obere linke y-Koordinaite des Fensters bzgl. des Bildschirms
     * @param width Die Breite des Fensters
     * @param height Die Höhe des Fensters
     */
    public DrawFrame(String name, int x, int y, int width, int height, DrawingPanel startingPanel) {
        activePanel = startingPanel;
        setLocation(x,y);
        setSize(width,height);
        setTitle(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        if ( Config.INFO_MESSAGES) System.out.println("  > DrawFrame: Fenster ist initialisiert.");
    }

    /**
     * Ändert das aktuell vom DrawFrame gezeigte DrawingPanel.
     * @param panel Das anzuzeigende Panel.
     */
    public void setActiveDrawingPanel(DrawingPanel panel){
        remove(panel);
        add(panel);
        revalidate();
        activePanel = panel;
    }

    @Override
    /**
     * Gibt die Breite des im Fenster liegenden DrawingPanels zurück
     */
    public int getWidth(){
        return activePanel.getWidth();
    }

    @Override
    /**
     * Gibt die Höhe des im Fenster liegenden DrawingPanels zurück
     */
    public int getHeight(){
        return activePanel.getHeight();
    }

}

