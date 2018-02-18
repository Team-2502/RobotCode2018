package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ActiveIntakeDown;
import com.team2502.robot2018.command.autonomous.ElevatorUpAutonCommand;
import com.team2502.robot2018.command.autonomous.PurePursuitCommand;
import com.team2502.robot2018.command.autonomous.ShootCubeCommand;
import com.team2502.robot2018.command.autonomous.ingredients.Paths;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.joml.ImmutableVector2f;

import java.util.Arrays;
import java.util.List;

public class CenterCommandGroup extends CommandGroup
{
    public CenterCommandGroup()
    {
        // Begin by calibrating the navX
        Robot.NAVX.reset();

        // Choose a path to take
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        switch(AUTO_GAME_DATA)
        {
            case "LL":
            {
                addSequential(new PurePursuitCommand(Paths.Center.leftSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                break;
            }

            case "LR":
            {
                addSequential(new PurePursuitCommand(Paths.Center.leftSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                break;
            }

            case "RL":
            {
                addSequential(new PurePursuitCommand(Paths.Center.rightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                break;
            }

            case "RR":
            {
                addSequential(new PurePursuitCommand(Paths.Center.rightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                break;
            }
        }

        addSequential(new ElevatorUpAutonCommand(1.1F));
        addSequential(new ActiveIntakeDown(0.35, 1));
        addSequential(new ShootCubeCommand(1));
    }

}
