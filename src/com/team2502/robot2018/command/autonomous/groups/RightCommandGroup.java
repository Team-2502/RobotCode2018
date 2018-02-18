package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.DeadreckoningDrive;
import com.team2502.robot2018.command.autonomous.ingredients.RotateAutonStationary;
import com.team2502.robot2018.command.autonomous.ingredients.ActiveIntakeDown;
import com.team2502.robot2018.command.autonomous.ingredients.ElevatorUpAutonCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.command.autonomous.ingredients.ShootCubeCommand;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RightCommandGroup extends CommandGroup
{
    public RightCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        List<Waypoint> waypoints = new ArrayList<>();
        switch(AUTO_GAME_DATA)
        {
            case "LL":
                break;

            case "LR":
            {
                List<Waypoint> straightScale = Arrays.asList(
                        new Waypoint(new ImmutableVector2f(0, 0), 9),
                        new Waypoint(new ImmutableVector2f(0, 5), 9),
                        new Waypoint(new ImmutableVector2f(0, 7), 0.2F)
                                                             );
                addParallel(new ActiveIntakeDown(0.7, 0.5));
                addSequential(new PurePursuitCommand(straightScale, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                addSequential(new WaitCommand(0.8F));
                addSequential(new RotateAutonStationary(55));
                addSequential(new ElevatorUpAutonCommand(2.7F));
                addSequential(new DeadreckoningDrive(0.5F, 2));
//                        addSequential(new PurePursuitCommand(forward, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                addSequential(new ShootCubeCommand(1, 0.5F));
                break;
            }

            case "RL":

            case "RR":
            {
                List<Waypoint> straightSwitch = Arrays.asList(
                        new Waypoint(new ImmutableVector2f(0, 0), 6),
                        new Waypoint(new ImmutableVector2f(1.0F, 10.5F),9F),
                        new Waypoint(new ImmutableVector2f(1.0F, 22.5F), 9F),
                        new Waypoint(new ImmutableVector2f(1.0F, 26.5F), 2F)
                                                             );

                addParallel(new ActiveIntakeDown(0.7, 0.5));
                addSequential(new PurePursuitCommand(straightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                addSequential(new WaitCommand(0.8F));
                addSequential(new RotateAutonStationary(55));
                addSequential(new ElevatorUpAutonCommand(2.7F));
                addSequential(new DeadreckoningDrive(0.5F, 2));
//                        addSequential(new PurePursuitCommand(forward, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                addSequential(new ShootCubeCommand(1, 0.5F));
                break;
            }

            default:
                break;
        }
    }

}
