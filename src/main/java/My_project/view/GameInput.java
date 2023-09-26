package My_project.view;

import KAGO_framework.Public.Graphics2D.Interactable;
import My_project.control.GameController;
import java.awt.event.MouseEvent;

/**
 * Realisiert ein Objekt, dass alle Eingaben empfängt und dann danach passende Methoden
 * im ProgramController aufruft
 */
public class GameInput implements Interactable {

    private GameController gameController;

    /**
     * Objekterzeugung
     * @param gameController Nötig als Objekt vom Controllerbereich, das informiert wird
     */
    public GameInput(GameController gameController){
        this.gameController = gameController;

    }

    @Override
    public void keyPressed(int key) {

    }

    @Override
    public void keyReleased(int key) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse clicked");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

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
