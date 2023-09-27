package KAGO_framework.Core.Graphics.Legacy2D;

import KAGO_framework.Config;
import KAGO_framework.Core.Sound.SoundController;
import MyProject.control.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// implements ActionListener, KeyListener, MouseListener, MouseMotionListener
public class OldGameManager implements ActionListener {

    // Referenzen
    private DrawFrame drawFrame;    // das Fenster des Programms
    private DrawingPanel drawingPanel;
    private GameController gameController; // das Objekt, das das Programm steuern soll
    private Timer gameProcess;
    private SoundController soundController;

    // Attribute
    private int dt;
    private long lastLoop_Drawables, elapsedTime_Drawables;
    private long lastLoop, elapsedTime;
    private int currentScene;

    OldGameManager(){
        // Erzeuge Fenster und erste Szene
        createWindow();

        // Setzt die Ziel-Zeit zwischen zwei aufeinander folgenden Frames in Millisekunden
        dt = 35; // Vernuenftiger Startwert
        if ( Config.INFO_MESSAGES ) System.out.println("  > ViewController: Erzeuge ProgramController und starte Spielprozess (Min. dt = "+dt+"ms)...");
        if ( Config.INFO_MESSAGES ) System.out.println("     > Es wird nun einmalig die Methode startProgram von dem ProgramController-Objekt aufgerufen.");
        if ( Config.INFO_MESSAGES ) System.out.println("     > Es wird wiederholend die Methode updateProgram von dem ProgramController-Objekt aufgerufen.");
        if ( Config.INFO_MESSAGES ) System.out.println("-------------------------------------------------------------------------------------------------\n");
        if ( Config.INFO_MESSAGES ) System.out.println("** Ab hier folgt das Log zum laufenden Programm: **");

        if (MyProject.Config.USE_SOUND){
            soundController = new SoundController();
        } else {
            if ( Config.INFO_MESSAGES)
                System.out.println("** Achtung! Sound deaktiviert => soundController ist NULL (kann in Config geändert werden). **");
        }

        startProgram();
    }

    private void createWindow(){
        // Berechne Mitte des Bildschirms
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int x = width / 2;
        int y = height / 2;

        // Berechne die beste obere linke Ecke für das Fenster so, dass es genau mittig erscheint
        x = x - MyProject.Config.WINDOW_WIDTH / 2;
        y = y - MyProject.Config.WINDOW_HEIGHT / 2;

        // Erzeuge ein neues Fenster zum Zeichnen
        drawingPanel = new DrawingPanel(this);
        drawFrame = new DrawFrame(MyProject.Config.WINDOW_TITLE, x, y, MyProject.Config.WINDOW_WIDTH, MyProject.Config.WINDOW_HEIGHT, drawingPanel);
        drawFrame.setResizable(false);

        // Übergibt den weiteren Programmfluss an das neue Objekt der Klasse ViewController
        if (Config.INFO_MESSAGES) System.out.println("  > ViewController: Fenster eingerichtet. Startszene (Index: 0) angelegt.");
    }

    private void startProgram(){
        // Start gameController

        // Starte nebenlaeufigen Prozess, der Zeichnen und Animation uebernimmt
        lastLoop = System.nanoTime();
        gameProcess = new Timer(dt, this);
        gameProcess.start();
    }

    public SoundController getSoundController(){
        return soundController;
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
        if (dtSeconds == 0)
            dtSeconds = 0.01;

        // Führe Berechnungen und Aktualisierungen im Hauptobjekt aus
        gameController.updateProgram(dtSeconds);

        // Aktualisiere SoundController, wenn vorhanden
        if (soundController != null) soundController.update(dtSeconds);
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
        if ( dtSeconds == 0 )
            dtSeconds = 0.01;
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
}