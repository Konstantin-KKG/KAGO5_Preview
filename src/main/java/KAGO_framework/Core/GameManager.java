package KAGO_framework.Core;

import KAGO_framework.Config;
import KAGO_framework.Core.Subsystems.SubsystemResolver;
import MyProject.Control.GameController;

public class GameManager {

    private GameController gameController;
    private GameScene activeGameScene;

    GameManager(){
        if (Config.INFO_MESSAGES) {
            System.out.println("  > GameManager: Create GameController and Start Game...");
            System.out.println("     > Execute startProgram method once.");
            System.out.println("     > Execute updateProgram method from now on repeatedly.");
            System.out.println("-------------------------------------------------------------------------------------------------\n");
            System.out.println("** Logs from now on relate to the user written program in \"MyProject\" **");
        }

        // Init Systems
        SubsystemResolver.Initialize();

        // Init Game
        GameScene scene = new GameScene();
        LoadScene(scene);

        gameController = new GameController(this, scene);
    }

    public void LoadScene(GameScene scene) {
        activeGameScene = scene;
    }
}