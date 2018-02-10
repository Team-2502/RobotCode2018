package com.team2502.robot2018.command.teleop;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * A lambda-based instant command.
 */
public class QuickCommand extends InstantCommand
{
    final Runnable toRun;

    public QuickCommand(final Runnable toRun)
    {
        this.toRun = toRun;
    }

    protected void execute()
    {
        toRun.run();
    }

}
