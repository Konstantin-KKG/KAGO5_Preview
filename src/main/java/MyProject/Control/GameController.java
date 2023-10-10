package MyProject.Control;

import KAGO_framework.Core.Components.*;
import KAGO_framework.Core.SceneGraph.GameObject;
import KAGO_framework.Core.SceneGraph.Scene;
import KAGO_framework.Core.Subsystems.Component;

public class GameController {
    private final Scene DEFAULT_SCENE;

    public GameController(Scene defaultScene){
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
