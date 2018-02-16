package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;
import logger.Log;

public class ElevatorBaseCommand extends Command
{
    private double _speed;

    public ElevatorBaseCommand(double speed)
    {
        requires(Robot.ELEVATOR);
        _speed = speed;
    }

    // PUT THIS BACK IF TESTED LINES IN ELEVATORSUBSYSTEM DO NOT WORK ANY BETTER


    @Override
    protected void initialize()
    {
//        System.out.println("initialize of ElevatorBaseCommand");
//        Robot.CLIMBER_SOLENOID.unlockElevator();
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveElevator(_speed);
        Log.info("Setting elevator speed.");
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
