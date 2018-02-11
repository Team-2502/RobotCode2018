package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorCommand extends Command
{
    public ElevatorCommand()
    {
        requires(Robot.ELEVATOR);
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveElevator(1F);
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
