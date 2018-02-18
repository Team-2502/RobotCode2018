package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ShootCubeCommand extends TimedCommand
{
    public ShootCubeCommand(double timeout)
    {
        super(timeout);
    }

    @Override
    protected void execute()
    {
        Robot.ACTIVE_INTAKE.runIntake(-0.5);
    }

    @Override
    protected void end()
    {
        Robot.ACTIVE_INTAKE.stopAll();
    }
}
