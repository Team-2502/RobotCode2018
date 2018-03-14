package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ShootCubeCommand extends TimedCommand
{

    private double speed;

    /**
     * Given a cube, shoot it
     * @param timeout How long to wait before stopping the intake
     */
    public ShootCubeCommand(double timeout)
    {
        super(timeout);
        this.speed = 0.5;
    }

    /**
     * Given a cube, shoot it
     * @param timeout How long to wait before stopping the intake
     * @param speed How fast to run the intake (percent voltage)
     *
     * @see com.team2502.robot2018.subsystem.ActiveIntakeSubsystem#runIntake(double)
     */
    public ShootCubeCommand(double timeout, double speed)
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
