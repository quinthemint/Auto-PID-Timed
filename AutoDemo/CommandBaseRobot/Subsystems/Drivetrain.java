package CommandBaseRobot.Subsystems;

import RobotLib.*;
import RobotLib.Command.SubsystemBase;

public class Drivetrain extends SubsystemBase{
    private Spark leftMotor = new Spark(0);
    private Spark rightMotor = new Spark(1);

    private Encoder leftEncoder = new Encoder(0, 1, false);
    private Encoder rightEncoder = new Encoder(2, 3, true);
    final float kEncoderTick2Meter = 1f / 4096f * 0.128f * Util.PI;

    public Drivetrain() {
//
    }

    public void setMotors(float left, float right) {
        leftMotor.set(left);
        rightMotor.set(right);
    }

    public void stopMotors() {
        leftMotor.set(0);
        rightMotor.set(0);
    }

    public void arcadeDrive(float power, float turn) {
        leftMotor.set(power + turn);
        rightMotor.set(power - turn);
    }

    public float getEncoder() {
        return leftEncoder.get() * kEncoderTick2Meter;
    }

    public void resetEncoder() {
        leftEncoder.reset();
        rightEncoder.reset();
    }
}
