package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import edu.wpi.first.wpilibj.command.Command;
import logger.Log;

public class ElevatorCommand extends Command
{
    private double _speed;

    public ElevatorCommand(double speed)
    {
        requires(Robot.ELEVATOR);
        _speed = speed;
    }

    // PUT THIS BACK IF TESTED LINES IN ELEVATORSUBSYSTEM DO NOT WORK ANY BETTER


    @Override
    protected void initialize()
    {
//        System.out.println("initialize of ElevatorCommand");
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
        return !OI.JOYSTICK_FUNCTION.getRawButton(RobotMap.Joystick.Button.RAISE_ELEVATOR);
    }

    @Override
    protected void end()
    {
//        Robot.CLIMBER_SOLENOID.lockElevator();
        Robot.ELEVATOR.stopElevator();
    }



}
