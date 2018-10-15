package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;


public class ActiveIntakeRotate extends TimedCommand
{

    private final double speed;

    /**
     * Rotate the active for a certain period of time
     * @param timeout How long to rotate the active for
     * @param speed Speed in percent voltage. Positive is down, negative is up (as of champs)
     */
    public ActiveIntakeRotate(double timeout, double speed)
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
