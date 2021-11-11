package KAGO_framework.control;

import java.awt.event.MouseEvent;

public interface Interactable {

    /**
     * Wird einmalig aufgerufen, wenn eine Taste heruntergedrückt wird. Nach der Anschlagverzögerung löst Windows den Tastendruck dann
     * in schneller Folge erneut aus. Eignet sich NICHT, um Bewegungen zu realisieren.
     * @param key Enthält den Zahlencode für die Taste. Kann direkt aus der Klasse KeyEvent geladen werden, z.B. KeyEvent_VK_3
     */
    void keyPressed(int key);

    /**
     * Wird einmalig aufgerufen, wenn eine Taste losgelassen wird.
     * @param key Enthält den Zahlencode für die Taste. Kann direkt aus der Klasse KeyEvent geladen werden, z.B. KeyEvent_VK_3
     */
    void keyReleased(int key);

    /**
     * Wird einmalig aufgerufen, wenn eine Maustaste losgelassen wurde.
     * @param e Das übergebene Objekt der Klasse MouseEvent enthält alle Information über das Ereignis.
     */
    void mouseReleased(MouseEvent e);

    /**
     * Wird einmalig aufgerufen, wenn eine Maustaste geklickt wurde.
     * @param e Das übergebene Objekt der Klasse MouseEvent enthält alle Information über das Ereignis.
     */
    void mouseClicked(MouseEvent e);

    /**
     * Wird einmalig aufgerufen, wenn eine Maustaste gehalten wird und die Maus bewegt wird.
     * @param e Das übergebene Objekt der Klasse MouseEvent enthält alle Information über das Ereignis.
     */
    void mouseDragged(MouseEvent e);

    /**
     * Wird einmalig aufgerufen, wenn die Maus bewegt wurde.
     * @param e Das übergebene Objekt der Klasse MouseEvent enthält alle Information über das Ereignis.
     */
    void mouseMoved(MouseEvent e);

    /**
     * Wird einmalig aufgerufen, wenn eine Maustaste heruntergedrückt wurde.
     * @param e Das übergebene Objekt der Klasse MouseEvent enthält alle Information über das Ereignis.
     */
    void mousePressed(MouseEvent e);

}
