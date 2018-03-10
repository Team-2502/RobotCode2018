package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class DeadreckoningDrive extends TimedCommand
{

    private final float speedFPS;

    //TODO: add PID drive

    /**
     * @param timeout  The time that the command will be run
     * @param speedFPS The speed in feet per second that the motors should run at
     * @deprecated When possible use {@link PurePursuitCommand}
     * or in the future, a PID-Trapezoidal Motion Profiling drive distance command
     */
    public DeadreckoningDrive(double timeout, float speedFPS)
    {
        super(timeout);
        this.speedFPS = speedFPS;
    }

    @Override
    protected void execute()
    {
        Robot.DRIVE_TRAIN.runMotorsVelocity(speedFPS, speedFPS);
    }
}
