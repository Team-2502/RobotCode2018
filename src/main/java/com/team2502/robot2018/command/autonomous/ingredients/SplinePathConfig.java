package com.team2502.robot2018.command.autonomous.ingredients;


import com.team2502.robot2018.pathplanning.purepursuit.Path;
import com.team2502.robot2018.pathplanning.purepursuit.SplineWaypoint;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Like constants, but for autonomous paths.
 *
 * <br>
 * <p>
 * If you want, you can graph these on Desmos to see where they go.
 */
public class SplinePathConfig
{

    private static List<Waypoint> flipY(List<Waypoint> waypoints)
    {
        List<Waypoint> toReturn = new ArrayList<>();
        for(Waypoint waypoint : waypoints)
        {
            ImmutableVector2f oldLoc = waypoint.getLocation();
            ImmutableVector2f newLoc = new ImmutableVector2f(-oldLoc.x, oldLoc.y);
            toReturn.add(new Waypoint(newLoc, waypoint.getMaxSpeed(), waypoint.getMaxAccel(), waypoint.getMaxDeccel(), waypoint.getCommands()));
        }
        return toReturn;
    }

    private static List<Waypoint> reverse(List<Waypoint> waypoints)
    {
        List<Waypoint> toReturn = new ArrayList<>(waypoints);
        Collections.reverse(toReturn);
        return toReturn;
    }

    public static List<Waypoint> combinePaths(List<Waypoint>... paths)
    {
        List<Waypoint> toReturn = new ArrayList<>();
        Waypoint lastPoint = null;
        for(List<Waypoint> path : paths)
        {
            for(Waypoint point : path)
            {
                // if this is the first point or if the last point is in the same spot
                if(lastPoint == null || !point.getLocation().equals(lastPoint.getLocation()))
                {
                    toReturn.add(point);
                }

                lastPoint = point;

            }
        }
        return toReturn;
    }

    /**
     * Hand-made artisan paths for when your robot is placed in the center
     */
    public static class Center
    {

        private static final Path splineLeftSwitch = Path.fromSplinePoints(
                new SplineWaypoint(new ImmutableVector2f(0, 0), 0, 16, 10, -10),
                new SplineWaypoint(new ImmutableVector2f(-4.5F, 10F), 0, 16F, 10, -10) // if this doesn't work, PP is broken or field is off.
                                                                          );

        private static final Path splineRightSwitch = Path.fromSplinePoints(
                new SplineWaypoint(new ImmutableVector2f(0, 0), 0, 16, 10, -10),
                new SplineWaypoint(new ImmutableVector2f(3.5F, 10F), 0, 16F, 10, -10) // if this doesn't work, PP is broken or field is off.
                                                                           );
        /**
         * Move from the center of the starting wall to the right side of the switch
         */
        public static final List<Waypoint> rightSwitch = splineRightSwitch.getWaypoints();

        /**
         * Move from the center of the starting wall to the left side of the switch
         */
        public static final List<Waypoint> leftSwitch = splineLeftSwitch.getWaypoints();
    }

    /**
     * Organic gluten-free paths for when your robot is placed on the left
     */
    public static class Left
    {
        private static final Path splineLeftToLeftScale = Path.fromSplinePoints(
                new SplineWaypoint(new ImmutableVector2f(0.000F, 0.000F), new ImmutableVector2f(0.000F, 1.000F), 16, 20, -12),
                new SplineWaypoint(new ImmutableVector2f(0.000F, 10.000F), new ImmutableVector2f(0.000F, 5.000F), 16, 20, -12),
                new SplineWaypoint(new ImmutableVector2f(2.000F, 27.000F), new ImmutableVector2f(15.000F, 0.000F), 8, 8, -5));

        private static final Path splineLeftToRightScale = Path.fromSplinePoints(new SplineWaypoint(new ImmutableVector2f(0.000F, 0.000F), new ImmutableVector2f(0.000F, 0.000F), 0, 20, -10),
                                                                                 new SplineWaypoint(new ImmutableVector2f(0.000F, 12.000F), new ImmutableVector2f(0.000F, 25.000F), 25, 20, -10),
                                                                                 new SplineWaypoint(new ImmutableVector2f(15.000F, 19.500F), new ImmutableVector2f(25.000F, 0.000F), 10, 20, -5),
                                                                                 new SplineWaypoint(new ImmutableVector2f(19.000F, 26.500F), new ImmutableVector2f(-8.000F, 0.000F), 2, 10, -5));

        private static final Path splineLeftToLeftSwitch = Path.fromSplinePoints(
                new SplineWaypoint(new ImmutableVector2f(0.000F, 0.000F), new ImmutableVector2f(0.000F, 10.000F), 16, 20, -12),
                new SplineWaypoint(new ImmutableVector2f(3.820F, 14.000F), new ImmutableVector2f(15.000F, 0.000F), 16, 20, -12)
                                                                                );

        public static final List<Waypoint> leftScale = splineLeftToLeftScale.getWaypoints();
        public static final List<Waypoint> rightScale = splineLeftToRightScale.getWaypoints();
        public static final List<Waypoint> leftSwitch = splineLeftToLeftSwitch.getWaypoints();

    }

    /**
     * GMO-free soy-based paths for when your robot is placed on the right (Ritik)
     * <p>
     * These seem pretty processed to me... (Isaac)
     */
    public static class Right
    {
        public static final List<Waypoint> rightSwitch = flipY(Left.leftSwitch);

        public static final List<Waypoint> rightScale = flipY(Left.leftScale);

        public static final List<Waypoint> leftScale = flipY(Left.rightScale);

    }

}
