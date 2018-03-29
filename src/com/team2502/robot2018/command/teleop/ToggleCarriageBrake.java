package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ToggleCarriageBrake extends InstantCommand
{
    public ToggleCarriageBrake()
    {
        requires(Robot.CARRIAGE_BRAKE_SOLENOID);
    }

    @Override
    protected void execute()
    {
        Robot.CARRIAGE_BRAKE_SOLENOID.toggle();
    }
}
