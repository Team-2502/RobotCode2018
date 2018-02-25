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
        addSequential(new PurePursuitCommand(Paths.Center.leftSwitch));
        emitCube();
    }

    private void goSwitchRight()
    {
        moveElevator();
        addSequential(new PurePursuitCommand(Paths.Center.rightSwitch));
        emitCube();
    }

    private void moveElevator()
    {
        addParallel(new RaiseElevatorSwitch());
    }

    private void emitCube()
    {
        addSequential(new ActiveIntakeRotate(0.35, 1));
        addSequential(new ShootCubeCommand(1));
    }

}
