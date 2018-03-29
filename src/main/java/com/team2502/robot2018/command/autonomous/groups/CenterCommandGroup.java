package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
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
        moveElevator();
        addSequential(new PurePursuitCommand(PathConfig.Center.leftSwitch, true));
        emitCubeSwitch();
        addSequential(new BackOffCommand());
    }

    private void goSwitchRight()
    {
        moveElevator();
        addSequential(new PurePursuitCommand(PathConfig.Center.rightSwitch, true));
        emitCubeSwitch();
        addSequential(new BackOffCommand());
    }

    private void moveElevator()
    {
        addParallel(new RaiseElevatorSwitch());
        addParallel(new ActiveIntakeRotate(0.35, 1));
    }

    private void emitCubeSwitch()
    {
        addSequential(new RunIntakeCommand(1));
    }

}
