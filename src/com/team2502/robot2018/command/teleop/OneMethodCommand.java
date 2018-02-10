package com.team2502.robot2018.command.teleop;

import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class OneMethodCommand extends InstantCommand
{
    Runnable thing;

    public OneMethodCommand(Subsystem subsystem, Runnable thing)
    {
        requires(subsystem);
        this.thing = thing;
    }


    protected void execute()
    {
        thing.run();
    }


    protected void interrupted()
    {
        end();
    }

}
