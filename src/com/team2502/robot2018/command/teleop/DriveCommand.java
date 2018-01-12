package com.team2502.robot2018.command.teleop;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.subsystem.TransmissionSubsystem;
import com.team2502.robot2018.trajectory.ILocationEstimator;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;
import org.joml.Vector2f;

import static com.team2502.robot2018.command.autonomous.PurePursuitCommand.RAW_UNIT_PER_ROT;
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
    private final DriveTrainSubsystem driveTrainSubsystem;
    private AHRS navx;
    private final TransmissionSubsystem transmission;
    private final Vector2f estimatedLocation = new Vector2f(0,0);

    private long lastTime = -1;
    public float heading = 0;
    private float initAngleDegrees;

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

    ILocationEstimator locationEstimator = () ->
    {
        // How many 100 ms intervals occured
        float dTime = (float) (getDTime() / 10F);

        // talon inversed
        float leftRevPer100ms = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0)/RAW_UNIT_PER_ROT;

        float rightRevPer100ms = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0)/RAW_UNIT_PER_ROT;

        float leftVel = leftRevPer100ms * Robot.Physical.WHEEL_DIAMETER_FT;

        float rightVel = rightRevPer100ms * Robot.Physical.WHEEL_DIAMETER_FT;

        Vector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE, dTime
                , heading);
        Log.debug("adpp: "+absoluteDPos);
        Vector2f absLoc = estimatedLocation.add(absoluteDPos);

        heading=MathUtils.Geometry.getDTheta(initAngleDegrees, (float) navx.getAngle());
        return absLoc;
    };

    public DriveCommand()
    {
        requires(Robot.DRIVE_TRAIN);
        requires(Robot.TRANSMISSION);
        driveTrainSubsystem = Robot.DRIVE_TRAIN;
        transmission = Robot.TRANSMISSION;
        navx = Robot.NAVX;
        initAngleDegrees = (float) navx.getAngle();
    }

    @Override
    protected void initialize()
    {
//        driveTrainSubsystem.setTeleopSettings();
    }

    @Override
    protected void execute()
    {
        Vector2f estimateLocation = locationEstimator.estimateLocation();
        // double radians = - MathUtils.deg2Rad(navx.getAngle() - initAngleDegrees) % TAU;
        // Log.debug("{0,number,#.###}", radians);
        //float encLeft = (float) -Robot.DRIVE_TRAIN.leftRearTalonEnc.get();
        //float encRight = (float) Robot.DRIVE_TRAIN.rightRearTalonEnc.get();
//        System.out.printf("%.2f,%.2f\n", encLeft, encRight);
//        System.out.println("NavX: " + Robot.NAVX.getDisplacementX() + "," + Robot.NAVX.getDisplacementY() + "," + Robot.NAVX.getDisplacementZ());
//        System.out.println(MathUtils.LinearAlgebra.rotate2D(new Vector(navx.getDisplacementX(), navx.getDisplacementY()),-initAngleRadians));
        SmartDashboard.putBoolean("DT: AutoShifting Enabled?", !transmission.disabledAutoShifting);
        driveTrainSubsystem.drive();

        if(!transmission.disabledAutoShifting)
        {
            // Check that at least 1/2 second has passed since last shifting
            if((System.currentTimeMillis() - Robot.SHIFTED) >= 500)
            {
                // Do the opposite if the driver is forcing a shift
                if(OI.JOYSTICK_DRIVE_RIGHT.getRawButton(RobotMap.Joystick.Button.FORCE_LOW_GEAR))
                {
                    Log.warn("Shifting down forced by driver.");
                    transmission.setGear(false);
                }
                else // If the driver is cool with auto shifting doing its thing
                {
                    // Make sure that we're going mostly straight
                    if(driveTrainSubsystem.turningFactor() < 0.1F)
                    {
                        float accel = navx.getRawAccelY();
                        float speed = driveTrainSubsystem.avgVel();

                        // Shift up if we are accelerating and going fast and the driver is putting the joystick at least 80% forward or backward
                        if(Math.abs(accel) > 0.15F && speed > RobotMap.Motor.SHIFT_UP_THRESHOLD && OI.joysThreshold(0.8D, true))
                        {
                            if(!transmission.highGear) { Log.info("Shifting up."); }
                            transmission.setGear(true);
                        }
                        else if(!transmission.signSame(accel, driveTrainSubsystem.rightRearTalonEnc.getSelectedSensorVelocity(0)) && OI.joysThreshold(0.8D, false)) /* If we are not accelerating very fast but the driver is still pushing forward we shift down because it is probably a pushing match */
                        {
                            if(transmission.highGear) { Log.info("Shifting down because you're a bad driver."); }
                            transmission.setGear(false);
                        }
                        else if(OI.joysThreshold(30.0D, false) && speed < RobotMap.Motor.SHIFT_DOWN_THRESHOLD) /* If we're going slow and the driver wants it to be that way we shift down */
                        {
                            if(transmission.highGear) { Log.info("Shifting down because slow."); }
                            transmission.setGear(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected boolean isFinished()
    { return false; }

    @Override
    protected void end()
    { driveTrainSubsystem.stop(); }
}
