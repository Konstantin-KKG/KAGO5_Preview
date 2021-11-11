package my_project;

/**
 * In dieser Klasse werden globale, statische Einstellungen verwaltet.
 * Die Werte können nach eigenen Wünschen angepasst werden.
 */
public class Config {

    // Titel des Programms (steht oben in der Fenstertitelzeile)
    public final static String WINDOW_TITLE = "Leeres Vorlagenprojekt des KAGO-Frameworks";

    // Konfiguration des Standardfensters: Anzeige und Breite des Programmfensters (Width) und Höhe des Programmfensters (Height)
    public final static boolean SHOW_DEFAULT_WINDOW = true;
    public final static int WINDOW_WIDTH = 600;
    public final static int WINDOW_HEIGHT = 600+29;   // Effektive Höhe ist etwa 29 Pixel geringer (Titelleiste wird mitgezählt)

    // Weitere Optionen für das Projekt
    public final static boolean useSound = true;

}
