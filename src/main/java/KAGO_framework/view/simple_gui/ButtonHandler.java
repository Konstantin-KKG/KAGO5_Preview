package KAGO_framework.view.simple_gui;

import KAGO_framework.control.ViewController;

/**
 * Interface für Objekte, die Buttons erzeugen wollen
 * Stellt sich, dass der Button alle nötigen Infos erhält
 */
public interface ButtonHandler {

    /**
     * Wird vom Button aufgerufen, wenn er angeklickt wird
     * @param code der Aktionscode, spezifiziert durch den ButtonHandler
     */
    void processButtonClick(int code);

    /**
     * Liefert den Index der Szene für die Buttons des Buttonhandlers zurück
     * @return Muss den Index der Szene zurückliefert, in der die Buttons benutzt werden sollen
     */
    int getSceneIndex();

    /**
     * Stellt den Buttons den ViewController zur Verfügung
     * @return der ViewController des Frameworks
     */
    ViewController getViewController();

}
