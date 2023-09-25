package My_project.control;

import KAGO_framework.Private.Core.ViewController;
import My_project.model.Ball;

public class ProgramController {

    private ViewController viewController;

    public ProgramController(ViewController viewController){
        this.viewController = viewController;
    }

    public void startProgram() {
        Ball ball1 = new Ball(150,150);
        viewController.draw(ball1);

    }

    public void updateProgram(double dt){

    }
}
