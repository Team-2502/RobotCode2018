package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.localization.NavXLocationEstimator;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Takes care of letting Driver 1 drive the robot
 */
public class DriveCommand extends Command
{

    private EncoderDifferentialDriveLocationEstimator locationEstimator;

    public DriveCommand()
    {
        requires(Robot.DRIVE_TRAIN);
    }

    @Override
    protected void initialize()
    {
        locationEstimator = new EncoderDifferentialDriveLocationEstimator(new NavXLocationEstimator());
    }

    @Override
    protected void execute()
    {
        locationEstimator.estimateLocation();

        Robot.DRIVE_TRAIN.drive();

    }

    @Override
    protected boolean isFinished()
    { return false; }

    @Override
    protected void end()
    { Robot.DRIVE_TRAIN.stop(); }
}
