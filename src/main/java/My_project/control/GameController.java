package My_project.control;

import KAGO_framework.Private.Core.GameLoopManager;
import My_project.model.Ball;
import My_project.view.GameInput;

public class GameController {

    private GameLoopManager gameLoopManager;

    public GameController(GameLoopManager gameLoopManager){
        this.gameLoopManager = gameLoopManager;
    }

    public void startProgram() {
        Ball ball1 = new Ball(150,150);
        GameInput gameInput1 = new GameInput(this);

        gameLoopManager.draw(ball1);
        gameLoopManager.register(gameInput1);
    }

    public void updateProgram(double dt){

    }
}
