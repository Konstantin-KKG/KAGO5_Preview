package KAGO_framework.Core;

import KAGO_framework.Config;
import KAGO_framework.Core.Subsystems.SubsystemResolver;
import MyProject.Control.GameController;

public class GameManager implements Runnable {

    // Main loop settings
    boolean running = true;

    long initialTime = System.nanoTime();
    long timer = System.currentTimeMillis();
    final double timeU = 1000000000.d / 60.d;
    final double timeF = 1000000000.d / 144.d;
    double deltaU = 0, deltaF = 0;
    int ticks = 0, frames = 0;

    //
    private GameController gameController;
    private GameScene activeGameScene;

    public void run() {
        if (Config.INFO_MESSAGES) {
            System.out.println("  > GameManager: Create GameController and Start Game...");
            System.out.println("     > Execute startProgram method once.");
            System.out.println("     > Execute updateProgram method from now on repeatedly.");
            System.out.println("-------------------------------------------------------------------------------------------------\n");
            System.out.println("** Logs from now on relate to the user written program in \"MyProject\" **");
        }

        initSystems();
        initGame();

        // Main loop:
        while (running) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                // getInput();
                // update();
                ticks++;
                deltaU--;
            }

            if (deltaF >= 1) {
                // render();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                System.out.printf("UPS: %s, FPS: %s%n", ticks, frames);
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }

    private void initSystems() {
        SubsystemResolver.Initialize();
    }

    private void initGame() {
        GameScene scene = new GameScene();
        LoadScene(scene);

        gameController = new GameController(this, scene);
    }

    public void LoadScene(GameScene scene) {
        activeGameScene = scene;
    }
}