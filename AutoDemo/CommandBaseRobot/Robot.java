package CommandBaseRobot;
import RobotLib.*;
import RobotLib.Command.CommandScheduler;

public class Robot extends RobotBase {

    public static void main(String[] args) {
        new Game(new Robot());
    }

    @Override
    public void start() {
        print("Program Started");
    }

    @Override
    public void update() {
        CommandScheduler.getInstance().run();
    }

}
