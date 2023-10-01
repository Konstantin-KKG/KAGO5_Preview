package KAGO_framework.Core;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Core.Subsystems.SubsystemComponentDistributor;
import KAGO_framework.Core.Subsystems.SubsystemInitializer;
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

    // References for future use
    private GameController gameController;
    private GameScene activeGameScene;
   
    public void run() {
        initSystems();
        initGame();

        Debug.Log("Running Main Loop", LogType.LOG);
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
        SubsystemInitializer.Initialize();
        SubsystemComponentDistributor.Initialize();
    }

    private void initGame() {
        GameScene scene = new GameScene();
        LoadScene(scene);
        Debug.Log("Created/Set default GameScene", LogType.LOG);

        gameController = new GameController(this, scene);
        Debug.Log("Created GameController", LogType.LOG);
    }

    public void LoadScene(GameScene scene) {
        activeGameScene = scene;
    }
}