package MyProject;

import KAGO_framework.Core.Subsystems.Graphics.RendererOptions;

/**
 * In dieser Klasse werden globale, statische Einstellungen verwaltet.
 * Die Werte können nach eigenen Wünschen angepasst werden.
 */
public class Config {

    // Window Settings
    public final static String WINDOW_TITLE = "Empty Project - KAGO.5";
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720 + 29; // The 29 pixels extra are for the title bar

    // Other Settings
    public final static RendererOptions RENDERER_OPTION = RendererOptions.LEGACY_2D;
    public final static boolean USE_SOUND = true;
}
