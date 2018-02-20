package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.joml.ImmutableVector2f;

import java.util.Arrays;
import java.util.List;

public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        List<Waypoint> waypoints = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0,0),0),
                new Waypoint(new ImmutableVector2f(0,7),8),
                new Waypoint(new ImmutableVector2f(0,14),0)
                                                );
        Scheduler.getInstance().add(new PurePursuitCommand(waypoints, 3, 1.5F));
    }
}
