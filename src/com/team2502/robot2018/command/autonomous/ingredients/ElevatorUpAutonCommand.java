package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ElevatorUpAutonCommand extends TimedCommand
{
    public ElevatorUpAutonCommand(double timeout)
    {
        super(timeout);
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveElevator(1);
    }

    @Override
    protected void end()
    {
        Robot.ELEVATOR.stopElevator();
    }
}
