package com.team2502.robot2018.command.teleop;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;
import org.joml.Vector2f;

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
    private final ImmutableVector2f estimatedLocation = new ImmutableVector2f(0, 0);
    public float heading = 0;
    private AHRS navx;
    private long lastTime = -1;
    private float initAngleDegrees;
    private EncoderDifferentialDriveLocationEstimator encoderLocationEstimator;
    private ImmutableVector2f lastEstimatedLocation = new ImmutableVector2f();

    public DriveCommand()
    {
        requires(Robot.DRIVE_TRAIN);
        navx = Robot.NAVX;
        initAngleDegrees = (float) navx.getAngle();
    }

    private static float compare(Vector2f a, Vector2f b)
    {
        float xDiff = Math.abs(a.x - b.x);
        float yDiff = Math.abs(a.y - b.y);
        return (xDiff + yDiff) / 2.0F;
    }

    /**
     * @return difference in seconds since last time the method was called
     */
    double getDTime()
    {
        long nanoTime = System.nanoTime();
        double dTime;
        dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return (dTime / 1E6);
    }

    @Override
    protected void initialize()
    { encoderLocationEstimator = new EncoderDifferentialDriveLocationEstimator(); }

    @Override
    protected void execute()
    {
        ImmutableVector2f estimateLocation = encoderLocationEstimator.estimateLocation();
        SmartDashboard.putBoolean("DT: AutoShifting Enabled?", !Robot.TRANSMISSION_SOLENOID.disabledAutoShifting);
        Robot.DRIVE_TRAIN.drive();

    }

    @Override
    protected boolean isFinished()
    { return false; }

    @Override
    protected void end()
    { Robot.DRIVE_TRAIN.stop(); }
}
