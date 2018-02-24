package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.joml.ImmutableVector2f;

import java.util.Arrays;
import java.util.List;

public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        List<Waypoint> straightSwitch = Arrays.asList(
                new Waypoint(new ImmutableVector2f(0, 0), 0),
                new Waypoint(new ImmutableVector2f(0, 15), 9),
                new Waypoint(new ImmutableVector2f(0, 30), 0)
                                                     );
        Robot.NAVX.reset();

        addSequential(new PurePursuitCommand(straightSwitch));
    }
}
