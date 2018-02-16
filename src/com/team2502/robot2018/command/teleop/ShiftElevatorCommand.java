package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ShiftElevatorCommand extends InstantCommand
{
    public ShiftElevatorCommand()
    {
        requires(Robot.CLIMBER_SOLENOID);
    }

    @Override
    protected void execute()
    {
        Robot.CLIMBER_SOLENOID.toggleLock();
    }
}
