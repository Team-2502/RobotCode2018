package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.sendables.SendableNavX;
import com.team2502.robot2018.trajectory.ITankRobotBounds;
import com.team2502.robot2018.trajectory.Lookahead;
import com.team2502.robot2018.trajectory.PurePursuitMovementStrategy;
import com.team2502.robot2018.trajectory.Waypoint;
import com.team2502.robot2018.trajectory.localization.EncoderDifferentialDriveLocationEstimator;
import com.team2502.robot2018.trajectory.localization.NavXLocationEstimator;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

import java.util.List;

public class PurePursuitCommand extends Command
{
    private final ITankRobotBounds tankRobot;
    private final EncoderDifferentialDriveLocationEstimator transLocEstimator;
    private final NavXLocationEstimator rotLocEstimator;
    private final SendableNavX sendableNavX;
    private PurePursuitMovementStrategy purePursuitMovementStrategy;

    public PurePursuitCommand(List<Waypoint> waypoints)
    {
        this(waypoints, Constants.LOOKAHEAD, Constants.STOP_DIST_TOLERANCE_FT);
    }

    public PurePursuitCommand(List<Waypoint> waypoints, Lookahead lookahead, float stopDistance)
    {
        requires(Robot.DRIVE_TRAIN);

        tankRobot = new ITankRobotBounds()
        {
            /**
             * @return The max velocity the right wheels can travel
             */
            @Override
            public float getV_rMax()
            { return Float.NaN; } // Not used in PurePursuitMovementStrategy

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
            { return Float.NaN; }

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
            { return Float.NaN; }

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
            { return Float.NaN; }

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
        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, transLocEstimator, rotLocEstimator, transLocEstimator, waypoints, lookahead, stopDistance);
    }

    @Override
    protected void initialize()
    {
        SmartDashboard.putBoolean("PPisClose", purePursuitMovementStrategy.isClose());
        SmartDashboard.putBoolean("PPisSuccess", purePursuitMovementStrategy.isWithinTolerences());
    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();

        sendableNavX.updateDashboard();

        ImmutableVector2f usedEstimatedLocation = purePursuitMovementStrategy.getUsedEstimatedLocation();

        SmartDashboard.putNumber("purePursuitLocX", usedEstimatedLocation.get(0));
        SmartDashboard.putNumber("purePursuitLocY", usedEstimatedLocation.get(1));

        ImmutableVector2f wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();

        float wheelL = wheelVelocities.get(0);
        float wheelR = wheelVelocities.get(1);

        SmartDashboard.putNumber("PPwheelL", wheelVelocities.get(0));
        SmartDashboard.putNumber("PPwheelR", wheelVelocities.get(1));

        float leftWheelVel = wheelL;
        float rightWheelVel = wheelR;

        Robot.DRIVE_TRAIN.runMotorsVelocity(leftWheelVel, rightWheelVel);
    }

    @Override
    protected boolean isFinished()
    {
        boolean finishedPath = purePursuitMovementStrategy.isFinishedPath();
        if(finishedPath)
        {
            SmartDashboard.putBoolean("PPisSuccess", purePursuitMovementStrategy.isWithinTolerences());
            if(purePursuitMovementStrategy.isWithinTolerences())
            {
                System.out.println("\n\nSUCCESS!\n\n");
            }
            Robot.DRIVE_TRAIN.stop();
        }
        return finishedPath;

    }
}
