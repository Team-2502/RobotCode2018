package com.team2502.robot2018.command.autonomous.ingredients;


import com.team2502.robot2018.Change;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Like constants, but for autonomous paths.
 *
 * <br>
 * <p>
 * If you want, you can graph these on Desmos to see where they go.
 */
public class PathConfig
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
        for(List<Waypoint> path : paths)
        {
            toReturn.addAll(path);
        }
        return toReturn;
    }

    /**
     * Hand-made artisan paths for when your robot is placed in the center
     */
    public static class Center
    {

        /**
         * Move from the center of the starting wall to the left side of the switch
         */
        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                new Waypoint(new ImmutableVector2f(0, 2F), 16, 5, -5),
                new Waypoint(new ImmutableVector2f(-4.5F, 4), 16, 5, -5),
                new Waypoint(new ImmutableVector2f(-4.5F, 10), 0F, 5, -5)
                                                                     );
        //TODO: Make speedLimiter 1.0F
        private static final float speedLimiter = 0.3F;
        /**
         * Move from the center of the starting wall to the right side of the switch
         */
        public static final List<Waypoint> rightSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 1, 10, -10),
                new Waypoint(new ImmutableVector2f(2F, 2.45F), 16 * speedLimiter, 10, -10),
                new Waypoint(new ImmutableVector2f(3.5F, 4F), 16 * speedLimiter, 10, -10),
                new Waypoint(new ImmutableVector2f(3.5F, 9F), 1F * speedLimiter, 10, -10) // if this doesn't work, PP is broken or field is off.
                                                                      );
    }

    /**
     * Organic gluten-free paths for when your robot is placed on the left
     */
    public static class Left
    {
        @Change(reason = "uncomment out RaiseElevatorScale()")
        public static final List<Waypoint> leftScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 16, 20, -12),
                new Waypoint(new ImmutableVector2f(0, 4), 16, 20, -12),
                new Waypoint(new ImmutableVector2f(-0.5F, 8.589F), 16F, 20, -12),
                new Waypoint(new ImmutableVector2f(-0.5F, 12.405F), 16F, 20, -12),
                new Waypoint(new ImmutableVector2f(-0.5F, 17F), 16F, 20, -12),
                new Waypoint(new ImmutableVector2f(1.5F, 19.4F), 0F, 20, -12) // max deceleration appears to be -7 ft / s^2
                                                                    );

        @Change(reason = "help")
        public static final List<Waypoint> leftScaleToSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(1.5F, 19.4F), 8F, 20, -7),
//                new Waypoint(new ImmutableVector2f(1F, 21.5F), 4F, 20, -9), // max deceleration appears to be -7 ft / s^2
                new Waypoint(new ImmutableVector2f(4F, 20.0F), 8F, 10, -9, new IntakeAndRaise()),
                new Waypoint(new ImmutableVector2f(6F, 16F), 8F, 10, -9), // 3rd cube 7 19
                new Waypoint(new ImmutableVector2f(4.5F, 14.9F), 8F, 10, -9),
                new Waypoint(new ImmutableVector2f(2.42F, 15F), 8F, 10, -9), // we should have the cube
                new Waypoint(new ImmutableVector2f(3.5F, 19F), 0F, 10, -9)
                                                                            );

        public static final List<Waypoint> leftSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                new Waypoint(new ImmutableVector2f(0, 2.454F), 9F, 5, -5),
                new Waypoint(new ImmutableVector2f(0, 9), 6F, 5, -5),
                new Waypoint(new ImmutableVector2f(3.82006F, 12.5F), 2F, 5, -5) // TODO: add ability to coast at end
                                                                     );

        public static final List<Waypoint> rightScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 20, -10),
                new Waypoint(new ImmutableVector2f(0F, 8.589F), 25F, 20, -10),
                new Waypoint(new ImmutableVector2f(0F, 17.0F), 25F, 20, -10),
                new Waypoint(new ImmutableVector2f(2.454F, 17.5F), 25F, 20, -10),
                new Waypoint(new ImmutableVector2f(15.0F, 17.5F), 3F, 20, -5, new RaiseElevatorScale(), new ActiveIntakeRotate(0.3, 0.5)),
                new Waypoint(new ImmutableVector2f(20.83F, 17.5F), 3F, 20, -5),
                new Waypoint(new ImmutableVector2f(16.833F, 22F), 0F, 10, -5)
                                                                     );
        public static final List<Waypoint> leftScaleDeepNullZone = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0, 20, -9),
                new Waypoint(new ImmutableVector2f(-0.5F, 8.589F), 16F, 20, -7),
                new Waypoint(new ImmutableVector2f(-0.5F, 12.405F), 15F, 20, -7, new RaiseElevatorScale()),
                new Waypoint(new ImmutableVector2f(-0.5F, 20.5F), 5F, 20, -7),
                new Waypoint(new ImmutableVector2f(-1.5F, 25F), 3F, 20, -7),
                new Waypoint(new ImmutableVector2f(1.5F, 25.375F), 0F, 20, -7)
                                                                                );
        public static List<Waypoint> leftSwitchToScale = Arrays.asList(
                new Waypoint(new ImmutableVector2f(3.5F, 19F), 8, 20, -7),
                new Waypoint(new ImmutableVector2f(2, 20.0F), 8, 20, -7),
                new Waypoint(new ImmutableVector2f(5, 21F), 0, 20, -7)
                                                                      );

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

        public static final List<Waypoint> rightScaleDeepNullZone = flipY(Left.leftScaleDeepNullZone);

    }

}
