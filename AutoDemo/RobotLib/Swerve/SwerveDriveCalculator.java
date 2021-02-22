package RobotLib.Swerve;

import RobotLib.Util;

public class SwerveDriveCalculator {

    private SwerveDriveKinematics kinematics;
    private float WHEELBASE;
    private float TRACKWIDTH;

    public SwerveDriveCalculator(SwerveDriveKinematics kinematics, float WHEELBASE, float TRACKWIDTH) {
        this.kinematics = kinematics;
        this.WHEELBASE = WHEELBASE;
        this.TRACKWIDTH = TRACKWIDTH;
    }

    public SwerveModuleState[] calculate(float forwardSpeed, float sideSpeed, float rotationSpeed, float robotAngle,
            boolean fieldOriented) {
        Translation2d translation = new Translation2d(forwardSpeed, sideSpeed);
        rotationSpeed *= 2.0f / Math.hypot(WHEELBASE, TRACKWIDTH);

        // print(rotationSpeed + " || " + translation); // correct

        ChassisSpeeds speeds;
        if (fieldOriented) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotationSpeed,
                    Rotation2d.fromDegrees(robotAngle * Util.toDeg));
        } else {
            speeds = new ChassisSpeeds(translation.getX(), translation.getY(), rotationSpeed);
        }

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);

        return states;
    }
}
