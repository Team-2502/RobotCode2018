package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
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
        addSequential(new ElevatorAutonCommand(10, 2F));
    }
}
