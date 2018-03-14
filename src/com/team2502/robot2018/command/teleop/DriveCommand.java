package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.localization.NavXLocationEstimator;
import com.team2502.robot2018.utils.Stopwatch;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Takes care of all Drivetrain related operations during Teleop, including automatic shifting
 * Automatic shifting will:
 * <li>
 * <ul>Space out shifting by at least 1/2 second</ul>
 * <ul>Invert itself if the driver holds a special button</ul>
 * <ul>Only shift when going mostly straight</ul>
 * <ul>Shift up if accelerating, going fast, and the driver is pushing hard on the sticks</ul>
 * <ul>Shift down if the sticks are being pushed but there is no acceleration</ul>
 * <ul>Shift down if the sticks aren't being pushed hard and the robot is going slow</ul>
 * </li>
 */
public class DriveCommand extends Command
{

    private final Stopwatch stopwatch;
    private EncoderDifferentialDriveLocationEstimator locationEstimator;

    public DriveCommand()
    {
        stopwatch = new Stopwatch();
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
