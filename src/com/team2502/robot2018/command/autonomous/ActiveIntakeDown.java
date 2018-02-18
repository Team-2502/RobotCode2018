package com.team2502.robot2018.command.autonomous;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ActiveIntakeDown extends TimedCommand
{

    private final double speed;

    public ActiveIntakeDown(double timeout, double speed)
    {
        super(timeout);
        this.speed = speed;
    }

    @Override
    protected void initialize()
    {
        System.out.println("init active");
    }

    @Override
    protected void execute()
    {
        Robot.ACTIVE_INTAKE.rotateIntake(speed);
    }
}
