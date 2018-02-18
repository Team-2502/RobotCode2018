package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LeftCommandGroup extends CommandGroup
{


    public LeftCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        Robot.NAVX.reset();


        switch(AUTO_GAME_DATA)
        {
            case "LL":
            {
                switch(Robot.AUTON_STRATEGY)
                {
                    case SCALE: // WARNING!!! the active needs to be half way down so it will not get caught
                    {
                        addParallel(new ActiveIntakeDown(0.7, 0.5));
                        addSequential(new PurePursuitCommand(Paths.Left.leftScale, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                        addSequential(new WaitCommand(0.8F));
                        addSequential(new RotateAutonStationary(55));
                        addSequential(new ElevatorUpAutonCommand(2.7F));
                        addSequential(new DeadreckoningDrive(0.5F, 2));

                        break;
                    }
                    case SWITCH:
                    {
                        addSequential(new PurePursuitCommand(Paths.Left.leftSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                        addSequential(new ElevatorUpAutonCommand(.8F));
                        addSequential(new ActiveIntakeDown(0.35, 1));

                        break;
                    }
                }
                break;
            }

            case "LR":
            {
                addSequential(new PurePursuitCommand(Paths.Left.leftSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
                addSequential(new ElevatorUpAutonCommand(.8F));
                addSequential(new ActiveIntakeDown(0.35, 1));
                break;
            }

            case "RL":
                break;

            case "RR":
                break;

            default:
                break;
        }
        addSequential(new ShootCubeCommand(1));
    }

}
