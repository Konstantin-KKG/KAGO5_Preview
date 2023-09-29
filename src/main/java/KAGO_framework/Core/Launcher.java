package KAGO_framework.Core;

import KAGO_framework.Config;

import javax.swing.plaf.synth.SynthRadioButtonMenuItemUI;
import java.awt.*;

public class Launcher {
    public static void startFramework(){
        if (Config.INFO_MESSAGES) {
            System.out.println("***** PROGRAM START (Framework: " + Config.VERSION + ") *****.");
            System.out.println("** Supported Java-Versions: " + Config.JAVA_SUPPORTED);
            System.out.println("** Initializing Framework: **");
        }

        Launcher newLauncher = new Launcher();
    }

    Launcher(){
        if ( Config.INFO_MESSAGES)
            System.out.println("  > KAGO-Launcher: Successfully created. Setting up Game Systems...");

        GameManager gameManager = new GameManager();
        Thread mainThread = new Thread(gameManager);

        mainThread.start();
    }
}
