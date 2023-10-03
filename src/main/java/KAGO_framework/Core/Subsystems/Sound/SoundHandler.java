package KAGO_framework.Core.Subsystems.Sound;

import KAGO_framework.Core.Components.Sound;
import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.ComponentHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class SoundHandler extends ComponentHandler {

    @Override
    public void ExecLogic(Component component) {
        Sound sound = (Sound) component;
        sound.playSound();
    }

}
