package KAGO_framework.Core.Components;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Core.Subsystems.Component;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound extends Component {
    private String filename;
    private boolean looping = false;
    private boolean playing = false;
    private MediaPlayer mediaPlayer;

    public Sound(String filename, boolean looping){
        this.filename = filename;
        this.looping = looping;
        createMediaPlayer(filename, looping);
    }

    /** STOP!!! <br></br>
     * This method is used by KAGO5. <br></br>
     * To play or stop a sound, use playSound() or stopSound()
     */
    public void switchMediaPlayerState(){
        if(playing) {
            mediaPlayer.play();
        } else {
            mediaPlayer.stop();
        }
    }

    /**
     * This method plays a sound.
     */
    public void playSound(){
        if(!playing) {
            playing = true;
        } else {
            Debug.Log("Sound is already playing!", LogType.WARNING);
        }
    }

    /**
     * If the sound is looped, use this method to stop the sound.
     */
    public void stopSound(){
        if(playing && looping) {
            playing = false;
        } else if (!playing){
            Debug.Log("Sound is not playing!", LogType.WARNING);
        } else {
            Debug.Log("Sound is not looped!", LogType.WARNING);
        }
    }

    private void setMediaPlayer(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }

    private void createMediaPlayer(String filepath, boolean looping){
        com.sun.javafx.application.PlatformImpl.startup(() -> {});

        Media media = new Media(new File(filepath).toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);

        if(looping)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        setMediaPlayer(mediaPlayer);
    }

}
