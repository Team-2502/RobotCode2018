package com.team2502.robot2018.command.teleop;

import com.team2502.ctannotationprocessor.Undefined;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

@Deprecated
public class QuickCommand extends InstantCommand
{
    @Undefined
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
