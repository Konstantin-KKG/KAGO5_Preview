package KAGO_framework.Core.Subsystems.Test;

import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.ComponentHandler;

public class TestHandler extends ComponentHandler {

    @Override
    public void ExecLogic(Component component) {
        System.out.println("EXEC LOGIC WAS CALLED FOR A TEST COMPONENT");
    }
}
