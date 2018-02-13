package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class GrabCommand extends InstantCommand
{
    public GrabCommand()
    {
        requires(Robot.ACTIVE_INTAKE);
    }

    protected void execute()
    {
        Robot.GRABBER.toggleIntake();
    }
}
