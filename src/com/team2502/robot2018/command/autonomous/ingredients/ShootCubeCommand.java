package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ShootCubeCommand extends TimedCommand
{

    private double speed;

    public ShootCubeCommand(double timeout)
    {
        super(timeout);
        this.speed = 0.5;
    }

    public ShootCubeCommand(double timeout,double speed)
    {
        super(timeout);
        this.speed = speed;
    }

    @Override
    protected void execute()
    {
        Robot.ACTIVE_INTAKE.runIntake(-speed);
    }

    @Override
    protected void end()
    {
        Robot.ACTIVE_INTAKE.stopAll();
    }
}
