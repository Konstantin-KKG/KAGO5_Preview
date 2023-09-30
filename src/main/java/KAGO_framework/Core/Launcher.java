package KAGO_framework.Core;

import KAGO_framework.Config;
import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;

public class Launcher {
    public static void startFramework(){
        Debug.Log(String.format("KAGO Framework Version: %s", Config.VERSION), LogType.INFO);
        Debug.Log(String.format("Supported Java-Versions: %s", Config.JAVA_SUPPORTED), LogType.INFO);
        Debug.Log("Initializing KAGO...", LogType.INFO);

        new Launcher();
    }

    Launcher(){
        GameManager gameManager = new GameManager();
        Thread mainThread = new Thread(gameManager);

        mainThread.start();
    }
}
