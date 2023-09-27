package KAGO_framework.Core;

import KAGO_framework.Config;
import MyProject.control.GameController;

public class GameManager {

    // Refs
    private GameController gameController;

    GameManager(){
        // createWindow();

        if (Config.INFO_MESSAGES) {
            System.out.println("  > GameManager: Create GameController and Start Game...");
            System.out.println("     > Execute startProgram method once.");
            System.out.println("     > Execute updateProgram method from now on repeatedly.");
            System.out.println("-------------------------------------------------------------------------------------------------\n");
            System.out.println("** Logs from now on relate to the user written program in \"MyProject\" **");
        }

        // startProgram();
    }
}