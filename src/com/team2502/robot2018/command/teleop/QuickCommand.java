package com.team2502.robot2018.command.teleop;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class QuickCommand extends InstantCommand
{
    final Runnable action;

    public QuickCommand(Subsystem subsystem, Runnable action)
    {
        requires(subsystem);
        this.action = action;
    }


    protected void execute()
    {
        action.run();
    }

}
