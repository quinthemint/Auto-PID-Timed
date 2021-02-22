// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package RobotLib.Command;

import RobotLib.PIDController;

/**
 * A subsystem that uses a {@link PIDController} to control an output. The
 * controller is run synchronously from the subsystem's periodic() method.
 */
public abstract class PIDSubsystem extends SubsystemBase {
    protected final PIDController m_controller;
    protected boolean m_enabled;

    private float m_setpoint;

    /**
     * Creates a new PIDSubsystem.
     *
     * @param controller      the PIDController to use
     * @param initialPosition the initial setpoint of the subsystem
     */
    public PIDSubsystem(PIDController controller, float initialPosition) {
        setSetpoint(initialPosition);
        m_controller = ErrorMessages.requireNonNullParam(controller, "controller", "PIDSubsystem");
    }

    /**
     * Creates a new PIDSubsystem. Initial setpoint is zero.
     *
     * @param controller the PIDController to use
     */
    public PIDSubsystem(PIDController controller) {
        this(controller, 0);
    }

    @Override
    public void periodic() {
        if (m_enabled) {
            m_controller.setSetpoint((float) m_setpoint);
            useOutput(m_controller.calculate((float) getMeasurement()), m_setpoint);
        }
    }

    public PIDController getController() {
        return m_controller;
    }

    /**
     * Sets the setpoint for the subsystem.
     *
     * @param setpoint the setpoint for the subsystem
     */
    public void setSetpoint(float setpoint) {
        m_setpoint = setpoint;
    }

    /**
     * Returns the current setpoint of the subsystem.
     *
     * @return The current setpoint
     */
    public float getSetpoint() {
        return m_setpoint;
    }

    /**
     * Uses the output from the PIDController.
     *
     * @param output   the output of the PIDController
     * @param setpoint the setpoint of the PIDController (for feedforward)
     */
    protected abstract void useOutput(float output, float setpoint);

    /**
     * Returns the measurement of the process variable used by the PIDController.
     *
     * @return the measurement of the process variable
     */
    protected abstract float getMeasurement();

    /** Enables the PID control. Resets the controller. */
    public void enable() {
        m_enabled = true;
    }

    /** Disables the PID control. Sets output to zero. */
    public void disable() {
        m_enabled = false;
        useOutput(0, 0);
    }

    /**
     * Returns whether the controller is enabled.
     *
     * @return Whether the controller is enabled.
     */
    public boolean isEnabled() {
        return m_enabled;
    }
}
