package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class GrabCommand extends InstantCommand
{
    public GrabCommand()
    { requires(Robot.ACTIVE_INTAKE_SOLENOID); }

    @Override
    protected void execute()
    {
        Robot.ACTIVE_INTAKE_SOLENOID.toggleIntake();
    }
}
