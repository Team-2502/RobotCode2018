package com.team2502.robot2018.command.autonomous;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.trajectory.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.ITankRobot;
import com.team2502.robot2018.trajectory.PurePursuitMovementStrategy;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.joml.Vector2f;

import java.util.List;

public class PurePursuitCommand extends Command
{
    public static final float RAW_UNIT_PER_ROT = 4096F;
    private final ITankRobot tankRobot;
    private final EncoderDifferentialDriveLocationEstimator locationEstimator;
    public float lookAheadDistance;
    private DriveTrainSubsystem driveTrain;
    private AHRS navx;
    private PurePursuitMovementStrategy purePursuitMovementStrategy;
    private long lastTime = -1;
    private float initAngleDegrees;

    public PurePursuitCommand(List<Vector2f> waypoints, float lookAheadDistance)
    {
        navx = Robot.NAVX;
        navx.resetDisplacement();
        this.lookAheadDistance = lookAheadDistance;
        requires(Robot.DRIVE_TRAIN);
        driveTrain = Robot.DRIVE_TRAIN;
        initAngleDegrees = (float) navx.getAngle();

        tankRobot = new ITankRobot()
        {
            /**
             *
             * @return The heading (angle) of the robot. In radians from [0,2pi). Increases counterclockwise.
             */
            @Override
            public float getHeading()
            {
                float rad = MathUtils.Geometry.getDTheta(initAngleDegrees, (float) navx.getAngle());
                return rad;
            }

            /**
             *
             * @return The max velocity the right wheels can travel
             */
            @Override
            public float getV_rMax()
            { return Robot.VR_MAX; }

            /**
             *
             * @return The max velocity the left wheels can travel
             */
            @Override
            public float getV_lMax()
            { return Robot.VL_MAX; }

            /**
             *
             * @return The min velocity the left wheels can travel
             */
            @Override
            public float getV_lMin()
            { return Robot.VL_MIN; }

            /**
             *
             * @return The min velocity the right wheels can travel
             */
            @Override
            public float getV_rMin()
            { return Robot.VR_MIN; }

            /**
             *
             * @return The lateral distance between wheels
             */
            @Override
            public float getLateralWheelDistance()
            { return Robot.LATERAL_WHEEL_DISTANCE; }
        };

        locationEstimator = new EncoderDifferentialDriveLocationEstimator();
        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, locationEstimator, waypoints, lookAheadDistance);
        System.out.println("initAngleDegrees:\n\n\n\n "+initAngleDegrees);
        locationEstimator.initialize();
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
//        System.out.println("NavX initial angle" + navx.getAngle());
//        driveTrain.setAutonSettings();
    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();

        Vector2f wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();
        float x = wheelVelocities.x;
        float y = wheelVelocities.y;
//        driveTrain.run
        driveTrain.runMotors(x, y);
    }

    @Override
    protected boolean isFinished()
    {
        boolean finishedPath = purePursuitMovementStrategy.isFinishedPath();
        if(finishedPath){
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
