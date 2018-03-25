package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.ITankRobotBounds;
import com.team2502.robot2018.trajectory.Lookahead;
import com.team2502.robot2018.trajectory.PurePursuitMovementStrategy;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

import java.util.List;

import static com.team2502.robot2018.Constants.PurePursuit;

public class PurePursuitCommand extends Command
{
    private ITankRobotBounds tankRobot;

    private PurePursuitMovementStrategy purePursuitMovementStrategy;
    private List<Waypoint> waypoints;
    private Lookahead lookahead;
    private boolean drift;
    private boolean autoFirstPoint;

    /**
     * Given some waypoints, drive through them
     *
     * @param waypoints the waypoints
     */
    public PurePursuitCommand(List<Waypoint> waypoints, boolean drift)
    {
        this(waypoints, Constants.PurePursuit.LOOKAHEAD, drift);
    }

    /**
     * Drive through some waypoints with extra options
     *
     * @param waypoints Waypoints to drive through
     * @param lookahead Bean for max + min vel and accel
     * @param drift     If the robot should brake at the end or drift
     */
    public PurePursuitCommand(List<Waypoint> waypoints, Lookahead lookahead, boolean drift)
    {
        this.autoFirstPoint = autoFirstPoint;
        this.drift = drift;
        this.waypoints = waypoints;
        this.lookahead = lookahead;
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
                return Float.NaN;
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
                return Float.NaN;
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
                return Float.NaN;
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
                return Float.NaN;
            }

            /**
             * @return The lateral distance between wheels
             */
            @Override
            public float getLateralWheelDistance()
            { return PurePursuit.LATERAL_WHEEL_DISTANCE_FT; }
        };

//        rotLocEstimator = new NavXLocationEstimator();
//        transLocEstimator = new EncoderDifferentialDriveLocationEstimator(rotLocEstimator);
//
//        sendableNavX = new SendableNavX(() -> MathUtils.rad2Deg(-rotLocEstimator.estimateHeading()), "purePursuitHeading");
    }

    @Override
    protected void initialize()
    {
        // set location to first robot point TODO: make better... this way of doing it is crap
        waypoints.get(0).setLocation(Robot.ROBOT_LOCALIZATION_COMMAND.estimateLocation());
        Robot.writeLog("init PP", 80);
        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, Robot.ROBOT_LOCALIZATION_COMMAND, Robot.ROBOT_LOCALIZATION_COMMAND, Robot.ROBOT_LOCALIZATION_COMMAND, waypoints, lookahead, true);
    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();

        ImmutableVector2f usedEstimatedLocation = purePursuitMovementStrategy.getUsedEstimatedLocation();

        SmartDashboard.putNumber("purePursuitLocX", usedEstimatedLocation.get(0));
        SmartDashboard.putNumber("purePursuitLocY", usedEstimatedLocation.get(1));

        ImmutableVector2f wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();

        float wheelL = wheelVelocities.get(0);
        float wheelR = wheelVelocities.get(1);

        SmartDashboard.putNumber("PPwheelL", wheelVelocities.get(0));
        SmartDashboard.putNumber("PPwheelR", wheelVelocities.get(1));

//        Robot.writeLog("wheelL %.2f", 200, wheelL);
//        Robot.writeLog("wheelR %.2f", 200, wheelR);
        Robot.DRIVE_TRAIN.runMotorsVelocity(wheelL, wheelR);
    }

    @Override
    protected boolean isFinished()
    {
        boolean finishedPath = purePursuitMovementStrategy.isFinishedPath();
        if(finishedPath)
        {
//            SmartDashboard.putBoolean("PPisSuccess", purePursuitMovementStrategy.isWithinTolerences());
//            if(purePursuitMovementStrategy.isWithinTolerences())
//            {
//                System.out.println("\n\nSUCCESS!\n\n");
//            }
            Robot.DRIVE_TRAIN.stop();
        }
        return finishedPath;

    }
}
