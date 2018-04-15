package com.team2502.robot2018.command.autonomous.groups;


import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.FastRotateCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        Robot.writeLog("TestCommand", 200);
        testRotation();
    }

    public void testRotation()
    {
        addSequential(new FastRotateCommand(0,8,-0.4F));
        addSequential(new FastRotateCommand(90,8,-0.4F));
    }

    public void testPurePursuitBackForth()
    {
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
