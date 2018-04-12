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


        addSequential(new PurePursuitCommand(
                Arrays.asList(
                        new Waypoint(new ImmutableVector2f(0,0),10,5,-8),
                        new Waypoint(new ImmutableVector2f(-3,-3),10,5,-8),
                        new Waypoint(new ImmutableVector2f(-3,-5),0,5,-8)
                             ),false,false));

        addSequential(new PurePursuitCommand(
                Arrays.asList(
                        new Waypoint(new ImmutableVector2f(-3,-5),10,5,-8),
                        new Waypoint(new ImmutableVector2f(-3,0),0,5,-8)
                             ),false,true));
    }
}
