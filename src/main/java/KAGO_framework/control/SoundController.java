package KAGO_framework.control;

import KAGO_framework.Config;
import KAGO_framework.model.Sound;
import KAGO_framework.model.abitur.datenstrukturen.Queue;
import javafx.embed.swing.JFXPanel;
import javax.swing.*;
import java.util.*;

/**
 * Achtung:
 * Die Nutzung dieser Klasse setzt voraus, dass im Projekt (STRG+ALT+UMSCHALT+S) unter Libraries
 * die JavaFX-Bibliothek registriert ist. Diese befindet sich im Projektverzeichnis im Ordner "javafx_lib".
 *
 * Diese Klasse dient zum Abspielen von Musik und Effekten aus dem Ordner assets/...
 * Je nach PC benötigt die JRE etwas Zeit um den Java-FX-Teil zu initialisieren. Daher
 * kann es dazu kommen, dass Sounds nicht gespielt werden, wenn sie sofort nach Programmstart
 * geladen UND gespielt werden sollen. Eine Sekunde Wartezeit reicht normalerweise dicke.
 */
public class SoundController {

    private double soundDelay = 1;

    /**
     * Kapselt die nötigen Daten für Sounds, die noch geladen werden
     * müssen.
     */
    private class SoundData{

        String filename, name;
        boolean playLooped;

        SoundData(String filename, String name, boolean playLooped){
            this.filename = filename;
            this.name = name;
            this.playLooped = playLooped;
        }
    }

    // Attribute
    private boolean initialized, started;

    // Referenzen
    private ArrayList<Sound> loadedSounds;
    private ArrayList<SoundData> soundsToLoad;
    private Queue<String> playingQueue;

    /**
     * Erzeugt ein Objekt der Klasse SoundController. Es ist nur eines dieser
     * Objekte nötig, um alle Sounds zu verwalten. Der SoundController sollte
     * so früh wie möglich initialisiert werden.
     */
    public SoundController() {
        started = false;
        initialized = false;
        soundsToLoad = new ArrayList<>();
        loadedSounds = new ArrayList<>();
        playingQueue = new Queue<>();
        try {
            SwingUtilities.invokeLater(() -> {
                try {
                    if ( Config.INFO_MESSAGES) System.out.println("Instanziiere JFXPanel für Tonwiedergabe...");
                    new JFXPanel(); // initializes JavaFX environment
                    initialized = true;
                } catch (Exception e) {
                    if ( Config.INFO_MESSAGES) System.out.println("Sound-Initialsierung final fehlgeschlagen (JFX-Panel). Vermutlich nicht unterstützte Java-Version (Java 9 verwenden).");
                }
            });
        } catch (Exception e) {
            if ( Config.INFO_MESSAGES) System.out.println("Sound-Initalisierung partiell fehlgeschlagen. Vermutlich nicht unterstützte Java-Version.");
            e.printStackTrace();
        }
    }

    /**
     * Sorgt für ein zeitverzögertes Laden der Sounds, damit berücksichtigt wird,
     * dass der JavaFX ggf. nicht sofort verfügbar ist.
     */
    public void update(double dt){
        if (initialized){
            if (soundDelay > 0) soundDelay -= dt;
            if(!started){
                started = true;
                if ( Config.INFO_MESSAGES) System.out.println("SoundController hat Arbeit aufgenommen.");
            }
            if(soundDelay <= 0) {
                Iterator<SoundData> iterator = soundsToLoad.iterator();
                while (iterator.hasNext()) {
                    SoundData sd = iterator.next();
                    loadNewSound(sd.filename, sd.name, sd.playLooped);
                    iterator.remove();
                }
                if (!playingQueue.isEmpty()) {
                    Iterator<Sound> iterator2 = loadedSounds.iterator();
                    boolean searching = true;
                    while (iterator2.hasNext() && searching) {
                        Sound currentSound = iterator2.next();
                        if (currentSound.getName().equals(playingQueue.front())) {
                            searching = false;
                            if (currentSound.isPlaying()) currentSound.stop();
                            currentSound.play();
                            playingQueue.dequeue();
                        }
                    }
                }
            }
        }
    }

    /**
     * Lädt einen neuen Sound!
     * @param filename Der Dateiname im Verzeichnis assets. Zum Beispiel "/sounds/wuff.mp3" Möglich sind die meisten Sound-Dateien.
     * @param name Der Name unter dem der Sound im Programm verwendet werden soll. Er muss EINDEUTIG sein.
     * @param playLooped Ob der Sound wiederholt werden soll, wenn er fertig gespielt ist.
     */
    public void loadSound(String filename, String name, boolean playLooped){
        soundsToLoad.add(new SoundData(filename,name,playLooped));
    }

    /**
     * Private Methode zur Erzeugung der Sounddaten.
     * @param filename Der Dateiname im Verzeichnis assets. Zum Beispiel "/sounds/wuff.mp3" Möglich sind die meisten Sound-Dateien.
     * @param name Der Name unter dem der Sound im Programm verwendet werden soll. Er muss EINDEUTIG sein.
     * @param playLooped Ob der Sound wiederholt werden soll, wenn er fertig gespielt ist.
     */
    private void loadNewSound(String filename, String name, boolean playLooped){
        if(initialized) {
            boolean isNotLoaded = true;
            Iterator<Sound> iterator = loadedSounds.iterator();
            while (iterator.hasNext() && isNotLoaded) {
                Sound currentSound = iterator.next();
                if (currentSound.getFilename().equals(filename)) {
                    isNotLoaded = false;
                    System.out.println("FEHLER: Der Sound aus der Datei " + filename + " wurde bereits geladen unter dem Namen " + currentSound.getName());
                }
                if (currentSound.getName().equals(name)) {
                    isNotLoaded = false;
                    System.out.println("FEHLER: Der Name " + name + " wurde schon vergeben für den Sound aus der Datei " + currentSound.getFilename());
                }
            }
            if (isNotLoaded) {
                Sound newSound = new Sound(filename, name, playLooped);
                loadedSounds.add(newSound);
            }
        } else {
            System.out.println("FEHLER: Der SoundController benötigt einen Moment, um sich zu initialisieren - bevor Ton geladen werden kann. Bitte laden verzögern.");
        }
    }

    /**
     * Spielt einen Sound (ggf. wiederholt) ab. Wenn der Sound bereits läuft, wird er zuvor gestoppt.
     * WICHTIGER HINWEIS: eine mp3-Datei kann evtl. nicht wiedergegeben werden, wenn spezielle Album-Art oder ähnliches integriert ist - bei Problemen erst andere mp3s testen
     * @param name Der beim Anlegen des Sounds vergebene, eindeutige Name.
     */
    public void playSound(String name){
        playingQueue.enqueue(name);
    }

    /**
     * Hält den Sound an und setzt ihn auf den Anfang zurück.
     * @param name Der beim Anlegen des Sounds vergebene, eindeutige Name.
     */
    public void stopSound(String name){
        if(isPlaying(name)) {
            Iterator<Sound> iterator = loadedSounds.iterator();
            boolean searching = true;
            while (iterator.hasNext() && searching) {
                Sound currentSound = iterator.next();
                if (currentSound.getName().equals(name)) {
                    currentSound.stop();
                    searching = false;
                }
            }
        }
    }

    /**
     * Überprüft, ob der spezifizierte Sound gerade abgespielt wird.
     * @param name der eindeutige Name des Sounds
     * @return true, falls der Sound gerade abgespielt wird, sonst false
     */
    public boolean isPlaying(String name){
        for (Sound currentSound : loadedSounds)
            if (currentSound.getName().equals(name)) return currentSound.isPlaying();
        return false;
    }

    /**
     * Ändert die Lautstärke eines Sounds. Funktioniert erst, wenn das Programm schon etwas läuft.
     * @param soundName der gesuchte Sound (muss geladen sein)
     * @param volume die neue Lautstärke (zwischen 0 und 1)
     */
    public void setVolume(String soundName, double volume){
        Sound toChange = getSound(soundName);
        try{
            if(toChange!=null) toChange.setVolume(volume);
        } catch (Exception e){
            if( Config.INFO_MESSAGES) System.out.println("HINWEIS: Lautstärke konnte nicht angepasst werden. Bitte Anforderung etwas verzögern (ein paar Sekunden).");
        }
    }

    /**
     * Sucht einen geladenen Sound heraus
     * @param soundName der gesuchte Sound
     * @return das Sound-Objekt, falls vorhanden
     */
    private Sound getSound(String soundName){
        for (Sound currentSound : loadedSounds) if (currentSound.getName().equals(soundName)) return currentSound;
        return null;
    }

    /**
     * Liefert, ob der Controller einsatzbereit ist
     * @return true, falls einsatzbereit, sonst false
     */
    public boolean getInitialized(){
        return initialized;
    }

}
