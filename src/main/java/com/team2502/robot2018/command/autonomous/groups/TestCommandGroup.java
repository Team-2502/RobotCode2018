package com.team2502.robot2018.command.autonomous.groups;


import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.FastRotateCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.SRXProfilingCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.TrajConfig;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Constants.SRXProfiling.NO_COMMANDS;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        Robot.writeLog("TestCommand", 200);
//        testRotateScaleToSwitch();
//        Robot.resetLocalization();
//        testPurePursuitForwardBackScaleSwitch();
        testMotionProfiling();
    }

    private void testMotionProfiling()
    {
        addSequential(new SRXProfilingCommand(NO_COMMANDS
        , 1,
                                              TrajConfig.testTraj
        ));
    }


    private void testRotation()
    {
        addSequential(new FastRotateCommand(0,8,-0.4F)); // rotate 0 degrees (shouldn't move anywhere)
        addSequential(new FastRotateCommand(90,8,-0.4F)); // rotate 90 degrees clockwise
    }

    private void testRotateScaleToSwitch()
    {
        addSequential(new FastRotateCommand(92,8,-0.4F));
    }

    // test going forward from scale to switch in auton after a rotation
    private void testPurePursuitForwardBackScaleSwitch()
    {
        PurePursuitCommand back = new PurePursuitCommand.Builder()
                .addWaypoint(0, 0, 8)
                .addWaypoint(0, 4.0F, 0)
                .setForward(true)
                .build();

        PurePursuitCommand forward = new PurePursuitCommand.Builder()
                .addWaypoint(0, 4.0F, 8)
                .addWaypoint(0, 0.8F, 0)
                .setForward(false)
                .build();

        // move back
        addSequential(back);

        // move forward
        addSequential(forward);

    }
}
