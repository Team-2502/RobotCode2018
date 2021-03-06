package com.team2502.robot2018.pathplanning.srxprofiling;


import com.team2502.robot2018.Constants;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Warning!
 * <p>
 * While pure pursuit has the y axis be the forward/backward direction,
 * trajectories has the x axis be the forward backward direction.
 * <p>
 * Stuff is relative, not absolute
 */
public class TrajConfig
{
    public static Trajectory[] testTraj = combineTraj(toTankDrive(new Waypoint(0, 0, 0),
                                                                  new Waypoint(3, 0, 0)),
                                                      reverseTraj(toTankDrive(new Waypoint(0, 0, 0),
                                                                              new Waypoint(3, 0, 0))));

    private static List<Waypoint> flipX(List<Waypoint> waypoints)
    {
        List<Waypoint> result = new ArrayList<>(waypoints.size());

        for(Waypoint waypoint : waypoints)
        {
            result.add(new Waypoint(-waypoint.x, waypoint.y, waypoint.angle));
        }
        return result;
    }

    private static Trajectory[] flipX(Trajectory[] input)
    {
        return new Trajectory[] { input[1], input[0] };
    }

    private static List<Waypoint> convertFromPP(List<com.team2502.robot2018.pathplanning.purepursuit.Waypoint> waypoints)
    {
        List<Waypoint> result = new ArrayList<>(waypoints.size());

        double ppX = waypoints.get(0).getLocation().x;
        double ppY = waypoints.get(0).getLocation().y;

        result.add(new Waypoint(ppY, ppX, 0));
        for(int i = 1; i < waypoints.size(); i++)
        {
            com.team2502.robot2018.pathplanning.purepursuit.Waypoint thisWaypoint = waypoints.get(i);
            com.team2502.robot2018.pathplanning.purepursuit.Waypoint lastWaypoint = waypoints.get(i - 1);

            ppX = thisWaypoint.getLocation().x;
            ppY = thisWaypoint.getLocation().y;

            double lastPPX = lastWaypoint.getLocation().x;
            double lastPPY = lastWaypoint.getLocation().y;

            double dX = ppX - lastPPX;
            double dY = ppY - lastPPY;

            double angleRadians = Math.atan(dY / dX);


            result.add(new Waypoint(ppY, ppX, angleRadians));
        }
        return result;
    }

    private static Trajectory toTrajectory(Waypoint... waypoints)
    {
        return Pathfinder.generate(waypoints, Constants.SRXProfiling.CONFIG_SETTINGS);
    }

    private static Trajectory[] toTankDrive(Waypoint... waypoints)
    {
        Trajectory traj = toTrajectory(waypoints);
        TankModifier modifier = new TankModifier(traj);
        modifier.modify(Constants.SRXProfiling.WHEELBASE_WIDTH);
        return new Trajectory[] { modifier.getLeftTrajectory(), modifier.getRightTrajectory() };
    }

    /**
     * returns a copied trajectory
     *
     * @param traj a traj
     * @return traj but reversed
     */
    private static Trajectory reverseTraj(Trajectory traj)
    {
        Trajectory.Segment[] segments = new Trajectory.Segment[traj.length()];

        for(int i = 0; i < traj.length(); i++)
        {
            Trajectory.Segment segment = traj.get(i);

            // using pass by reference like a good boy
            segments[i] = new Trajectory.Segment(segment.dt, segment.x, segment.y, -segment.position, -segment.velocity, segment.acceleration, segment.jerk, segment.heading);
        }

        return new Trajectory(segments);
    }

    private static Trajectory[] reverseTraj(Trajectory[] trajs)
    {
        Trajectory[] ret = new Trajectory[trajs.length];
        for(int i = 0; i < trajs.length; i++)
        {
            ret[i] = reverseTraj(trajs[i]);
        }
        return ret;

    }

    private static Trajectory[] combineTraj(Trajectory[]... trajs)
    {
        int totalCount = 0;
        for(Trajectory[] traj : trajs)
        {
            totalCount += traj[0].length();
        }
        System.out.println("totalCount = " + totalCount);

        Trajectory.Segment[] leftSegments = new Trajectory.Segment[totalCount];
        Trajectory.Segment[] rightSegments = new Trajectory.Segment[totalCount];

        int currentCountLeft = 0;
        int currentCountRight = 0;
        boolean oneLoop = false;

        double lastLeftPos = 0;
        double lastRightPos = 0;


        for(Trajectory[] traj : trajs)
        {
            Trajectory.Segment[] partialLeftSegments = traj[0].segments;
            Trajectory.Segment[] partialRightSegments = traj[1].segments;

            for(int j = 0; j < partialLeftSegments.length; j++)
            {
                leftSegments[currentCountLeft] = partialLeftSegments[j];
                if(oneLoop)
                {
                    leftSegments[currentCountLeft].position += lastLeftPos;
                }

                currentCountLeft++;
            }
            for(int j = 0; j < partialRightSegments.length; j++)
            {
                rightSegments[currentCountRight] = partialRightSegments[j];
                if(oneLoop)
                {
                    rightSegments[currentCountRight].position += lastRightPos;

                }
                currentCountRight++;
            }
            oneLoop = true;
            lastLeftPos = leftSegments[currentCountLeft - 1].position;
            lastRightPos = leftSegments[currentCountRight - 1].position;
        }
        return new Trajectory[] { new Trajectory(leftSegments), new Trajectory(rightSegments) };

    }

    public static class Center
    {

        // It works
        // pos y = go left, flip x makes it go right
        public static final Trajectory[] firstCubeRightSwitch = flipX(toTankDrive(new Waypoint(0, 0, 0),
//                                                                                  new Waypoint(2.45, 2, Math.PI / 4),
                                                                                  new Waypoint(8.5, 3.5, 0)));


        public static final Trajectory[] secondCubeRightSwitch = combineTraj(Right.toSecondCube, Right.forwardToCube, Right.backUpFromCube, Right.backToSwitch);

        public static final Trajectory[] secondCubeLeftSwitch = flipX(secondCubeRightSwitch);

        public static final Trajectory[] firstCubeLeftSwitch = flipX(firstCubeRightSwitch);

        public static final Trajectory[] twoCubesleftSwitch = combineTraj(firstCubeLeftSwitch, secondCubeLeftSwitch);

        public static final Trajectory[] twoCubesRightSwitch = combineTraj(firstCubeRightSwitch, secondCubeRightSwitch);

        public static void init()
        {
            System.out.println("secondCubeRightSwitch[0].segments.length = " + (secondCubeRightSwitch[0].segments.length));
            Right.init();
        }

        public static class Right
        {
            public static final Trajectory[] toSecondCube = reverseTraj(toTankDrive(new Waypoint(0, 0, 0),
                                                                                    new Waypoint(50D / 12, 26D / 12, Math.PI / 6),
                                                                                    new Waypoint(75D / 12, 44D / 12, 0)));

            public static final Trajectory[] forwardToCube = toTankDrive(new Waypoint(0, 0, 0),
                                                                         new Waypoint(3, 0, 0));

            static final Trajectory[] backUpFromCube = reverseTraj(toTankDrive(new Waypoint(0, 0, 0),
                                                                               new Waypoint(3, 0, 0)));

            static final Trajectory[] backToSwitch = flipX(toTankDrive(new Waypoint(0, 0, 0),
                                                                       new Waypoint(30D / 12, 26D / 12, Math.PI / 3),
                                                                       new Waypoint(96D / 12, 46D / 12, 0)));
            public static Trajectory[] finishDoubleCube = combineTraj(forwardToCube, backUpFromCube, backToSwitch);

            static void init() { }


        }

    }

    public static class Left
    {
        public static final Trajectory[] twoCube = combineTraj(TwoCube.backUpToCube, TwoCube.forwardToCube, TwoCube.backUpToCube, TwoCube.forwardToScale);

        public static void init()
        {
            System.out.println("twoCube[0].segments.length = " + (twoCube[0].segments.length));
            TwoCube.init();
//            Pathfinder.writeToCSV(new File("/home/lvuser/LEFT.csv"), Right.backToSwitch[0]);
//            Pathfinder.writeToCSV(new File("/home/lvuser/RIGHT.csv"), Right.backToSwitch[1]);
        }

        private static class TwoCube
        {
            static final Trajectory[] backUpToCube = reverseTraj(toTankDrive(new Waypoint(0, 0, -Math.PI / 2),
                                                                             new Waypoint(4.25, -2.5, 0)));

            static final Trajectory[] forwardToCube = toTankDrive(new Waypoint(0, 0, 0),
                                                                  new Waypoint(3.75, -2.5, -Math.PI));

            static final Trajectory[] backUpToScale = reverseTraj(forwardToCube);

            static final Trajectory[] forwardToScale = reverseTraj(backUpToCube);

            static void init() { }

        }

    }

}
