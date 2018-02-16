package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class UnlockElevatorCommand extends InstantCommand
{
    public UnlockElevatorCommand()
    {
        requires(Robot.CLIMBER_SOLENOID);
    }

    @Override
    protected void execute()
    {
        Robot.CLIMBER_SOLENOID.unlockElevator();
    }
}
