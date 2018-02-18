package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;

import com.team2502.robot2018.command.autonomous.ingredients.ActiveIntakeDown;
import com.team2502.robot2018.command.autonomous.ingredients.ElevatorUpAutonCommand;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import com.team2502.robot2018.command.autonomous.ingredients.ShootCubeCommand;

import com.team2502.robot2018.command.autonomous.ingredients.Paths;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class CenterCommandGroup extends CommandGroup
{
    public CenterCommandGroup()
    {
        // Begin by calibrating the navX
        Robot.NAVX.reset();

        // Choose a path to take
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        if(AUTO_GAME_DATA.charAt(0) == 'L')
        {
            goSwitchLeft();
        }

        else if(AUTO_GAME_DATA.charAt(0) == 'R')
        {
            goSwitchRight();
        }


    }

    private void goSwitchLeft()
    {
        addSequential(new PurePursuitCommand(Paths.Center.leftSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
        emitCube();
    }

    private void goSwitchRight()
    {
        addSequential(new PurePursuitCommand(Paths.Center.rightSwitch, Constants.LOOKAHEAD_DISTANCE_FT, Constants.STOP_DIST_TOLERANCE_FT));
        emitCube();
    }

    private void emitCube()
    {
        addSequential(new ElevatorUpAutonCommand(.8F));
        addSequential(new ActiveIntakeDown(0.35, 1));
        addSequential(new ShootCubeCommand(1));
    }

}
