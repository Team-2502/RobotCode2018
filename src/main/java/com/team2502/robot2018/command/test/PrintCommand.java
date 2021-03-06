package com.team2502.robot2018.command.test;

import edu.wpi.first.wpilibj.command.InstantCommand;
import logger.ShuffleboardLog;

/**
 * Prints a message on Shuffleboard (usually used during a systems test)
 * <p>
 * See the systems test tab to view it.
 */
public class PrintCommand extends InstantCommand
{

    private final String message;

    public PrintCommand(String message)
    {
        this.message = message;
    }

    @Override
    protected void initialize()
    {
        ShuffleboardLog.log(message);
    }
}
