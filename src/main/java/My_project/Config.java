package My_project;

import KAGO_framework.Core.Graphics.RendererOptions;

/**
 * In dieser Klasse werden globale, statische Einstellungen verwaltet.
 * Die Werte können nach eigenen Wünschen angepasst werden.
 */
public class Config {

    // Window Settings
    public final static String WINDOW_TITLE = "Empty Project - KAGO.5";
    public final static int WINDOW_WIDTH = 1280;
    public final static int WINDOW_HEIGHT = 720 + 29; // Effektive Höhe ist etwa 29 Pixel geringer (Titelleiste wird mitgezählt)

    // Other Settings
    public final static RendererOptions RENDERER_OPTION = RendererOptions.Legacy2D;
    public final static boolean USE_SOUND = true;
}
