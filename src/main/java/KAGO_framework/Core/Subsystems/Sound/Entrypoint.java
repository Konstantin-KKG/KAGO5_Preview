package KAGO_framework.Core.Subsystems.Sound;

import KAGO_framework.Core.Subsystems.SubsystemEntrypoint;

public class Entrypoint extends SubsystemEntrypoint {
    @Override
    public void Start() {
        com.sun.javafx.application.PlatformImpl.startup(() -> {});
    }

    @Override
    public void Update() {

    }

    @Override
    public void Stop() {
        com.sun.javafx.application.PlatformImpl.exit();
    }
}
