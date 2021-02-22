import RobotLib.*;

public class DrivePID extends RobotBase {

    Joystick joystick;
    Camera camera;
    Spark leftDriveMotor, rightDriveMotor;
    Encoder leftEncoder, rightEncoder;

    public static void main(String[] args) {
        new Game(new DrivePID());
    }

    @Override
    public void start() {
        joystick = new Joystick(0);
        leftDriveMotor = new Spark(0);
        rightDriveMotor = new Spark(1);
        leftEncoder = new Encoder(0, 1, false);
        rightEncoder = new Encoder(2, 3, true);

        camera = new Camera();

        print("Robot program started!");
    }

    final float kEncoderTick2Meter = 1f / 4096f * 0.128f * Util.PI;

    final float kP = 0.7f;
    final float kI = 0.5f;
    final float kD = 0.9f;
    final float iLimit = 0.1f;

    float setpoint = 0f;
    float errorSum = 0f;
    float lastTimestamp = 0f;
    float lastError = 0f;

    @Override
    public void update() {
        if (joystick.getButtonPressed(1)) {
            setpoint = 2;
        } else if (joystick.getButtonPressed(2)) {
            setpoint = 0;
        }

        float sensorPosition = getEncoderAverage() * kEncoderTick2Meter;
        print(sensorPosition);

        float error = setpoint - sensorPosition;
        float dt = Timer.getFPGATimestamp() - lastTimestamp;

        if (Math.abs(error) < iLimit) {
            errorSum += error * dt;
        }

        float errorRate = (error - lastError) / dt;

        float outputSpeed = kP * error + kI * errorSum + kD * errorRate;

        outputSpeed = Util.clamp(outputSpeed, -0.5f, 0.5f);

        leftDriveMotor.set(outputSpeed);
        rightDriveMotor.set(-outputSpeed);

        lastTimestamp = Timer.getFPGATimestamp();
        lastError = error;

        System.out.println(sensorPosition);

        if (joystick.getButtonPressed(0))
            camera.toggleCameraAngle();
    }

    private float getEncoderAverage() {
        // System.out.println(
        // leftEncoder.get() + " " + rightEncoder.get() + " " + (leftEncoder.get() +
        // rightEncoder.get()) / 2f);
        return (leftEncoder.get() + rightEncoder.get()) / 2f;
    }

}