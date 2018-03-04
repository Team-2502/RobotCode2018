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
            toReturn.add(new Waypoint(newLoc, waypoint.getMaxSpeed(), waypoint.getMaxAccel(), waypoint.getMaxDeccel(), waypoint.isForward(), waypoint.getCommands()));
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
                new Waypoint(new ImmutableVector2f(0, 0), 0, 10, -10),
                new Waypoint(new ImmutableVector2f(2F, 2.45F), 16, 10, -10),
                new Waypoint(new ImmutableVector2f(5F, 4F), 16, 10, -10),
                new Waypoint(new ImmutableVector2f(5F, 8.5F), 1F, 10, -10)
                                                                      );

        /**
         * Move from the center of the starting wall to the left side of the switch
         */
        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                new Waypoint(new ImmutableVector2f(-1.75F, 2.45F), 16, 5, -5),
                new Waypoint(new ImmutableVector2f(-5.25F, 4), 16, 5, -5),
                new Waypoint(new ImmutableVector2f(-5.25F, 9), 3F, 5, -5)
                                                                     );
    }

    /**
     * Organic gluten-free paths for when your robot is placed on the left
     */
    public static class Left
    {
        public static final List<Waypoint> leftScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 10, -5),
                new Waypoint(new ImmutableVector2f(0F, 8.589F), 20F, 10, -5),
                new Waypoint(new ImmutableVector2f(0F, 12.405F), 20F, 10, -5),
                new Waypoint(new ImmutableVector2f(0F, 16.405F), 7F, 10, -5, true, new RaiseElevatorScale()),
                new Waypoint(new ImmutableVector2f(2F, 20.5F), 0F, 10, -5) // max deceleration appears to be -7 ft / s^2
                                                                    );

        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                new Waypoint(new ImmutableVector2f(1.636F, 2.454F), 9F, 5, -5),
                new Waypoint(new ImmutableVector2f(3.82006F, 5.726F), 6F, 5, -5),
                new Waypoint(new ImmutableVector2f(3.82006F, 9.816F), 2F, 5, -5)
                                                                     );

        public static final List<Waypoint> rightScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                new Waypoint(new ImmutableVector2f(0F, 8.589F), 25F, 5, -5),
                new Waypoint(new ImmutableVector2f(0F, 17F), 25F, 5, -5),
                new Waypoint(new ImmutableVector2f(2.454F, 17.5F), 25F, 5, -5),
                new Waypoint(new ImmutableVector2f(19F, 17.5F), 3F, 5, -5, true, new RaiseElevatorScale(), new ActiveIntakeRotate(0.3, 0.5)),
                new Waypoint(new ImmutableVector2f(22F, 17.5F), 0F, 5, -5)
                                                                     );

    }

    /**
     * GMO-free soy-based paths for when your robot is placed on the right
     */
    public static class Right
    {
        public static final List<Waypoint> rightSwitch = flipY(Left.leftSwitch);

        public static final List<Waypoint> rightScale = flipY(Left.leftScale);

        public static final List<Waypoint> leftScale = flipY(Left.rightScale);

    }

}
