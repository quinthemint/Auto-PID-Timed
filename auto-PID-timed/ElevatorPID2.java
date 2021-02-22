import RobotLib.*;

public class ElevatorPID2 extends RobotBase {

    Joystick joystick;
    Camera camera;
    Spark elevatorMotor;
    Encoder elevatorEncoder;
    PIDController pidController;
    final float kP = 2f;
    final float kI = 2f;
    final float kD = 0.8f;
    final float iLimit = 0.15f;
    int mode = 0; //true means PID, false is manual
    public static void main(String[] args) {
        new Game(new ElevatorPID2());
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

        if (joystick.getButtonPressed(1)) {
            pidController.setSetpoint(1.3f);
            mode = 1;
        } else if (joystick.getButtonPressed(2)) {
            pidController.setSetpoint(0f);
            mode = 1;
        } else if (joystick.getAxis(1) >= 0.99f) {
            mode = 0;
        } else if (joystick.getAxis(1) <= -0.99f) {
            mode = 0;
        }
        if (mode == 0){
            elevatorMotor.set(-joystick.getAxis(1));
        }
        else if (mode == 1) {
            float sensorPosition = elevatorEncoder.get() * kEncoderTick2Meter;
            elevatorMotor.set(pidController.calculate(sensorPosition));
        }  else if (mode == 3){

        }
        if (joystick.getButtonPressed(0))
            camera.toggleCameraAngle();
    }

}
