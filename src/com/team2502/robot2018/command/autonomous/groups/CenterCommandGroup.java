package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Robot.autonStrategySelector;


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

            if(autonStrategySelector.getSelected().equals(AutonStrategy.DANGEROUS))
            {
                secondCubeLeft();
            }
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
        emitCubeSwitch();
    }

    private void goSwitchRight()
    {
        moveElevator();
        addSequential(new PurePursuitCommand(Paths.Center.rightSwitch));
        emitCubeSwitch();
    }

    private void secondCubeLeft()
    {
        addParallel(new ElevatorAutonCommand(1, 0));
        addSequential(new FastRotateCommand(75, 8, -0.2F));

        addSequential(new EncoderDrive(3, 4));
        addParallel(new ShootCubeCommand(3, -1));

        addSequential(new EncoderDrive(-3, 4));
        moveElevator();

        addSequential(new FastRotateCommand(0, 8, -0.2F));
        addSequential(new DeadreckoningDrive(1, 8));
        emitCubeSwitch();

    }

    private void moveElevator()
    {
        addParallel(new RaiseElevatorSwitch());
    }

    private void emitCubeSwitch()
    {
//        addSequential(new ActiveIntakeRotate(0.4, 0.7));
        addSequential(new ToggleIntakeCommand());
        addSequential(new ShootCubeCommand(1));
    }

}
