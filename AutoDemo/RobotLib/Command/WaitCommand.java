// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package RobotLib.Command;

import RobotLib.Timer;

/**
 * A command that does nothing but takes a specified amount of time to finish.
 * Useful for CommandGroups. Can also be subclassed to make a command with an
 * internal timer.
 */
public class WaitCommand extends CommandBase {
    protected Timer m_timer = new Timer();
    private final float m_duration;

    /**
     * Creates a new WaitCommand. This command will do nothing, and end after the
     * specified duration.
     *
     * @param seconds the time to wait, in seconds
     */
    public WaitCommand(float seconds) {
        m_duration = seconds;
    }

    @Override
    public void initialize() {
        m_timer.reset();
        m_timer.start();
    }

    @Override
    public void end(boolean interrupted) {
        m_timer.stop();
    }

    @Override
    public boolean isFinished() {
        return m_timer.hasElapsed(m_duration);
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
