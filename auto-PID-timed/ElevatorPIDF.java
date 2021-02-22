import RobotLib.*;

public class ElevatorPIDF extends RobotBase {

    Joystick joystick;
    Camera camera;
    Spark elevatorMotor;
    Encoder elevatorEncoder;
    PIDController pidController;
    final float kP = 2f;
    final float kI = 2f;
    final float kD = 0.8f;
    final float iLimit = 0.15f;

    public static void main(String[] args) {
        new Game(new ElevatorPIDF());
    }

    @Override
    public void start() {
        joystick = new Joystick(0);
        elevatorMotor = new Spark(2);
        elevatorEncoder = new Encoder(4, 5, false);
        pidController = new PIDController(kP, kI, kD, iLimit);

        camera = new Camera();

        print("Robot program started!");
    }

    final float kEncoderTick2Meter = 1f / 4096f * 0.1f * Util.PI;

    @Override
    public void update() {

        // For characterization:
        // float power = joystick.getAxis(1) * 0.3f;

        // elevatorMotor.set(power);
        // System.out.println(power);

        // // determined that 14% power can counter gravity

        if (joystick.getButtonPressed(1))
            pidController.setSetpoint(1.3f);
        else if (joystick.getButtonPressed(2))
            pidController.setSetpoint(0);

        float sensorPosition = elevatorEncoder.get() * kEncoderTick2Meter;

        float speed = 0.14f + pidController.calculate(sensorPosition);

        elevatorMotor.set(speed);

        System.out.println(sensorPosition);

        if (joystick.getButtonPressed(0))
            camera.toggleCameraAngle();
    }

}
