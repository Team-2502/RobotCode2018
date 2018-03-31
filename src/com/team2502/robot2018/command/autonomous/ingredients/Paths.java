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
                new Waypoint(new ImmutableVector2f(4F, 4F), 16, 10, -10),
                new Waypoint(new ImmutableVector2f(4F, 9.5F), 1F, 10, -10)
                                                                      );

        /**
         * Move from the center of the starting wall to the left side of the switch
         */
        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                new Waypoint(new ImmutableVector2f(0, 2F), 16, 5, -5),
                new Waypoint(new ImmutableVector2f(-4.5F, 4), 16, 5, -5),
                new Waypoint(new ImmutableVector2f(-4.5F, 10), 1F, 5, -5)
                                                                     );
    }

    /**
     * Organic gluten-free paths for when your robot is placed on the left
     */
    public static class Left
    {
        public static final List<Waypoint> leftScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 20, -9),
                new Waypoint(new ImmutableVector2f(-0.5F, 8.589F), 16F, 20, -7, true),
                new Waypoint(new ImmutableVector2f(-0.5F, 12.405F), 5F, 20, -7, true, new RaiseElevatorScale()),
                new Waypoint(new ImmutableVector2f(-0.5F, 20.5F), 3F, 20, -7, true),
                new Waypoint(new ImmutableVector2f(2.3F, 23.5F), 0F, 20, -7) // max deceleration appears to be -7 ft / s^2
                                                                    );

        public static final List<Waypoint> leftScaleToSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(1.5F, 22.0F), 0F, 20, -7),
                new Waypoint(new ImmutableVector2f(4F, 21.2F), 0F, 20, -9), // max deceleration appears to be -7 ft / s^2
                new Waypoint(new ImmutableVector2f(10F, 15F), 3F, 10, -9)
                                                                            );
        //TODO: try navX calibration when auto begins ... generally 
        public static final List<Waypoint> leftSwitch = Arrays.asList( //TODO need to fix as was changed because I (Andrew) thought this was the path we were tweaking
                                                                       new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                                                                       new Waypoint(new ImmutableVector2f(1.636F, 2.454F), 9F, 5, -5),
                                                                       new Waypoint(new ImmutableVector2f(3.82006F, 5.726F), 6F, 5, -5),
                                                                       new Waypoint(new ImmutableVector2f(3.82006F, 10.5F), 2F, 5, -5) // TODO: add ability to coast at end
                                                                     );

        public static final List<Waypoint> rightScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 20, -10),
                new Waypoint(new ImmutableVector2f(0F, 8.589F), 25F, 20, -10),
                new Waypoint(new ImmutableVector2f(0F, 17.0F), 25F, 20, -10),
                new Waypoint(new ImmutableVector2f(2.454F, 17.5F), 25F, 20, -10),
                new Waypoint(new ImmutableVector2f(15.0F, 17.5F), 3F, 20, -5, true, new RaiseElevatorScale(), new ActiveIntakeRotate(0.3, 0.5)),
                new Waypoint(new ImmutableVector2f(21F - 0.1666666667F, 17.5F), 3F, 20, -5),
                new Waypoint(new ImmutableVector2f(17F - 0.1666666667F, 22F), 0F, 10, -5)
                                                                     );

        public static final List<Waypoint> leftScaleDeepNullZone = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 20, -10),
                new Waypoint(new ImmutableVector2f(-0.5F, 11.589F), 16F, 20, -7, true),
                new Waypoint(new ImmutableVector2f(-0.5F, 15.405F), 5F, 20, -7, true, new RaiseElevatorScale()),
                new Waypoint(new ImmutableVector2f(-0.5F, 23.5F), 3F, 20, -7, true),
                new Waypoint(new ImmutableVector2f(2.3F, 26.5F), 0F, 20, -7)
                                                                                );

    }

    /**
     * GMO-free soy-based paths for when your robot is placed on the right (Andrew? Ritik?)
     * <p>
     * These seem pretty processed to me... (Isaac)
     */
    public static class Right
    {
        public static final List<Waypoint> rightSwitch = flipY(Left.leftSwitch);

        public static final List<Waypoint> rightScale = flipY(Left.leftScale);

        public static final List<Waypoint> leftScale = flipY(Left.rightScale);

        public static final List<Waypoint> rightScaleDeepNullZone = flipY(Left.leftScaleDeepNullZone);

    }

}
