import RobotLib.*;

public class ElevatorPID1 extends RobotBase {

    Joystick joystick;
    Camera camera;
    Spark elevatorMotor;
    Encoder elevatorEncoder;

    public static void main(String[] args) {
        new Game(new ElevatorPID1());
    }

    @Override
    public void start() {
        joystick = new Joystick(0);
        elevatorMotor = new Spark(2);
        elevatorEncoder = new Encoder(4, 5, false);

        camera = new Camera();

        print("Robot program started!");
    }

    final float kEncoderTick2Meter = 1f / 4096f * 0.1f * Util.PI;

    // less aggressive:
    // final float kP = 1.5f;
    // final float kI = 1f;
    // final float kD = 0.7f;
    // final float iLimit = 0.15f;

    final float kP = 0.9f;
    final float kI = 0.8f;
    final float kD = 0.6f;
    final float iLimit = 0.15f;

    float setpoint = 0f;
    float errorSum = 0f;
    float lastTimestamp = 0f;
    float lastError = 0f;

    @Override
    public void update() {
        if (joystick.getButtonPressed(1)) {
            setpoint = 1.3f;
        } else if (joystick.getButtonPressed(2)) {
            setpoint = 0;
        } else if (joystick.getAxis(1) >= 0.99f) {
            elevatorMotor.set(-0.5f);
            return;
        } else if (joystick.getAxis(1) <= -0.99f) {
            elevatorMotor.set(0.5f);
            return;
        }
        float sensorPosition = elevatorEncoder.get() * kEncoderTick2Meter;

        float error = setpoint - sensorPosition;
        float dt = Timer.getFPGATimestamp() - lastTimestamp;

        if (Math.abs(error) < iLimit) {
            errorSum += error * dt;
        }

        float errorRate = (error - lastError) / dt;

        float outputSpeed = kP * error + kI * errorSum + kD * errorRate;

        elevatorMotor.set(outputSpeed);

        lastTimestamp = Timer.getFPGATimestamp();
        lastError = error;

        System.out.println(sensorPosition  + "        " + joystick.getAxis(1));

        if (joystick.getButtonPressed(0))
            camera.toggleCameraAngle();
    }

}
