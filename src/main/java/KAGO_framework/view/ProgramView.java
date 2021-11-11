package KAGO_framework.view;

import KAGO_framework.control.ViewController;
import KAGO_framework.model.InteractiveGraphicalObject;
import KAGO_framework.view.DrawTool;
import my_project.control.ProgramController;
import java.awt.event.MouseEvent;

/**
 * Diese Klasse stellt eine Vorlage für die Konstruktion einer GUI bereit, die sich
 * aus grafischen Objekten des Frameworks zusammensetzt
 */
public abstract class ProgramView extends InteractiveGraphicalObject {

    protected ViewController viewController;
    protected ProgramController programController;

    /**
     * Erzeugt ein Objekt der Klasse GameView
     * @param viewController das ViewController-Objekt des Frameworks
     * @param programController das ProgramController-Objekt des Frameworks
     */
    public ProgramView(ViewController viewController, ProgramController programController){
        this.viewController = viewController;
        this.programController = programController;
        viewController.register(this);
        viewController.draw(this);
    }

    /**
     * Entfernt die View aus der aktuellen DrawFrame.
     * Achtung: eventuell über die GUI hinzugefügte Objekte werden nicht entfernt
     */
    public void disposeView(){
        viewController.removeDrawable(this);
        viewController.removeInteractable(this);
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

    @Override
    public void draw(DrawTool drawTool) {

    }

    @Override
    public void update(double dt) {

    }
}
