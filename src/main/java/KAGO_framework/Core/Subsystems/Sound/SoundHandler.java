package KAGO_framework.Core.Subsystems.Sound;

import KAGO_framework.Core.Components.Sound;
import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.ComponentHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class SoundHandler extends ComponentHandler {

    private static ArrayList<Sound> sounds = new ArrayList<>();
    @Override
    public void ExecLogic(Component component) {

    }

    public static void ExecLogicImminently(Sound sound){
        createSoundPlayer(sound.getFilename(), sound.getLooping(), sound);
        sounds.add(sound);
    }

    public static void Stop(){
        for(Sound sound : sounds)
            sound.Delete();
    }

    private static void createSoundPlayer(String filepath, boolean looping, Sound sound){
        com.sun.javafx.application.PlatformImpl.startup(() -> {});

        Media media = new Media(new File(filepath).toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(media);

        if(looping)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        sound.setMediaPlayer(mediaPlayer);
    }
}
