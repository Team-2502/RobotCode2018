package com.team2502.robot2018.command.autonomous.groups;


import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.FastRotateCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        Robot.writeLog("TestCommand",200);

        PurePursuitCommand back = new PurePursuitCommand.Builder()
                .addWaypoint(0, 0, 8)
                .addWaypoint(0, -3, 0)
                .setForward(false)
                .build();

        PurePursuitCommand forward = new PurePursuitCommand.Builder()
                .addWaypoint(0, -3, 8)
                .addWaypoint(0, 0, 0)
                .build();

        addSequential(back);
        addSequential(forward);
    }
}
