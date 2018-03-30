package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Enable the climber in order to stop the elevator from falling down under its own weight
 */
public class LockElevatorCommand extends InstantCommand
{
    public LockElevatorCommand()
    {
        requires(Robot.CLIMBER_SOLENOID);
    }

    @Override
    protected void execute()
    {
        Robot.CLIMBER_SOLENOID.lockElevator();
    }
}
