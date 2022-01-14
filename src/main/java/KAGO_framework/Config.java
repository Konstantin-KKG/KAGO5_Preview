package KAGO_framework;

/**
 * In dieser Klasse werden globale, statische Einstellungen verwaltet.
 * Diese beziehen sich nur auf die Funktionsweise des Frameworks.
 * Für individuelle Einstellungen am eigenen Projekt sollte die Config-Datei im Paket "my_project"
 * verwendet werden.
 */
public class Config {

    // Frameworkversion
    public final static String VERSION = "KNB-AOS-GraphicalObject-Java-Framework - 4.5b - 14.01.2022";
    public final static String JAVA_SUPPORTED = "Java 11 bis Java 15";

    // Schaltet die Infomeldungen des Frameworks an oder aus
    public final static boolean INFO_MESSAGES = true;
    public final static boolean DEBUG = false;

}