package RobotLib.Swerve;

import RobotLib.*;

public class SwerveModule {

    private Spark steeringMotor;
    private Encoder angleEncoder;
    private Spark driveMotor;
    private Encoder driveEncoder;

    private float kAngleOffset;
    private float kAngleEncoderTick2Rad;
    private float kDriveEncoderTick2Dist;

    private PIDController angleController;

    private float prevDriveDistance = 0f;
    private float prevTimestamp = 0f;

    private float dt = 0.0f;
    private float angle = 0.0f;
    private float position = 0.0f;
    private float velocity = 0.0f;
    private float robotAngle = 0.0f;
    private Vector2 globalPosition = Vector2.ZERO;

    private float targetSpeed = 0.0f;
    private float targetAngle = 0.0f;

    /**
     * @param modulePosition The module's offset from the center of the robot's
     *                       center of rotation
     * @param angleOffset    An angle in radians that is used to offset the angle
     *                       encoder
     * @param angleMotor     The motor that controls the module's angle
     * @param driveMotor     The motor that drives the module's wheel
     * @param angleEncoder   The analog input for the angle encoder
     */
    public SwerveModule(float angleOffset, Spark angleMotor, Spark driveMotor, Encoder angleEncoder,
            float kAngleEncoderTick2Rad, Encoder driveEncoder, float kDriveEncoderTick2Dist,
            PIDController pidController) {
        this.kAngleEncoderTick2Rad = kAngleEncoderTick2Rad;
        this.kDriveEncoderTick2Dist = kDriveEncoderTick2Dist;
        this.kAngleOffset = angleOffset;
        this.steeringMotor = angleMotor;
        this.angleEncoder = angleEncoder;
        this.driveMotor = driveMotor;
        this.driveEncoder = driveEncoder;
        this.angleController = pidController;

        angleController.setContinuous(true, 2 * Util.PI);

    }

    public float getPosition() {
        return position;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getAngle() {
        return angle;
    }

    public float getRobotAngle() {
        return robotAngle;
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(velocity, new Rotation2d(angle));
    }

    public Vector2 getGlobalPosition() {
        return globalPosition;
    }

    public void resetGlobalPosition(Vector2 position) {
        globalPosition = position;
    }

    public void update(float targetSpeed, float targetAngle, float robotAngle) {

        updateDt();
        updateSensors(robotAngle);

        updateTargetOutputs(targetSpeed, targetAngle);
        optimizeMotion();

        updateMotors();

        updateGlobalPosition();

        updateLastVariables();
    }

    private void updateDt() {
        dt = Timer.getFPGATimestamp() - prevTimestamp;
    }

    private void updateMotors() {
        driveMotor.set(targetSpeed);
        angleController.setSetpoint(targetAngle);
        steeringMotor.set(angleController.calculate(angle));
    }

    private void updateSensors(float robotAngle) {
        this.robotAngle = robotAngle;

        // update angle
        angle = angleEncoder.get() * kAngleEncoderTick2Rad + kAngleOffset;
        angle %= 2.0 * Math.PI;
        if (angle < 0.0) {
            angle += 2.0 * Math.PI;
        }

        // update position
        position = driveEncoder.get() * kDriveEncoderTick2Dist;

        // update velocity
        velocity = (position - prevDriveDistance) / (dt);
    }

    private void updateGlobalPosition() {

        float deltaDistance = position - prevDriveDistance;
        float currentAngle = angle + robotAngle;

        Vector2 deltaPosition = Vector2.fromAngle(Rotation2.fromRadians(currentAngle)).scale(deltaDistance);

        globalPosition = globalPosition.add(deltaPosition);
    }

    private void updateTargetOutputs(float newTargetSpeed, float newTargetAngle) {

        targetSpeed = newTargetSpeed;
        if (Math.abs(targetSpeed) > 0.01) {
            targetAngle = newTargetAngle;
        }

        if (targetSpeed < 0.0) {
            targetSpeed *= -1.0;

            targetAngle += Math.PI;
        }

        targetAngle %= 2.0 * Math.PI;
        if (targetAngle < 0.0) {
            targetAngle += 2.0 * Math.PI;
        }

        // System.out.print(Util.round(targetSpeed) + "," + Util.round(targetAngle) +
        // "|");
    }

    private void optimizeMotion() {

        // Change the target angle so the delta is in the range [-pi, pi) instead of [0,
        // 2pi)
        float delta = targetAngle - angle;
        if (delta >= Math.PI) {
            targetAngle -= 2.0 * Math.PI;
        } else if (delta < -Math.PI) {
            targetAngle += 2.0 * Math.PI;
        }

        // Deltas that are greater than 90 deg or less than -90 deg can be inverted so
        // the total movement of the module
        // is less than 90 deg by inverting the wheel direction
        delta = targetAngle - angle;
        if (delta > Math.PI / 2.0 || delta < -Math.PI / 2.0) {
            // Only need to add pi here because the target angle will be put back into the
            // range [0, 2pi)
            targetAngle += Math.PI;

            targetSpeed *= -1.0;
        }

        // Put target angle back into the range [0, 2pi)
        targetAngle %= 2.0 * Math.PI;
        if (targetAngle < 0.0) {
            targetAngle += 2.0 * Math.PI;
        }

    }

    private void updateLastVariables() {
        prevDriveDistance = position;
        prevTimestamp = Timer.getFPGATimestamp();
    }

    public static SwerveModuleBuilder create() {
        return new SwerveModuleBuilder();
    }
}
