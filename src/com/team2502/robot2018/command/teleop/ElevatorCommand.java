package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.ElevatorSubsystem;
import edu.wpi.first.wpilibj.command.Command;

import javax.management.relation.RoleNotFoundException;

public class ElevatorCommand extends Command
{
    public double moveElevator = 0;
    ElevatorSubsystem elevatorSubsystem;

    public ElevatorCommand()
    {
        this(1);
    }

    public ElevatorCommand(double x)
    {
        requires(Robot.ELEVATOR);
        moveElevator = x;
        elevatorSubsystem = new ElevatorSubsystem();
    }


    @Override
    protected void execute()
    {
       elevatorSubsystem.moveElevator(moveElevator);
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
