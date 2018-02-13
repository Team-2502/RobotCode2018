package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ClimberCommand extends Command
{
    double x = 0;

    public ClimberCommand(double x)
    {
        requires(Robot.ELEVATOR);
        this.x = x;
    }

    protected void execute()
    {
        Robot.ELEVATOR.moveClimber(x);
        System.out.println("Running Climber command");
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
       Robot.ELEVATOR.stopClimber();
    }
}
