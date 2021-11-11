package KAGO_framework.view.simple_gui;

import KAGO_framework.control.Interactable;
import KAGO_framework.view.DrawTool;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Repräsentiert ein anklickbares Label
 */
public class Button extends Label implements Interactable {

    //Referenzen
    private ButtonHandler buttonHandler;

    //Attribute
    private int actionCode;

    /**
     * Erzeugt einen neuen, simplen und nicht sonderlich schönen, aber funktionalen Button
     * Entspricht einem anklickbaren Label
     * @param bH der zugewiesene ButtonHandler
     * @param actionCode der Action-Code, den dieser Button benutzen soll
     * @param x x obere linke Ecke des Buttons
     * @param y y obere linke Ecke des Buttons
     * @param text Beschriftung des Buttons
     * @param textsize Textgröße für die Beschriftung
     */
    public Button(ButtonHandler bH, int actionCode, double x, double y, String text, int textsize){
        super(x,y,text,textsize,true);
        buttonHandler = bH;
        buttonHandler.getViewController().draw(this,buttonHandler.getSceneIndex());
        buttonHandler.getViewController().register(this,buttonHandler.getSceneIndex());
        this.actionCode = actionCode;
    }

    /**
     * Erzeugt einen neuen, simplen und nicht sonderlich schönen, aber funktionalen Button
     * Entspricht einem anklickbaren Label
     * @param bH der zugewiesene ButtonHandler
     * @param actionCode der Action-Code, den dieser Button benutzen soll
     * @param x x obere linke Ecke des Buttons
     * @param y y obere linke Ecke des Buttons
     * @param image das Bild, das der Button benutzen soll
     * @param hasBorder true, falls ein Rahmen gezeichnet werden soll
     */
    public Button(ButtonHandler bH, int actionCode, double x, double y, BufferedImage image, boolean hasBorder){
        super(x,y,image,hasBorder);
        buttonHandler = bH;
        buttonHandler.getViewController().draw(this);
        buttonHandler.getViewController().register(this);
        this.actionCode = actionCode;
    }

    @Override
    public void draw(DrawTool drawTool) {
        super.draw(drawTool);
    }

    @Override
    public void update(double dt) {

    }

    /**
     * Sendet bei Mausklick auf den Button den entsprechenden ActionCode an den zugehörigen
     * ButtonHandler
     * @param e Das übergebene Objekt der Klasse MouseEvent enthält alle Information über das Ereignis.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        double mouseX = e.getX();
        double mouseY = e.getY();
        if(mouseX > x && mouseX < x+width && mouseY > y && mouseY < y+height){
            buttonHandler.processButtonClick(actionCode);
        }
    }

    @Override
    public void keyPressed(int key) {

    }
    @Override
    public void keyReleased(int key) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

}
