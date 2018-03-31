package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Run the climber to climb
 */
public class ClimberCommand extends Command
{
    private double _speed;

    /**
     * @param speed Percent voltage to run the climber at
     */
    public ClimberCommand(double speed)
    {
        requires(Robot.ELEVATOR);
        this._speed = speed;
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveClimber(_speed);
        System.out.println("Running Climber command");
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.ELEVATOR.stopClimber();
    }
}
