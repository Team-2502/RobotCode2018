package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Manually toggle the lock on the climber
 */
public class ToggleCarriageBrake extends InstantCommand
{
    public ToggleCarriageBrake()
    {
        requires(Robot.CLIMBER_CARRIAGE_BRAKE);
    }

    @Override
    protected void execute()
    {
        Robot.CLIMBER_CARRIAGE_BRAKE.toggle();
    }
}
