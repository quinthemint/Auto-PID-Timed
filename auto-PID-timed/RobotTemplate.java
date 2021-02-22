import RobotLib.*;

public class RobotTemplate extends RobotBase {

    final float kPDrive = 0.7f;
    final float kIDrive = 0.1f;
    final float kDDrive = 0.9f;
    final float iLimitDrive = 0.1f;

    final float kPElevator = 0.7f;
    final float kIElevator = 0.5f;
    final float kDElevator = 0.9f;
    final float iLimitElevator = 0.1f;

    PIDController elevatorPidController = new PIDController(kPElevator, kIElevator, kDElevator, iLimitElevator);
    PIDController drivePidController = new PIDController(kPDrive, kIDrive, kDDrive, iLimitDrive);
    
    Joystick joystick = new Joystick(0);
    Camera camera = new Camera();
    Spark leftDriveMotor = new Spark(0);
    Spark rightDriveMotor = new Spark(1);
    Spark elevatorMotor = new Spark(2);
    Spark leftIntakeMotor = new Spark(3);
    Spark rightIntakeMotor = new Spark(4);
    Encoder leftEncoder = new Encoder(0, 1, false);
    Encoder rightEncoder = new Encoder(2, 3, true);
    Encoder elevatorEncoder = new Encoder(4, 5, false);

    final float kDriveTick2Meter = 1f / 4096f * 0.128f * Util.PI;
    final float kElevatorTick2Meter = 1f / 4096f * 0.1f * Util.PI;

    float startTime = 0f;

    public static void main(String[] args) {
        new Game(new RobotTemplate());
    }

    @Override
    public void start() {
        print("program started");
        startTime = Timer.getFPGATimestamp();
        leftEncoder.reset();
        rightEncoder.reset();
        elevatorEncoder.reset();
    }

    @Override
    public void update() {

        float driveEncoderVal = getEncoderAverage() * kDriveTick2Meter;
        float driveOutput = drivePidController.calculate(driveEncoderVal);
        float elevatorEncoderVal = elevatorEncoder.get() * kElevatorTick2Meter;
        float elevatorOutput = elevatorPidController.calculate(elevatorEncoderVal);
       
        if (Timer.getFPGATimestamp() - startTime >= 2) {
            leftIntakeMotor.set(1);
            rightIntakeMotor.set(1);
        }

        if (Timer.getFPGATimestamp() - startTime >= 3) {
            elevatorPidController.setSetpoint(2f);
            elevatorMotor.set(elevatorOutput);
        }

        if (Timer.getFPGATimestamp() - startTime <= 5) {
            drivePidController.setSetpoint(8f);
            leftDriveMotor.set(driveOutput);
            rightDriveMotor.set(-driveOutput);
        } 

        if (Timer.getFPGATimestamp() - startTime >= 6) {
            leftIntakeMotor.set(-1);
            rightIntakeMotor.set(-1);
        }

        if (Timer.getFPGATimestamp() - startTime >= 7) {
            drivePidController.setSetpoint(-1f);
            leftDriveMotor.set(driveOutput);
            rightDriveMotor.set(-driveOutput);
        }

        if (Timer.getFPGATimestamp() - startTime >= 8) {
            elevatorPidController.setSetpoint(0f);
            elevatorMotor.set(elevatorOutput);
        }
        
    }
    
    private float getEncoderAverage() {
        return (leftEncoder.get() + rightEncoder.get()) / 2f;
    }

}
