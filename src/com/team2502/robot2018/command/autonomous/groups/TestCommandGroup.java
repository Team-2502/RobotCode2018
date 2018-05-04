package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

import java.util.Arrays;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        addSequential(new RaiseElevatorScale());
        addParallel(new WaitCommand(3));
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(5.0, -5.0F));
    }

    private void testElevatorScale()
    {
        addSequential(new RaiseElevatorScale());
    }

    private void testNonPPSideSwitch() // much jank
    {
        addParallel(new RaiseElevatorSwitch());
//        addSequential(new EncoderDrive(-12.5, 4));
        addSequential(new PurePursuitCommand(
                Arrays.asList(new Waypoint(new ImmutableVector2f(0, 0), 0, 5, -5),
                              new Waypoint(new ImmutableVector2f(0, (float) SmartDashboard.getNumber("Side switch distance",13.5)), 6, 5, -5)
                      )));
        addSequential(new FastRotateCommand(((float) SmartDashboard.getNumber("Side switch tuning: angle", 80)), 5, -0.4F));
        addSequential(new DeadreckoningDrive(1.5, 4));
        addSequential(new ActiveIntakeRotate(0.4, 1));

        addParallel(new ShootCubeCommand(3));
        addSequential(new WaitCommand(1));
        addSequential(new ToggleIntakeCommand());
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
