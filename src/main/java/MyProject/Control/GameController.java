package MyProject.Control;

import KAGO_framework.Core.Components.Sound;
import KAGO_framework.Core.GameManager;
import KAGO_framework.Core.GameObject;
import KAGO_framework.Core.Scene;

public class GameController {

    private GameManager gameManager;
    private Scene defaultScene;

    public GameController(GameManager gameManager, Scene defaultScene){
        this.gameManager = gameManager;
        this.defaultScene = defaultScene;
    }

    public void startProgram() {
        GameObject go = new GameObject();
        Sound s = new Sound("src/main/resources/sound/DeterminedDestination.mp3");
        go.components.add(s);
        s.Start();
    }

    public void updateProgram(double dt){

    }
}
