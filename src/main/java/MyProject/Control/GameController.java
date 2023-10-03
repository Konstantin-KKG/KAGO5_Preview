package MyProject.Control;

import KAGO_framework.Core.Components.*;
import KAGO_framework.Core.GameManager;
import KAGO_framework.Core.GameObject;
import KAGO_framework.Core.Scene;

public class GameController {

    private GameManager gameManager;
    private Scene defaultScene;
    private Sound sound;

    public GameController(GameManager gameManager, Scene defaultScene){
        this.gameManager = gameManager;
        this.defaultScene = defaultScene;
    }

    public void startProgram() {
        GameObject gameObject = new GameObject();
        sound = new Sound("src/main/resources/sound/vine-boom.mp3", true);
        gameObject.components.add(sound);
        defaultScene.AddGameObject(gameObject);
    }

    public void updateProgram(double dt){

    }
}
