package KAGO_framework.Core.Subsystems.Graphics.Vulkan2D;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Core.Subsystems.Graphics.RendererBase;
import KAGO_framework.Core.Subsystems.Graphics.Window;

public class Renderer extends RendererBase {
    private long windowHandle;

    @Override
    public void Construct() {
        windowHandle = Window.GetWindowHandle();
        Debug.Log("Vulkan backend is not yet implemented.", LogType.ERROR);
    }

    @Override
    public void RenderFrame() {

    }

    @Override
    public void Deconstruct() {

    }
}
