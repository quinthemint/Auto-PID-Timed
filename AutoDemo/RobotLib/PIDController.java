package RobotLib;

public class PIDController {
    private final float kInfinity = Float.POSITIVE_INFINITY;
    private float kP = 0f, kI = 0f, kD = 0f, kILimit = kInfinity, kIMaxVal = kInfinity;
    private boolean kClearIntegrationOnErrorSignChange = false;
    private float kPosTolerance = kInfinity, kVelTolerance = kInfinity;
    private boolean kContinuous = false;
    private float kInputRange = kInfinity;

    private float setpoint = 0f;

    private float dt = 0f;
    private float error = 0f;
    private float errorRate = 0f;
    private float errorSum = 0f;
    private float outputSpeed = 0f;
    private float lastTimestamp = 0f;
    private float lastError = 0f;
    private float lastSensorPosition = 0f;

    /**
     * Some systems have the input range wrapped around. Ex: an angle motor that
     * goes back to 0 degs after 360 degs. Because of this, the control on these
     * systems may contain shortcuts. Ex: to go from 350 degs to 10 degs, you don't
     * have to spin 340 degs; instead, realize that you can wrap around from the
     * other side and only travel 20 degs. This function scans for these kind of
     * shortcuts and returns new error values that describe the motion plan for the
     * shortcut.
     * 
     * @param error the simple motion plan to get to setpoint
     * @return new motion plan (the error) to the setpoint
     */
    private float recalculateMotionUsingContinuous(float error) {
        error %= kInputRange;
        if (Math.abs(error) > kInputRange / 2.0) {
            if (error > 0.0) {
                error -= kInputRange;
            } else {
                error += kInputRange;
            }
        }
        return error;
    }

    public float calculate(float sensorPosition) {

        error = setpoint - sensorPosition;
        if (kContinuous)
            error = recalculateMotionUsingContinuous(error);

        dt = Timer.getFPGATimestamp() - lastTimestamp;

        if (Math.abs(error) < kILimit) {
            errorSum += error * dt;
            errorSum = Util.clamp(errorSum, -kIMaxVal / kI, kIMaxVal / kI);
            if (kClearIntegrationOnErrorSignChange && !Util.sameSign(error, lastError))
                errorSum = 0;
        }

        errorRate = (lastSensorPosition - sensorPosition) / dt; // prevent derivative kick

        outputSpeed = kP * error + kI * errorSum + kD * errorRate;

        lastTimestamp = Timer.getFPGATimestamp();
        lastError = error;
        lastSensorPosition = sensorPosition;

        return outputSpeed;
    }

    public float getError() {
        return error;
    }

    public float getErrorRate() {
        return errorRate;
    }

    public float getErrorSum() {
        return errorSum;
    }

    public void resetIntegration() {
        errorSum = 0;
    }

    public boolean onTarget() {
        return Math.abs(error) < kPosTolerance && Math.abs(errorRate) < kVelTolerance;
    }

    public boolean iskClearIntegrationOnErrorSignChange() {
        return kClearIntegrationOnErrorSignChange;
    }

    public void setkClearIntegrationOnErrorSignChange(boolean clearIntegrationOnErrorSignChange) {
        this.kClearIntegrationOnErrorSignChange = clearIntegrationOnErrorSignChange;
    }

    public float getkIMaxVal() {
        return kIMaxVal;
    }

    public void setkIMaxVal(float kIMaxVal) {
        this.kIMaxVal = kIMaxVal;
    }

    public float getkILimit() {
        return kILimit;
    }

    public void setkILimit(float kILimit) {
        this.kILimit = kILimit;
    }

    public float getkP() {
        return kP;
    }

    public float getVelTolerance() {
        return kVelTolerance;
    }

    public void setVelTolerance(float velTolerance) {
        this.kVelTolerance = velTolerance;
    }

    public float getPosTolerance() {
        return kPosTolerance;
    }

    public void setPosTolerance(float posTolerance) {
        this.kPosTolerance = posTolerance;
    }

    public void setTolerance(float posTolerance, float velTolerance) {
        this.kPosTolerance = posTolerance;
        this.kVelTolerance = velTolerance;
    }

    public void setkP(float kP) {
        this.kP = kP;
    }

    public float getkI() {
        return kI;
    }

    public void setkI(float kI) {
        this.kI = kI;
    }

    public float getkD() {
        return kD;
    }

    public void setkD(float kD) {
        this.kD = kD;
    }

    public void setPID(float kP, float kI, float kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setPID(float kP, float kI, float kD, float iLimit) {
        setPID(kP, kI, kD);
        this.kILimit = iLimit;
    }

    public void setPID(float kP, float kI, float kD, float iLimit, float iMaxVal) {
        setPID(kP, kI, kD, iLimit);
        this.kIMaxVal = iMaxVal;
    }

    public float getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(float setpoint) {
        this.setpoint = setpoint;
    }

    public boolean isContinuous() {
        return kContinuous;
    }

    public void setContinuous(boolean kContinuous, float kInputRange) {
        this.kContinuous = kContinuous;
        this.kInputRange = kInputRange;

    }

    public float getInputRange() {
        return kInputRange;
    }

    public PIDController(float kP, float kI, float kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public PIDController(float kP, float kI, float kD, boolean continuous, float inputRange) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kContinuous = continuous;
        this.kInputRange = inputRange;
    }

    public PIDController(float kP, float kI, float kD, float kILimit) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kILimit = kILimit;
    }

    public PIDController(float kP, float kI, float kD, float kILimit, float kIMaxVal) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kILimit = kILimit;
        this.kIMaxVal = kIMaxVal;
    }

    public PIDController(float kP, float kI, float kD, float kILimit, float kIMaxVal, float kPosTolerance,
            float kVelTolerance) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kILimit = kILimit;
        this.kIMaxVal = kIMaxVal;
        this.kPosTolerance = kPosTolerance;
        this.kVelTolerance = kVelTolerance;
    }

}
