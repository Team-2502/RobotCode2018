package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RightCommandGroup extends CommandGroup
{
    public RightCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        List<Waypoint> waypoints = new ArrayList<>();
        switch(AUTO_GAME_DATA)
        {
            case "LL":
                break;

            case "LR":
                break;

            case "RL":

                waypoints = Arrays.asList(
                        new Waypoint(new ImmutableVector2f(0, 0), 9),
                        new Waypoint(new ImmutableVector2f(0, 5), 9),
                        new Waypoint(new ImmutableVector2f(0, 7), 0.2F)
                                         );

                break;
            case "RR":

                waypoints = Arrays.asList(
                        new Waypoint(new ImmutableVector2f(0, 0), 9),
                        new Waypoint(new ImmutableVector2f(0, 5), 9),
                        new Waypoint(new ImmutableVector2f(0, 7), 0.2F)
                                         );


                break;

            default:
                break;
        }
    }

}
