package RobotLib;

public class Timer {
    /**
     * Return the system clock time in seconds. Return the time from the FPGA
     * hardware clock in seconds since the FPGA started.
     *
     * @return Robot running time in seconds.
     */
    public static float getFPGATimestamp() {
        return Data.robotData[RobotData.time.ordinal()];
    }

    /**
     * Pause the thread for a specified time. Pause the execution of the thread for
     * a specified period of time given in seconds. Motors will continue to run at
     * their last assigned values, and sensors will continue to update. Only the
     * task containing the wait will pause until the wait time is expired.
     *
     * @param seconds Length of time to pause
     */
    public static void delay(final float seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private float m_startTime;
    private float m_accumulatedTime;
    private boolean m_running;

    @SuppressWarnings("MissingJavadocMethod")
    public Timer() {
        reset();
    }

    public float getMillis() {
        return getFPGATimestamp() / 1000f;
    }

    /**
     * Get the current time from the timer. If the clock is running it is derived
     * from the current system clock the start time stored in the timer class. If
     * the clock is not running, then return the time when it was last stopped.
     *
     * @return Current time value for this timer in seconds
     */
    public float get() {
        if (m_running) {
            return m_accumulatedTime + (getFPGATimestamp() - m_startTime);
        } else {
            return m_accumulatedTime;
        }
    }

    /**
     * Reset the timer by setting the time to 0. Make the timer startTime the
     * current time so new requests will be relative now
     */
    public void reset() {
        m_accumulatedTime = 0;
        m_startTime = getFPGATimestamp();
    }

    /**
     * Start the timer running. Just set the running flag to true indicating that
     * all time requests should be relative to the system clock. Note that this
     * method is a no-op if the timer is already running.
     */
    public void start() {
        if (!m_running) {
            m_startTime = getFPGATimestamp();
            m_running = true;
        }
    }

    /**
     * Stop the timer. This computes the time as of now and clears the running flag,
     * causing all subsequent time requests to be read from the accumulated time
     * rather than looking at the system clock.
     */
    public void stop() {
        m_accumulatedTime = get();
        m_running = false;
    }

    /**
     * Check if the period specified has passed.
     *
     * @param seconds The period to check.
     * @return Whether the period has passed.
     */
    public boolean hasElapsed(float seconds) {
        return get() > seconds;
    }

    /**
     * Check if the period specified has passed and if it has, advance the start
     * time by that period. This is useful to decide if it's time to do periodic
     * work without drifting later by the time it took to get around to checking.
     *
     * @param seconds The period to check.
     * @return Whether the period has passed.
     */
    public boolean advanceIfElapsed(float seconds) {
        if (get() > seconds) {
            // Advance the start time by the period.
            // Don't set it to the current time... we want to avoid drift.
            m_startTime += seconds;
            return true;
        } else {
            return false;
        }
    }
}
