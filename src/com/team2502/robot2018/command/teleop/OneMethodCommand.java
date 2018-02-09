package com.team2502.robot2018.command.teleop;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class OneMethodCommand extends InstantCommand
{
    Runnable thing;
    public OneMethodCommand(Runnable thing)
    {
        this.thing = thing;
    }

    protected void execute()
    {
        thing.run();
    }
}
