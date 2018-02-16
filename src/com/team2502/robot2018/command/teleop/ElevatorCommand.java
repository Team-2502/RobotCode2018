package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.ElevatorSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorCommand extends Command
{
    private double _speed;
    ElevatorSubsystem elevatorSubsystem;

    public ElevatorCommand(double speed)
    {
        requires(Robot.ELEVATOR);
        requires(Robot.CLIMBER_SOLENOID);
        _speed = speed;
    }

    // PUT THIS BACK IF TESTED LINES IN ELEVATORSUBSYSTEM DO NOT WORK ANY BETTER

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
//        Robot.CLIMBER_SOLENOID.lockElevator();
        Robot.ELEVATOR.stopElevator();
    }



}
