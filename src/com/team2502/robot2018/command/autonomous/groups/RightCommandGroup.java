package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class RightCommandGroup extends CommandGroup
{
    public RightCommandGroup()
    {
        Robot.NAVX.reset();

        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        switch(AUTO_GAME_DATA)
        {
            case "LL":
                crossLine();
                break;

            case "LR":
                goScaleRight();
                break;

            case "RL":
                goSwitch();
                break;

            case "RR":
                switch(Robot.autonStrategySelector.getSelected())
                {
                    case SCALE:
                        goScaleRight();
                        break;
                    case SWITCH:
                        goSwitch();
                        break;
                }
                break;

        }

    }

    private void goSwitch()
    {
        addSequential(new PurePursuitCommand(Paths.Right.rightSwitch));
        addSequential(new ElevatorAutonCommand(.8F));
        addSequential(new ActiveIntakeMove(0.35, 1));

        emitCube();
    }

    private void goScaleRight()
    {
        // TODO: Move things into constants
        addParallel(new ActiveIntakeMove(0.7, 0.5));
        addSequential(new PurePursuitCommand(Paths.Right.rightScale));
        addSequential(new WaitCommand(0.8F));
        addSequential(new RotateAutonStationary(-55));
        addSequential(new ElevatorAutonCommand(2.7F));
        addSequential(new DeadreckoningDrive(0.5F, 2));
        emitCube();
    }

    private void crossLine()
    {
        addSequential(new DriveTime(7, 0.4F));

    }

    private void emitCube()
    {
        addSequential(new ShootCubeCommand(1));

    }
}
