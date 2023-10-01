package KAGO_framework.Core;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import KAGO_framework.Core.Subsystems.SubsystemComponentDistributor;
import KAGO_framework.Core.Subsystems.SubsystemInitializer;
import MyProject.Control.GameController;
import org.lwjgl.glfw.GLFW;

public class GameManager implements Runnable {
    // Main loop settings
    long initialTime = System.nanoTime();
    long timer = System.currentTimeMillis();
    final double TIME_U = 1000000000.d / 60.d;
    final double TIME_F = 1000000000.d / 144.d;
    double deltaU = 0, deltaF = 0;
    int ticks = 0, frames = 0;

    // References for future use
    private GameController gameController;
   
    public void run() {
        initSystems();
        initGame();

        loop();
    }

    private void initSystems() {
        SubsystemInitializer.Initialize();
        SubsystemComponentDistributor.Initialize();
    }

    private void initGame() {
        // Create & Load scene
        Scene scene = new Scene();
        Debug.Log("Created/Set default GameScene", LogType.LOG);

        // Create game controller
        gameController = new GameController(this, scene);
        Debug.Log("Created GameController", LogType.LOG);
    }

    private void loop() {
        long windowHandle = Window.GetWindowHandle();

        while (!GLFW.glfwWindowShouldClose(windowHandle)) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / TIME_U;
            deltaF += (currentTime - initialTime) / TIME_F;
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

            GLFW.glfwPollEvents();
        }

        // Terminate & stuff (when window is closed)
        Window.Deconstruct();
    }
}