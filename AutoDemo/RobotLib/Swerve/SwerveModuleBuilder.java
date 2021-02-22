package RobotLib.Swerve;

import RobotLib.*;

public class SwerveModuleBuilder {
    private float kAngleEncoderTick2Rad;
    private float kDriveEncoderTick2Dist;
    private float angleOffset;
    private Spark angleMotor;
    private Spark driveMotor;
    private Encoder angleEncoder;
    private Encoder driveEncoder;
    private PIDController pidController;

    public SwerveModuleBuilder() {

    }

    public SwerveModuleBuilder setAngleOffset(float angleOffset) {
        this.angleOffset = angleOffset;
        return this;
    }

    public SwerveModuleBuilder setAngleMotor(Spark angleMotor) {
        this.angleMotor = angleMotor;
        return this;
    }

    public SwerveModuleBuilder setDriveMotor(Spark driveMotor) {
        this.driveMotor = driveMotor;
        return this;
    }

    public SwerveModuleBuilder setAngleEncoder(Encoder angleEncoder) {
        this.angleEncoder = angleEncoder;
        return this;
    }

    public SwerveModuleBuilder setDriveEncoder(Encoder driveEncoder) {
        this.driveEncoder = driveEncoder;
        return this;
    }

    public SwerveModuleBuilder setAnglePIDController(PIDController pidController) {
        this.pidController = pidController;
        return this;
    }

    public SwerveModuleBuilder setAngleEncoderTick2Rad(float kAngleEncoderTick2Rad) {
        this.kAngleEncoderTick2Rad = kAngleEncoderTick2Rad;
        return this;
    }

    public SwerveModuleBuilder setDriveEncoderTick2Dist(float kDriveEncoderTick2Dist) {
        this.kDriveEncoderTick2Dist = kDriveEncoderTick2Dist;
        return this;
    }

    public SwerveModule build() {
        return new SwerveModule(angleOffset, angleMotor, driveMotor, angleEncoder, kAngleEncoderTick2Rad, driveEncoder,
                kDriveEncoderTick2Dist, pidController);
    }

}
