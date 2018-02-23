package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.trajectory.Waypoint;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Like constants, but for autonomous paths.
 * <p>
 * <br>
 * <p>
 * If you want, you can graph these on Desmos to see where they go.
 */
public class Paths
{
    private static List<Waypoint> flipY(List<Waypoint> waypoints)
    {
        List<Waypoint> toReturn = new ArrayList<>();
        for(Waypoint waypoint : waypoints)
        {
            ImmutableVector2f oldLoc = waypoint.getLocation();
            ImmutableVector2f newLoc = new ImmutableVector2f(-oldLoc.x, oldLoc.y);
            toReturn.add(new Waypoint(newLoc, waypoint.getMaxSpeed(), waypoint.isForward()));
        }
        return toReturn;
    }

    /**
     * Hand-made artisan paths for when your robot is placed in the center
     */
    public static class Center
    {
        /**
         * Move from the center of the starting wall to the right side of the switch
         */
        public static final List<Waypoint> rightSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 6),
                new Waypoint(new ImmutableVector2f(2, 3), 9),
                new Waypoint(new ImmutableVector2f(4.42F, 7), 6),
                new Waypoint(new ImmutableVector2f(4.42F, 12), 2F)
                                                                      );
        /**
         * Move from the center of the starting wall to the left side of the switch
         */
        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 6),
                new Waypoint(new ImmutableVector2f(-3.95F, 4), 9),
                new Waypoint(new ImmutableVector2f(-6.2F, 7), 6),
                new Waypoint(new ImmutableVector2f(-6.2F, 12), 2F)
                                                                     );
    }

    /**
     * Organic gluten-free paths for when your robot is placed on the left
     */
    public static class Left
    {
        public static final List<Waypoint> leftScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 6),
                new Waypoint(new ImmutableVector2f(-1.0F, 10.5F), 9F),
                new Waypoint(new ImmutableVector2f(-1.0F, 22.5F), 9F),
                new Waypoint(new ImmutableVector2f(-1.0F, 26.5F), 2F)
                                                                    );

        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 6),
                new Waypoint(new ImmutableVector2f(2F, 3), 9F),
                new Waypoint(new ImmutableVector2f(4.67F, 7), 6F),
                new Waypoint(new ImmutableVector2f(4.67F, 12), 2F)
                                                                     );

    }

    /**
     * GMO-free soy-based paths for when your robot is placed on the right
     */
    public static class Right
    {
        public static final List<Waypoint> rightSwitch = flipY(Left.leftSwitch);

        public static final List<Waypoint> rightScale = flipY(Left.leftScale);

    }

}
