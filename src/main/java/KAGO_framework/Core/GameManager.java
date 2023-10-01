package KAGO_framework.Core;

import KAGO_framework.Core.Debug.Debug;
import KAGO_framework.Core.Debug.LogType;
import KAGO_framework.Core.Subsystems.Component;
import KAGO_framework.Core.Subsystems.Graphics.Window;
import KAGO_framework.Core.Subsystems.SubsystemComponentDistributor;
import KAGO_framework.Core.Subsystems.SubsystemInitializer;
import MyProject.Control.GameController;
import org.lwjgl.glfw.GLFW;

public class GameManager implements Runnable {
    // References for future use
    private GameController gameController;

    long timer = System.currentTimeMillis();
    int frames;
   
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
        SceneManager.LoadScene(scene);
        Debug.Log("Created/Load default GameScene", LogType.LOG);

        // Create game controller
        gameController = new GameController(this, scene);
        Debug.Log("Created GameController", LogType.LOG);
    }

    private void loop() {
        long windowHandle = Window.GetWindowHandle();

        while (!GLFW.glfwWindowShouldClose(windowHandle)) {
            // Update Components
            GameObject[] gameObjects = SceneManager.GetAllActiveGameObjects();
            for (GameObject gameObject : gameObjects)
                for (Component component : gameObject.components)
                    SubsystemComponentDistributor.Distribute(component);

            // Render TODO: MALICIOUS JULIUS RENDERER

            // FPS
            frames++;
            if(System.currentTimeMillis() - timer > 1000) {
                System.out.printf("FPS: %s \n", frames);
                frames = 0;
                timer += 1000;
            }

            GLFW.glfwPollEvents();
        }

        // Terminate & stuff (when window is closed)
        Window.Deconstruct();
    }
}