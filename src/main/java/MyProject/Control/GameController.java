package MyProject.Control;

import KAGO_framework.Core.Components.*;
import KAGO_framework.Core.GameManager;
import KAGO_framework.Core.GameObject;
import KAGO_framework.Core.Scene;
import KAGO_framework.Core.Subsystems.Component;

public class GameController {

    private final GameManager GAME_MANAGER;
    private final Scene DEFAULT_SCENE;

    public GameController(GameManager gameManager, Scene defaultScene){
        this.GAME_MANAGER = gameManager;
        this.DEFAULT_SCENE = defaultScene;
    }

    public void startProgram() {
        GameObject soundTestGameObject = new GameObject(
            new Component[] {
                    new SoundSource("src/main/resources/sound/vine-boom.mp3", 0.5f, true, true)
            }
        );

        DEFAULT_SCENE.AddGameObject(soundTestGameObject);
    }

    public void updateProgram(double dt){

    }
}
