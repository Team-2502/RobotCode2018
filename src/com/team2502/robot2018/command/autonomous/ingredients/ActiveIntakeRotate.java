package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;


public class ActiveIntakeRotate extends TimedCommand
{

    private final double speed;

    /**
     * <<<<<<< HEAD
     *
     * @param timeout
     * @param speed   =======
     *                Rotate the active intake
     * @param timeout How long to do it for (seconds)
     * @param speed   Speed of rotation motor in percent voltage
     *                >>>>>>> develop
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
