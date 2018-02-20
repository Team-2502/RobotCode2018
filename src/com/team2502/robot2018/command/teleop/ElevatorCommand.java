package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorCommand extends Command
{
    private double _speed;

    public ElevatorCommand(double speed)
    {
        requires(Robot.ELEVATOR);
        _speed = speed;
    }


    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveElevator(_speed);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.ELEVATOR.stopElevator();
    }
}
