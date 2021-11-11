package KAGO_framework.view;

import KAGO_framework.control.ViewController;

import javax.swing.*;
import java.awt.*;
/**
 * Stellt eine Zeichenfläche in einem DrawFrame-Fenster dar. Beim DrawingPanel über die Methode "add" registrierte
 * Objekte werden vom Panel gezeichnet. Außerdem kümmert sich das DrawingPanel um das Aufrufen der im framework
 * realisierten Callbacks wie etwa update und draw.
 * Diese Modellierung ist nicht sauber, da das DrawingPanel damit Funktionen eines Controllers übernimmt.
 * Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public class DrawingPanel extends JPanel  {

    //Attribute
    private boolean requested = false;

    // Referenzen
    private DrawTool drawTool;
    private ViewController viewController;

    /**
     * Konstruktor
     */
    public DrawingPanel(ViewController viewController){
        super();
        this.viewController = viewController;
        setDoubleBuffered(true);
        drawTool = new DrawTool();
    }

    /**
     * Zeichnen aller registrierten Objekte
     */
    @Override
    public void paintComponent(Graphics g) {
        if(!requested){
            addMouseListener(viewController);
            addKeyListener(viewController);
            addMouseMotionListener(viewController);
            setFocusable(true);
            requestFocusInWindow();
            requested = ! requested;
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawTool.setGraphics2D(g2d,this);
        viewController.drawAndUpdateObjects(drawTool);
    }

}

