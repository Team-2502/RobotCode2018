package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ActiveIntakeDown;
import com.team2502.robot2018.command.autonomous.ElevatorUpAutonCommand;
import com.team2502.robot2018.command.autonomous.PurePursuitCommand;
import com.team2502.robot2018.command.autonomous.ShootCubeCommand;
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
//        Scheduler.getInstance().add(new CalibrateRobotCommand());
        addSequential(new PurePursuitCommand(straightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
        addSequential(new ElevatorUpAutonCommand(1.1F));
        addSequential(new ActiveIntakeDown(0.35, 1));
        addSequential(new ShootCubeCommand(1));
    }
}
