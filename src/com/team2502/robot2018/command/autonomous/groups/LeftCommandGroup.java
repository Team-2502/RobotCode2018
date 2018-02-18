package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.*;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.joml.ImmutableVector2f;

import java.util.Arrays;
import java.util.List;

public class LeftCommandGroup extends CommandGroup
{



    public LeftCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        switch(AUTO_GAME_DATA)
        {
            case "LL":
            {
                switch(Robot.AUTON_STRATEGY)
                {
                    case SCALE: // WARNING!!! the active needs to be half way down so it will not get caught
                    {
                        List<Waypoint> straightSwitch = Arrays.asList(
                                new Waypoint(new ImmutableVector2f(0, 0), 6),
                                new Waypoint(new ImmutableVector2f(-1.0F, 10.5F),9F),
                                new Waypoint(new ImmutableVector2f(-1.0F, 22.5F), 9F),
                                new Waypoint(new ImmutableVector2f(-1.0F, 26.5F), 2F)
                                                                     );
                        Robot.NAVX.reset();

                        addParallel(new ActiveIntakeDown(0.7, 0.5));
                        addSequential(new PurePursuitCommand(straightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                        addSequential(new WaitCommand(0.8F));
                        addSequential(new RotateAutonStationary(45));
                        addSequential(new ElevatorUpAutonCommand(2.7F));
                        addSequential(new DeadreckoningDrive(1,3));
//                        addSequential(new PurePursuitCommand(forward, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                        addSequential(new ShootCubeCommand(1,0.5F));
                        break;
                    }
                    case SWITCH:
                    {
                        List<Waypoint> straightSwitch = Arrays.asList(
                                new Waypoint(new ImmutableVector2f(0, 0), 6),
                                new Waypoint(new ImmutableVector2f(2F, 3), 9F),
                                new Waypoint(new ImmutableVector2f(4.67F, 7), 6F),
                                new Waypoint(new ImmutableVector2f(4.67F, 12), 2F)
                                                                     );
                        Robot.NAVX.reset();

                        addSequential(new PurePursuitCommand(straightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                        addSequential(new ElevatorUpAutonCommand(.8F));
                        addSequential(new ActiveIntakeDown(0.35, 1));
                        addSequential(new ShootCubeCommand(1));
                        break;
                    }
                }
                break;
            }

            case "LR":
            {
                List<Waypoint> straightSwitch = Arrays.asList(
                        new Waypoint(new ImmutableVector2f(0, 0), 6),
                        new Waypoint(new ImmutableVector2f(2F, 3), 9F),
                        new Waypoint(new ImmutableVector2f(4.67F, 7), 6F),
                        new Waypoint(new ImmutableVector2f(4.67F, 12), 2F)
                                                             );
                Robot.NAVX.reset();

                addSequential(new PurePursuitCommand(straightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                addSequential(new ElevatorUpAutonCommand(.8F));
                addSequential(new ActiveIntakeDown(0.35, 1));
                addSequential(new ShootCubeCommand(1));
                break;
            }

            case "RL":
                break;

            case "RR":
                break;

            default:
                break;
        }
    }

}
