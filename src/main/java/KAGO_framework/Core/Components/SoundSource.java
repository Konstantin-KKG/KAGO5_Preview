package KAGO_framework.Core.Components;

import KAGO_framework.Core.Subsystems.Component;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundSource extends Component {
    private final MediaPlayer MEDIA_PLAYER;

    public SoundSource(String filepath, float volume, boolean onStart, boolean looping) {
        Media media = new Media(new File(filepath).toURI().toString());
        MEDIA_PLAYER = new MediaPlayer(media);

        setVolume(volume);

        if (looping)
            MEDIA_PLAYER.setCycleCount(MediaPlayer.INDEFINITE);

        if (onStart)
            playSound();
    }

    public void playSound() {
        MEDIA_PLAYER.play();
    }

    public void stopSound() {
        MEDIA_PLAYER.stop();
    }

    /**
     * Sets the volume of the sound.
     * Effective range 0 - 1
     */
    public void setVolume(double volume) {
        MEDIA_PLAYER.setVolume(volume);
    }
}
