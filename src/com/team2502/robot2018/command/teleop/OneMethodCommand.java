package com.team2502.robot2018.command.teleop;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class OneMethodCommand extends InstantCommand
{
    Runnable thing;
    Runnable whenDone = () -> {};
    public OneMethodCommand(Runnable thing)
    {
        this.thing = thing;
    }
    public OneMethodCommand(Runnable thing, Runnable whenDone)
    {
        this(thing);
        this.whenDone = whenDone;
    }

    protected void execute()
    {
        thing.run();
    }

    protected void end()
    {
        whenDone.run();
    }

    protected void interrupted()
    {
        end();
    }

}
