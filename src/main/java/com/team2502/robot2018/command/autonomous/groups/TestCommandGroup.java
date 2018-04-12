package com.team2502.robot2018.command.autonomous.groups;


import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.FastRotateCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        Robot.writeLog("TestCommand",200);

        System.out.println("wrong = " + "wrong");
//        addSequential(new FastRotateCommand(0, 8 ,-0.4F));
//        addSequential(new WaitCommand(3));
//        addSequential(new FastRotateCommand(180, 8, -0.4F));
//        addSequential(new WaitCommand(3));
//        addSequential(new FastRotateCommand(270, 8, -0.4F));
//        addSequential(new WaitCommand(3));
//        addSequential(new FastRotateCommand(0, 8, -0.4F));
//        addSequential(new WaitCommand(3));
//        addSequential(new FastRotateCommand(315, 8, -0.4F));
//        addSequential(new PurePursuitCommand(Arrays.asList(
//                new Waypoint(new ImmutableVector2f(0,0),0,5,-5),
//        new Waypoint(new ImmutableVector2f(-5,-5),5,5,-5),
//        new Waypoint(new ImmutableVector2f(-10,-10),0,5,-5)
//                                                          )
//        ,false,false));
    }
}
