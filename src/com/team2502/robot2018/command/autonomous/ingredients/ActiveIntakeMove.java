package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ActiveIntakeMove extends TimedCommand
{

    private final double speed;

    /**
     * Positi
     * @param timeout
     * @param speed
     */
    public ActiveIntakeMove(double timeout, double speed)
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
