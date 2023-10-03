package KAGO_framework.Core.Components;

import KAGO_framework.Core.Subsystems.Component;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound extends Component {
    private String filename;
    private boolean looping = false;
    private MediaPlayer mediaPlayer;

    public Sound(String filename, boolean looping){
        this.filename = filename;
        this.looping = looping;
        createMediaPlayer(filename, looping);
    }

    public void playSound(){
        mediaPlayer.play();
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
