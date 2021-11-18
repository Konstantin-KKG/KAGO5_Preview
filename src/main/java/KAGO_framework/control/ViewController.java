package KAGO_framework.control;

import KAGO_framework.Config;
import KAGO_framework.view.DrawTool;
//import KAGO_scenario_framework.control.ScenarioController;
import my_project.control.ProgramController;
import KAGO_framework.view.DrawFrame;
import KAGO_framework.view.DrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Diese Klasse kontrolliert die DrawingPanels einer ihr zugewiesenen DrawingFrame.
 * Sie kann verschiedene Objekte erzeugen und den Panels hinzufuegen.
 * Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public class ViewController implements ActionListener, KeyListener, MouseListener, MouseMotionListener {

    /**
     * Die innere Klasse kapselt jeweils eine Szene.
     * Diese besteht aus einem Panel auf das gezeichnet wird und das Tastatur- und Mauseingaben empfängt.
     * Außerdem gibt es jeweils eine Liste von Objekte, die gezeichnet und aktualisiert werden sollen
     * und eine Liste von Objekten, die über Eingaben informiert werden sollen
     */
    private class Scene {

        DrawingPanel drawingPanel;
        ArrayList<Drawable> drawables;
        ArrayList<Interactable> interactables;

        Scene(ViewController viewController){
            drawingPanel = new DrawingPanel(viewController);
            drawingPanel.setBackground(new Color(255,255,255));
            drawables = new ArrayList<>();
            interactables = new ArrayList<>();
        }
    }

    // Referenzen
    private DrawFrame drawFrame;    // das Fenster des Programms
    private ProgramController programController; // das Objekt, das das Programm steuern soll
    private Timer gameProcess;
    private static ArrayList<Integer> currentlyPressedKeys = new ArrayList<>();;
    private ArrayList<Scene> scenes;
    private SoundController soundController;

    // Attribute
    private int dt;
    private long lastLoop_Drawables, elapsedTime_Drawables;
    private long lastLoop, elapsedTime;
    private int currentScene;
    private boolean notChangingInteractables, notChangingDrawables;

    /**
     * Erzeugt ein Objekt zur Kontrolle des Programmflusses.
     */
    ViewController(){
        notChangingDrawables = true;
        notChangingInteractables = true;
        scenes = new ArrayList<>();
        // Erzeuge Fenster und erste Szene
        createWindow();
        // Setzt die Ziel-Zeit zwischen zwei aufeinander folgenden Frames in Millisekunden
        dt = 35; //Vernuenftiger Startwert
        if ( Config.INFO_MESSAGES) System.out.println("  > ViewController: Erzeuge ProgramController und starte Spielprozess (Min. dt = "+dt+"ms)...");
        if ( Config.INFO_MESSAGES) System.out.println("     > Es wird nun einmalig die Methode startProgram von dem ProgramController-Objekt aufgerufen.");
        if ( Config.INFO_MESSAGES) System.out.println("     > Es wird wiederholend die Methode updateProgram von dem ProgramController-Objekt aufgerufen.");
        if ( Config.INFO_MESSAGES) System.out.println("-------------------------------------------------------------------------------------------------\n");
        if ( Config.INFO_MESSAGES) System.out.println("** Ab hier folgt das Log zum laufenden Programm: **");
        if(my_project.Config.useSound){
            soundController = new SoundController();
        } else {
            if ( Config.INFO_MESSAGES) System.out.println("** Achtung! Sound deaktiviert => soundController ist NULL (kann in Config geändert werden). **");
        }

        if (!my_project.Config.SHOW_DEFAULT_WINDOW){
            setDrawFrameVisible(false);
            if(Config.INFO_MESSAGES) System.out.println("** Achtung! Standardfenster deaktiviert => wird nicht angezeigt.). **");
        }
        startProgram();
    }

    /**
     * Startet das Programm, nachdem Vorarbeiten abgeschlossen sind.
     */
    private void startProgram(){
        programController = new ProgramController(this);
        programController.startProgram();
        // Starte nebenlaeufigen Prozess, der Zeichnen und Animation uebernimmt
        lastLoop = System.nanoTime();
        gameProcess = new Timer(dt, this);
        gameProcess.start();
    }

    /**
     * Setzt den ViewController in den Startzustand zurück.
     */
    public void reset(){
        scenes = new ArrayList<>();
        createScene();
        showScene(0);
    }

    /**
     * Erzeugt das Fenster und die erste Szene, die sofort angezeigt wird.
     */
    private void createWindow(){
        // Berechne Mitte des Bildschirms
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int x = width / 2;
        int y = height / 2;
        // Berechne die beste obere linke Ecke für das Fenster so, dass es genau mittig erscheint
        x = x - my_project.Config.WINDOW_WIDTH / 2;
        y = y - my_project.Config.WINDOW_HEIGHT / 2;
        // Erzeuge die erste Szene
        createScene();
        // Erzeuge ein neues Fenster zum Zeichnen
        drawFrame = new DrawFrame(my_project.Config.WINDOW_TITLE, x, y, my_project.Config.WINDOW_WIDTH, my_project.Config.WINDOW_HEIGHT, scenes.get(0).drawingPanel);
        drawFrame.setResizable(false);
        showScene(0);
        // Übergibt den weiteren Programmfluss an das neue Objekt der Klasse ViewController
        if ( Config.INFO_MESSAGES) System.out.println("  > ViewController: Fenster eingerichtet. Startszene (Index: 0) angelegt.");
    }

    /**
     * Zeigt die entsprechende Szene in der DrawFrame an. Außerdem ist nur noch die Interaktion mit Objekten dieser Szene möglich.
     * @param index Gibt die Nummer des gewünschten Drawing-Panel-Objekts an.
     */
    public void showScene(int index){
        // Setze das gewuenschte DrawingPanel und lege eine Referenz darauf an.
        if (index < scenes.size()) {
            currentScene = index;
            drawFrame.setActiveDrawingPanel(scenes.get(currentScene).drawingPanel);
        } else {
            if ( Config.INFO_MESSAGES) System.out.println("  > ViewController: Fehler: Eine Szene mit dem Index "+index+" existiert nicht.");
        }
    }

    /**
     * Erzeugt ein neue, leere Szene. Diese wird nicht sofort angezeigt.
     */
    public void createScene(){
        scenes.add(new Scene(this));
    }

    /**
     * Erzeugt ein neue, leere Szene. Diese wird nicht sofort angezeigt.
     * Überschreibt eine bestehende Szene! Wenn der Index höher als die verfügbare
     * Szenenanzahl ist, passiert nichts.
     */
    public void replaceScene(int index){
        if(scenes.size()-1<=index){
            scenes.set(index,new Scene(this));
        }
    }

    public SoundController getSoundController(){
        return soundController;
    }

    /**
     * Zeichnet und aktualisiert ein neues Objekt in der gewünschten Szene
     * @param d Das zu zeichnende Objekt (Interface Drawable muss implementiert werden)
     * @param sceneIndex Die Nummer der Szene für das Objekt
     */
    public void draw(Drawable d, int sceneIndex){
        if ( sceneIndex < scenes.size() && d != null){
            SwingUtilities.invokeLater(() -> scenes.get(sceneIndex).drawables.add(d));
        }
    }

    /**
     * Zeichnet und aktualisiert ein neues Objekt in der aktuellen Szene
     * @param d Das zu zeichnende Objekt.
     */
    public void draw(Drawable d){
        draw(d,currentScene);
    }

    /**
     * Fügt ein Objekt, das das Interactable-Interface implementiert zur aktuellen Szene hinzu, so
     * dass es auf Events reagiert
     * @param i das gewünschte Objekt
     */
    public void register(Interactable i){
        register(i, currentScene);
    }

    /**
     * Fügt ein Objekt, das das Interactable-Interface implementiert zur indizierten Szene hinzu, so
     * dass es auf Events reagiert
     * @param i das gewünschte Objekt
     */
    public void register(Interactable i, int sceneIndex){
        if (sceneIndex < scenes.size() && i!=null){
            SwingUtilities.invokeLater(() -> scenes.get(sceneIndex).interactables.add(i));
        }
    }

    /**
     * Abkuerzende Methode, um ein Objekt vom aktuellen DrawingPanel zu entfernen. Dann wird auch
     * update vom Objekt nicht mehr aufgerufen.
     * @param d Das zu entfernende Objekt.
     */
    public void removeDrawable(Drawable d){
        removeDrawable(d,currentScene);
    }

    /**
     * Entfernt ein Objekt aus einem DrawingPanel. Die Update- und Draw-Methode des Objekts
     * wird dann nicht mehr aufgerufen.
     * @param d Das zu entfernende Objekt
     * @param sceneIndex Der Index des DrawingPanel-Objekts von dem entfernt werden soll
     */
    public void removeDrawable(Drawable d, int sceneIndex){
        if ( sceneIndex < scenes.size() && d != null){
            notChangingDrawables = false;
            SwingUtilities.invokeLater(() -> {
                scenes.get(sceneIndex).drawables.remove(d);
                notChangingDrawables = true;
            });
        }
    }

    /**
     * Abkuerzende Methode, um ein Objekt vom aktuellen DrawingPanel zu entfernen. Dann wird auch
     * update vom Objekt nicht mehr aufgerufen.
     * @param i Das zu entfernende Objekt.
     */
    public void removeInteractable(Interactable i){
        removeInteractable(i,currentScene);
    }

    /**
     * Entfernt ein Objekt aus einem DrawingPanel. Die Update- und Draw-Methode des Objekts
     * wird dann nicht mehr aufgerufen.
     * @param i Das zu entfernende Objekt
     * @param sceneIndex Der Index des DrawingPanel-Objekts von dem entfernt werden soll
     */
    public void removeInteractable(Interactable i, int sceneIndex){
        if ( sceneIndex < scenes.size() && i != null){
            notChangingInteractables = false;
            SwingUtilities.invokeLater(() -> {
                scenes.get(sceneIndex).interactables.remove(i);
                notChangingInteractables = true;
            });
        }
    }

    /**
     * Wird vom Timer-Thread aufgerufen. Es wird dafuer gesorgt, dass das aktuelle Drawing-Panel
     * alle seine Objekte zeichnet und deren Update-Methoden aufruft.
     * Zusaetzlich wird die updateProgram-Methode des GameControllers regelmaeßig nach jeder Frame
     * aufgerufen.
     * @param e Das uebergebene Action-Event-Objekt
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        elapsedTime = System.nanoTime() - lastLoop;
        lastLoop = System.nanoTime();
        int dt = (int) ((elapsedTime / 1000000L));
        double dtSeconds = (double)dt/1000;
        if ( dtSeconds == 0 ) dtSeconds = 0.01;
        // Führe Berechnungen und Aktualisierungen im Hauptobjekt aus
        programController.updateProgram(dtSeconds);
        // Zeichne alle Objekte der aktuellen Szene
        scenes.get(currentScene).drawingPanel.repaint();
        // Aktualisiere SoundController, wenn vorhanden
        if(soundController != null) soundController.update(dtSeconds);
    }

    /**
     * Diese Methode wird vom aktuellen DrawingPanel aufgerufen, sobald es bereit ist, alle Objekte
     * in das Fenster zu zeichnen. Dieser Vorgang wird schnellstmöglich wiederholt.
     * @param drawTool das zur Verfügung gestellte DrawTool des Fensters
     */
    public void drawAndUpdateObjects(DrawTool drawTool){
        elapsedTime_Drawables = System.nanoTime() - lastLoop_Drawables;
        lastLoop_Drawables = System.nanoTime();
        int dt = (int) ((elapsedTime / 1000000L));
        double dtSeconds = (double)dt/1000;
        if ( dtSeconds == 0 ) dtSeconds = 0.01;
        Iterator<Drawable> drawIterator = scenes.get(currentScene).drawables.iterator();
        while (drawIterator.hasNext() && notChangingDrawables){
            Drawable currentObject = drawIterator.next();
            currentObject.draw(drawTool);
            currentObject.update(dtSeconds);
            if (my_project.Config.useSound && soundController != null) soundController.update(dtSeconds);
        }
    }

    /**
     * Diese Methode überprüft, ob die angebene Taste momentan heruntergedrückt ist.
     * @param key Der Tastecode der zu überprüfenden Taste.
     * @return True, falls die entsprechende Taste momentan gedrückt ist, andernfalls false.
     */
    public static boolean isKeyDown(int key){
        return currentlyPressedKeys.contains(key);
    }

    /**
     * Nötig zur Einbindung nativer Java-Fensterelemente
     * @return Liefert das DrawFrame-Objekt zurück (als Schnittstelle zu den JFrame-Methoden)
     */
    public DrawFrame getDrawFrame(){
        return this.drawFrame;
    }

    /**
     * Zeigt das Standardfenster an oder versteckt es.
     * @param b der gewünschte Sichtbarkeitsstatus
     */
    public void setDrawFrameVisible(boolean b){
        drawFrame.setVisible(b);
    }

    /* INTERFACE METHODEN */

    @Override
    public void mouseReleased(MouseEvent e) {
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Wird momentan nicht unterstützt
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Wird momentan nicht unterstützt
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //programController.mouseClicked(e); entfernt 11.11.21 KNB - Simplifizierung & MVC für ProgramController
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.mouseClicked(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.mouseMoved(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.mousePressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // wird momentan nicht unterstützt => einfach keyReleased verwenden
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!currentlyPressedKeys.contains(e.getKeyCode())) currentlyPressedKeys.add(e.getKeyCode());
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.keyPressed(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (currentlyPressedKeys.contains(e.getKeyCode()))
            currentlyPressedKeys.remove(Integer.valueOf(e.getKeyCode()));
        Iterator<Interactable> iterator = scenes.get(currentScene).interactables.iterator();
        while (iterator.hasNext() && notChangingInteractables){
            Interactable tmpInteractable = iterator.next();
            tmpInteractable.keyReleased(e.getKeyCode());
        }
    }

}