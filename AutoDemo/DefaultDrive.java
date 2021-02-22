import RobotLib.*;

/**
 * This is an example ElevatorBot robot code that mimics the default drive
 * feature in the simulator.
 */
public class DefaultDrive extends RobotBase {

    Joystick joystick;
    Spark leftDriveMotor, rightDriveMotor;
    Camera camera;
    Spark elevatorMotor;
    Spark leftIntakeMotor, rightIntakeMotor;
    Encoder leftEncoder, rightEncoder;

    public static void main(String[] args) {
        new Game(new DefaultDrive());
    }

    @Override
    public void start() {
        joystick = new Joystick(0);
        leftDriveMotor = new Spark(0);
        rightDriveMotor = new Spark(1);
        elevatorMotor = new Spark(2);
        leftIntakeMotor = new Spark(3);
        rightIntakeMotor = new Spark(4);
        leftEncoder = new Encoder(0, 1, true);
        rightEncoder = new Encoder(0, 1, false);

        camera = new Camera();

        print("Robot program started!");
    }

    @Override
    public void update() {
        float power = -joystick.getAxis(1) * 0.5f;
        float turn = joystick.getAxis(3);

        float leftPower = power + turn;
        float rightPower = power - turn;

        leftDriveMotor.set(leftPower);
        rightDriveMotor.set(-rightPower);

        if (joystick.getButtonPressed(0)) {
            camera.toggleCameraAngle();
        }

        if (joystick.getButtonDown(2)) {
            elevatorMotor.set(1f);
        } else {
            elevatorMotor.set(0f);
        }

        if (joystick.getButtonDown(3)) {
            leftIntakeMotor.set(1f);
            rightIntakeMotor.set(1f);
        } else {
            leftIntakeMotor.set(0f);
            rightIntakeMotor.set(0f);
        }
    }

}
