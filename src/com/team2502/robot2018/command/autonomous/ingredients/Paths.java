package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.trajectory.Waypoint;
import org.joml.ImmutableVector2f;

import java.util.Arrays;
import java.util.List;

/**
 * Like constants, but for autonomous paths.
 *
 * <br>
 *
 * If you want, you can graph these on Desmos to see where they go.
 */
public class Paths
{
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
}
