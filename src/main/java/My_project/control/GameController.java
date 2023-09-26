package My_project.control;

import KAGO_framework.Core.GameStateManager;
import My_project.model.Ball;
import My_project.view.GameInput;

public class GameController {

    private GameStateManager gameStateManager;

    public GameController(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
    }

    public void startProgram() {
        Ball ball1 = new Ball(150,150);
        GameInput gameInput1 = new GameInput(this);

        gameStateManager.draw(ball1);
        gameStateManager.register(gameInput1);
    }

    public void updateProgram(double dt){

    }
}
