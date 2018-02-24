package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.ActiveIntakeMove;
import com.team2502.robot2018.command.autonomous.ingredients.ElevatorAutonCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.command.autonomous.ingredients.ShootCubeCommand;
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
                new Waypoint(new ImmutableVector2f(0, 0), 6),
                new Waypoint(new ImmutableVector2f(-3.95F, 4), 9),
                new Waypoint(new ImmutableVector2f(-6.2F, 7), 6),
                new Waypoint(new ImmutableVector2f(-6.2F, 12), 2F)
                                                     );
        Robot.NAVX.reset();

        addSequential(new PurePursuitCommand(straightSwitch));
        addSequential(new ElevatorAutonCommand(1.1F));
        addSequential(new ActiveIntakeMove(0.35, 1));
        addSequential(new ShootCubeCommand(1));
    }
}
