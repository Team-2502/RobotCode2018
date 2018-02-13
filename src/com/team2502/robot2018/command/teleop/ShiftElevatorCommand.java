package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ShiftElevatorCommand extends InstantCommand
{
    public ShiftElevatorCommand()
    {
        requires(Robot.CLIMBER);
    }

    protected void execute()
    {
        Robot.CLIMBER.lockElevator();
    }


}
