package KAGO_framework.Core.Subsystems.Sound;

import KAGO_framework.Core.Subsystems.SubsystemEntrypoint;

public class Entrypoint extends SubsystemEntrypoint {
    @Override
    public void Start() {

    }

    @Override
    public void Update() {

    }

    @Override
    public void Stop() {
        SoundHandler.Stop();
    }
}
