package com.team2502.robot2018.pathplanning.srxprofiling;

import com.team2502.robot2018.command.autonomous.ingredients.PathConfig;
import com.team2502.robot2018.pathplanning.purepursuit.Path;
import jaci.pathfinder.Waypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Warning!
 *
 * While pure pursuit has the y axis be the forward/backward direction,
 * trajectories has the x axis be the forward backward direction.
 *
 * Stuff is relative, not absolute
 */
public class TrajConfig
{
    public static class Center
    {


        public static class Right
        {
            public static final List<Waypoint> rightSwitch = convertFromPP(PathConfig.Center.rightSwitch);

            public static final int toSecondCubeDir = -1;
            public static final MotionProfileConstituent goInFrontOfCube = new MotionProfileConstituent(Arrays.asList(new Waypoint(0, 0, 0),
                                                                                                                      new Waypoint(-50D/12, -26D/12, Math.PI / 4),
                                                                                                                      new Waypoint(-75D/12, -40D/12, 0)), Direction.BACKWARD);

            public static final MotionProfileConstituent goToCube = new MotionProfileConstituent(Arrays.asList(new Waypoint(0, 0, 0),
                                                                                                               new Waypoint(3, 0, 0))); // angle correction for previous step

            public static final MotionProfile secondCubeRightSwitch = new MotionProfile(goInFrontOfCube,
                                                                                        goToCube,
                                                                                        goToCube.reverse(),
                                                                                        goInFrontOfCube.reverse());


        }

    }

    private static List<Waypoint> flipX(List<Waypoint> waypoints)
    {
        List<Waypoint> result = new ArrayList<>(waypoints.size());

        for(Waypoint waypoint : waypoints)
        {
            result.add(new Waypoint(-waypoint.x, waypoint.y, waypoint.angle));
        }
        return result;
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
}
