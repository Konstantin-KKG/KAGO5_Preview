package My_project.view;

import KAGO_framework.Public.Graphics2D.InteractiveGraphicalObject;
import My_project.control.ProgramController;
import java.awt.event.MouseEvent;

/**
 * Realisiert ein Objekt, dass alle Eingaben empfängt und dann danach passende Methoden
 * im ProgramController aufruft
 */
public class InputManager extends InteractiveGraphicalObject {

    private ProgramController programController;

    /**
     * Objekterzeugung
     * @param programController Nötig als Objekt vom Controllerbereich, das informiert wird
     */
    public InputManager(ProgramController programController){
        this.programController = programController;

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}
