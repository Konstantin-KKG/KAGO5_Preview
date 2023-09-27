package KAGO_framework.Core.Components;

import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.Test.TestHandler;

import java.lang.reflect.Type;

public class Test extends Component {

    @Override
    public Type GetComponentHandlerType() {
        return TestHandler.class;
    }
}
