// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package RobotLib.Command;

/** A command that prints a string when initialized. */
public class PrintCommand extends InstantCommand {
    /**
     * Creates a new a PrintCommand.
     *
     * @param message the message to print
     */
    public PrintCommand(String message) {
        super(() -> System.out.println(message));
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
