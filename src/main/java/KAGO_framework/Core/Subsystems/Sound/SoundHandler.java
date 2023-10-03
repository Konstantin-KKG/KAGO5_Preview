package KAGO_framework.Core.Subsystems.Sound;

import KAGO_framework.Core.Components.Sound;
import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.ComponentHandler;

public class SoundHandler extends ComponentHandler {

    @Override
    public void ExecLogic(Component component) {
        Sound sound = (Sound) component;
        sound.playSound();
    }

}
