package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.util.ArrayList;
import java.util.List;

public class MotionProfileConstituent
{
    public static final List<MotionProfileConstituent> MOTION_PROFILE_CONSTITUENTS = new ArrayList<>();

    private final List<Waypoint> waypoints;
    private final Direction dir;

    private TrajectoryPoint[] leftPoints;
    private TrajectoryPoint[] rightPoints;

    private boolean initialized = false;

    public MotionProfileConstituent(List<Waypoint> waypoints, Direction dir)
    {
        MOTION_PROFILE_CONSTITUENTS.add(this);
        this.waypoints = waypoints;
        this.dir = dir;


    }

    public MotionProfileConstituent(MotionProfileConstituent motionProfileConstituent, Direction dir)
    {
        this(motionProfileConstituent.waypoints, dir);
    }

    public void init()
    {
        Waypoint[] waypointArray = new Waypoint[0];

        waypoints.toArray(waypointArray);
        Trajectory traj = Pathfinder.generate(waypointArray, Constants.SRXProfiling.CONFIG_SETTINGS);
        TankModifier modifier = new TankModifier(traj);
        modifier.modify(Constants.SRXProfiling.WHEELBASE_WIDTH);

        Trajectory leftTraj = modifier.getLeftTrajectory();
        Trajectory rightTraj = modifier.getRightTrajectory();

        leftPoints = new TrajectoryPoint[leftTraj.segments.length];
        rightPoints = new TrajectoryPoint[rightTraj.segments.length];

        // left side
        loadPoints(leftTraj, leftPoints, rightPoints);

        // right side
        loadPoints(rightTraj, rightPoints, leftPoints);

        initialized = true;
    }

    /**
     * Turn a segment into a {@link TrajectoryPoint}, negating position and velocity if the path is backwards
     *
     * @param segment The segment
     * @return The TrajectoryPoint
     */
    private TrajectoryPoint segmentToTrajPoint(Trajectory.Segment segment)
    {
        TrajectoryPoint point = new TrajectoryPoint();

        // Trajectory headings are in radians, but SRX wants them in degrees
        point.headingDeg = Pathfinder.r2d(segment.heading);

        point.isLastPoint = false;

        point.timeDur = Constants.SRXProfiling.PERIOD;

        // The position is shifted by the current pos in {@link DriveTrainSubsystem}
        point.position = Robot.DRIVE_TRAIN.fakeToRealEncUnits((float) segment.position * Constants.Physical.DriveTrain.FEET_TO_EPOS_DT) * dir.getValue();
        point.velocity = Robot.DRIVE_TRAIN.fakeToRealEncUnits((float) segment.velocity * Constants.Physical.DriveTrain.FPS_TO_EVEL_DT) * dir.getValue();


        point.profileSlotSelect0 = 0;
        point.profileSlotSelect1 = 0;

        return point;
    }

    /**
     * Load the segments of a trajectory into arrays of {@link TrajectoryPoint}s
     *
     * @param traj            The trajectory to load
     * @param forwardsPoints  Where to put the points if we are going forwards
     * @param backwardsPoints Where to put the points if we are going backwards
     */
    private void loadPoints(Trajectory traj, TrajectoryPoint[] forwardsPoints, TrajectoryPoint[] backwardsPoints)
    {
        for(int i = 0; i < traj.segments.length; i++)
        {
            Trajectory.Segment segment = traj.get(i);
            TrajectoryPoint point = segmentToTrajPoint(segment);

            point.isLastPoint = i + 1 == traj.segments.length;
            point.zeroPos = Constants.SRXProfiling.USE_RELATIVE_COORDS && i == 0;

            switch(dir)
            {
                case BACKWARD:
                    forwardsPoints[i] = point;
                    break;
                case FORWARD:
                default:
                    backwardsPoints[i] = point;

            }
        }
    }

    public List<Waypoint> getWaypoints()
    {
        return waypoints;
    }

    public Direction getDir()
    {
        return dir;
    }

    public TrajectoryPoint[] getLeftPoints()
    {
        return leftPoints;
    }

    public TrajectoryPoint[] getRightPoints()
    {
        return rightPoints;
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public MotionProfileConstituent reverse()
    {
        return new MotionProfileConstituent(this, Direction.opposite(dir));
    }

    public static void initTrajectories()
    {
        for(MotionProfileConstituent profile : MOTION_PROFILE_CONSTITUENTS)
        {
            profile.init();
        }

    }


}


