package com.team2502.robot2018.command.autonomous;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.trajectory.ILocationEstimator;
import com.team2502.robot2018.trajectory.PurePursuitMovementStrategy;
import com.team2502.robot2018.trajectory.ITankRobot;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import logger.Log;
import org.joml.Vector2f;

import java.util.List;
import java.util.function.Function;

public class PurePursuitCommand extends Command
{
    private final ITankRobot tankRobot;
    public float lookAheadDistance;
    public static final float TAU = 2 * 3.1415F;
    public static final float RAW_UNIT_PER_ROT = 4096F;
    private DriveTrainSubsystem driveTrain;
    private AHRS navx;
    private PurePursuitMovementStrategy purePursuitMovementStrategy;
    private long lastTime = -1;
    private float initAngleDegrees;
    private float initAngleRadians;

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

    public PurePursuitCommand(List<Vector2f> waypoints, float lookAheadDistance)
    {
        navx = Robot.NAVX;
        navx.resetDisplacement();
        initAngleDegrees = (float) navx.getAngle();
        initAngleRadians = MathUtils.deg2Rad(initAngleDegrees);
        this.lookAheadDistance = lookAheadDistance;
        requires(Robot.DRIVE_TRAIN);
        driveTrain = Robot.DRIVE_TRAIN;

        tankRobot = new ITankRobot()
        {
            /**
             *
             * @return The heading (angle) of the robot. In radians from [0,2pi). Increases counterclockwise.
             */
            @Override
            public float getHeading()
            {
                double radians = MathUtils.deg2Rad(navx.getAngle() - initAngleDegrees);
                return (float) -(radians % TAU);
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

        ILocationEstimator locationEstimator = () ->
        {
            // How many 100 ms intervals occured
            float dTime = (float) (getDTime() / 10F);

            // talon inversed
            float leftRevPer100ms = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0)/RAW_UNIT_PER_ROT;

            float rightRevPer100ms = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0)/RAW_UNIT_PER_ROT;

            float leftVel = leftRevPer100ms * Robot.Physical.WHEEL_DIAMETER_FT;

            float rightVel = rightRevPer100ms * Robot.Physical.WHEEL_DIAMETER_FT;

            return purePursuitMovementStrategy.getUsedEstimatedLocation()
                                              .add(
                                                      MathUtils.Kinematics.getAbsoluteDPos(
                                                              leftVel,rightVel,Robot.LATERAL_WHEEL_DISTANCE,dTime
                                                              ,purePursuitMovementStrategy.getUsedHeading())
                                                      );

            /*
            float rotVelocity = (leftVel - rightVel) / tankRobot.getLateralWheelDistance();

            Function<Float, Vector2f> estimatePositionFromDTheta = dTheta -> {

                float dxRelative = -purePursuitMovementStrategy.getPathRadius() * (1 - MathUtils.cos(-dTheta));
                float dyRelative = -purePursuitMovementStrategy.getPathRadius() * MathUtils.sin(-dTheta);

                Vector2f dRelativeVector = new Vector2f(dxRelative, dyRelative);
                Vector2f rotated = MathUtils.LinearAlgebra.rotate2D(dRelativeVector, purePursuitMovementStrategy.getUsedHeading());
                return rotated.add(purePursuitMovementStrategy.getUsedEstimatedLocation());
            };

            Function<Double, Vector2f> dTimeToPosition = dTime1 -> {
                double dTheta = dTime1 * rotVelocity; // purePursuitMovementStrategy.getRotVelocity();
                return estimatePositionFromDTheta.apply((float) dTheta); //
            };
            return dTimeToPosition.apply(dTime);
            */
        };


        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, locationEstimator, waypoints, lookAheadDistance);
    }

    @Override
    protected void initialize()
    {
        System.out.println("NavX initial angle" + navx.getAngle());
//        driveTrain.setAutonSettings();
    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();
        Vector2f wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();
        float x = wheelVelocities.x;
        float y = wheelVelocities.y;
        Log.info("Wheel velocities: " + x + " ::: " + y + " ::: Wheel velocities");
        Log.info("Unmoded Loc: " + navx.getDisplacementX() + "," + navx.getDisplacementY());
        Log.info("Heading: " + tankRobot.getHeading());
        driveTrain.runMotors(x, y);
    }

    @Override
    protected boolean isFinished()
    {
        return purePursuitMovementStrategy.isFinishedPath();

    }

    @Override
    protected void end()
    {
        driveTrain.setTeleopSettings();
    }
}
