package KAGO_framework.control;

import KAGO_framework.Config;

import java.awt.*;

/**
 * Diese Klasse enthält die main-Methode. Von ihr wird als erstes ein Objekt innerhalb der main-Methode erzeugt,
 * die zu Programmstart aufgerufen wird und kein Objekt benötigt, da sie statisch ist.
 * Die erste Methode, die also nach der main-Methode aufgerufen wird, ist der Konstruktor dieser Klasse. Aus ihm
 * wird alles weitere erzeugt.
 * Vorgegebene Klasse des Frameworks. Modifikation auf eigene Gefahr.
 */
public class MainController {

    // Attribute

    // Referenzen

    /**
     * Diese Methode startet das gesamte Framework und erzeugt am Ende dieses Prozesses ein Objekt der Klasse
     * ProgramController aus dem Paket "my_project > control"
     */
    public static void startFramework(){
        if ( Config.INFO_MESSAGES) System.out.println("***** PROGRAMMSTART ("+" Framework: "+Config.VERSION+") *****.");
        if ( Config.INFO_MESSAGES) System.out.println("** Supported Java-Versions: "+ Config.JAVA_SUPPORTED);
        if ( Config.INFO_MESSAGES) System.out.println("");
        if ( Config.INFO_MESSAGES) System.out.println("** Ablauf der Framework-Initialisierung: **");
        EventQueue.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if ( Config.INFO_MESSAGES) System.out.println("  > Main-Methode: Programmstart. Erzeuge ein Objekt der Main-Controller-Klasse.");
                        new MainController();
                    }
                });
    }

    /**
     * Der Konstruktor der Klasse-MainController ist die erste Methode, die nach der Main-Methode
     * aufgerufen wird. Hier wird der Programmfluss geregelt.
     */
    public MainController(){
        if ( Config.INFO_MESSAGES) System.out.println("  > MainController: Ich wurde erzeugt. Erstelle ein ViewController-Objekt zur Steuerung der View...");
        new ViewController();
    }

}
