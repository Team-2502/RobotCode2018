package com.team2502.robot2018.command.autonomous;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.sendables.SendableNavX;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.trajectory.ITankRobotBounds;
import com.team2502.robot2018.trajectory.PurePursuitMovementStrategy;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.localization.NavXLocationEstimator;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;
import org.joml.ImmutableVector2f;

import java.util.List;

public class PurePursuitCommand extends Command
{
    private final ITankRobotBounds tankRobot;
    private final EncoderDifferentialDriveLocationEstimator transLocEstimator;
    private final NavXLocationEstimator rotLocEstimator;
    private final SendableNavX sendableNavX;
    private final float stopDistance;
    private float lookAheadDistance;
    private DriveTrainSubsystem driveTrain;
    private AHRS navx;
    private PurePursuitMovementStrategy purePursuitMovementStrategy;
    private long lastTime = -1;
    private float initAngleDegrees;

    public PurePursuitCommand(List<ImmutableVector2f> waypoints)
    {
        this(waypoints, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT);
    }

    public PurePursuitCommand(List<ImmutableVector2f> waypoints, float lookAheadDistance, float stopDistance)
    {
        navx = Robot.NAVX;
        navx.resetDisplacement();

        this.lookAheadDistance = lookAheadDistance;
        requires(Robot.DRIVE_TRAIN);
        driveTrain = Robot.DRIVE_TRAIN;
        initAngleDegrees = (float) navx.getAngle();
        this.stopDistance = stopDistance;

        tankRobot = new ITankRobotBounds()
        {
            /**
             * @return The max velocity the right wheels can travel
             */
            @Override
            public float getV_rMax()
            { return Constants.VR_MAX; }

            @Override
            public float getA_rMax()
            {
                return Constants.AR_MAX;
            }

            /**
             * @return The max velocity the left wheels can travel
             */
            @Override
            public float getV_lMax()
            { return Constants.VL_MAX; }

            @Override
            public float getA_lMax()
            {
                return Constants.AL_MAX;
            }

            /**
             * @return The min velocity the left wheels can travel
             */
            @Override
            public float getV_lMin()
            { return Constants.VL_MIN; }

            @Override
            public float getA_lMin()
            {
                return Constants.AL_MIN;
            }

            /**
             * @return The min velocity the right wheels can travel
             */
            @Override
            public float getV_rMin()
            { return Constants.VR_MIN; }

            @Override
            public float getA_rMin()
            {
                return Constants.AR_MIN;
            }

            /**
             * @return The lateral distance between wheels
             */
            @Override
            public float getLateralWheelDistance()
            { return Constants.LATERAL_WHEEL_DISTANCE_FT; }
        };

        rotLocEstimator = new NavXLocationEstimator();
        transLocEstimator = new EncoderDifferentialDriveLocationEstimator(rotLocEstimator);

        sendableNavX = new SendableNavX(() -> MathUtils.rad2Deg(-rotLocEstimator.estimateHeading()), "purePursuitHeading");
        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, transLocEstimator, rotLocEstimator, transLocEstimator, waypoints, lookAheadDistance, stopDistance);
        Log.info("initAngleDegrees: {0,number,0.00}\n" + initAngleDegrees);
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
    {

    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();

        sendableNavX.updateDashboard();

        SmartDashboard.putNumber("purePursuitHeadingRad", purePursuitMovementStrategy.getUsedHeading());

        ImmutableVector2f usedEstimatedLocation = purePursuitMovementStrategy.getUsedEstimatedLocation();

        SmartDashboard.putNumber("purePursuitLocX", usedEstimatedLocation.get(0));
        SmartDashboard.putNumber("purePursuitLocY", usedEstimatedLocation.get(1));

        ImmutableVector2f wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();

        float wheelL = wheelVelocities.get(0);
        float wheelR = wheelVelocities.get(1);

        SmartDashboard.putNumber("PPwheelL", wheelVelocities.get(0));
        SmartDashboard.putNumber("PPwheelR", wheelVelocities.get(1));

        ImmutableVector2f relativeGoalPoint = purePursuitMovementStrategy.getRelativeGoalPoint();
        if(relativeGoalPoint != null)
        {
            SmartDashboard.putNumber("rGPx", relativeGoalPoint.get(0));
            SmartDashboard.putNumber("rGPy", relativeGoalPoint.get(1));
        }

        ImmutableVector2f absGP = purePursuitMovementStrategy.getAbsoluteGoalPoint();
        if(absGP != null)
        {
            SmartDashboard.putNumber("GPx", absGP.get(0));
            SmartDashboard.putNumber("GPy", absGP.get(1));
        }

        SmartDashboard.putBoolean("PPisClose", purePursuitMovementStrategy.isClose());

        SmartDashboard.putBoolean("PPisSuccess", purePursuitMovementStrategy.isSuccessfullyFinished());

        driveTrain.runMotors(ControlMode.Velocity, wheelL * Constants.FPS_TO_EVEL, wheelR * Constants.FPS_TO_EVEL);
//        driveTrain.runMotors(wheelL, wheelR);
    }

    @Override
    protected boolean isFinished()
    {
        boolean finishedPath = purePursuitMovementStrategy.isFinishedPath();
        if(finishedPath)
        {
            SmartDashboard.putBoolean("PPisSuccess", purePursuitMovementStrategy.isSuccessfullyFinished());
            System.out.println("\n!!!\nBRAKING\n!!!!\n");
            Robot.DRIVE_TRAIN.leftRearTalonEnc.set(ControlMode.Disabled, 0.0F);
            Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Disabled, 0.0F);
        }
        return finishedPath;

    }

    @Override
    protected void end()
    {
        driveTrain.setTeleopSettings();
    }
}
