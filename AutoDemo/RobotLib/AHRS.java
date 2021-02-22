package RobotLib;

public class AHRS {

    private float resetVal = 0f;

    public AHRS() {

    }

    public float getAngle() {
        return Data.robotData[RobotData.robotAngle.ordinal()] - resetVal;
    }

    public void reset() {
        resetVal = getAngle();
    }
}
