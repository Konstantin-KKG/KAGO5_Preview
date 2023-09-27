package KAGO_framework.Core;

import KAGO_framework.Config;
import java.awt.*;

public class Launcher {
    public static void startFramework(){
        if (Config.INFO_MESSAGES) {
            System.out.println("***** PROGRAM START (Framework: " + Config.VERSION + ") *****.");
            System.out.println("** Supported Java-Versions: " + Config.JAVA_SUPPORTED);
            System.out.println("** Initializing Framework: **");
        }

        EventQueue.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        if ( Config.INFO_MESSAGES)
                            System.out.println("  > KAGO-Main-Method: Program Start. Creating KAGO Launcher Instance.");
                        new Launcher();
                    }
                });
    }

    public Launcher(){
        if ( Config.INFO_MESSAGES)
            System.out.println("  > KAGO-Launcher: Successfully created. Setting up Game Systems...");

        new GameManager();
    }

}
