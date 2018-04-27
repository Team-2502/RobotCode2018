package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
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
        testNonPPSideSwitch();
    }

    private void testElevatorScale()
    {
        addSequential(new RaiseElevatorScale());
    }

    private void testNonPPSideSwitch() // much jank
    {
        addParallel(new RaiseElevatorSwitch());
        addSequential(new EncoderDrive(12.5, 4));
        addSequential(new FastRotateCommand(80, 8, -0.4F)); //80 ==> 90 because of overshoot
        addSequential(new EncoderDrive(3, 3));
        addSequential(new ShootCubeCommand(2,.7));
    }

    private void testEmitCubeSwitch()
    {
//        addSequential(new ActiveIntakeRotate(0.4, 0.7));

        // Shoot cube while waiting for the "main thread"
        addParallel(new ShootCubeCommand(3));
        addSequential(new WaitCommand(1));
        addSequential(new ToggleIntakeCommand());
    }

    private void testLefttoScaleRight()
    {
//        addSequential(new PurePursuitCommand(Paths.Left.rightScale));

        addParallel(new ActiveIntakeRotate(1F, 0.5));

        addSequential(new ShootCubeCommand(1, .5F));

//        addSequential(new DeadreckoningDrive(0.7F, -4F));
        addSequential(new ElevatorAutonCommand(2.5, 0));

    }
}
