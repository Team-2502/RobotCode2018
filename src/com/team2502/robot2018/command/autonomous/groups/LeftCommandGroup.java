package com.team2502.robot2018.command.autonomous.groups;

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
                switch(Robot.autonStrategySelector.getSelected())
                {
                    case SCALE:
                    {
                        goScaleLeft();
                        break;
                    }
                    case SWITCH:
                    {
                        goSwitch();
                        break;
                    }
                }
                break;
            case "LR":
                goSwitch();
                break;


            case "RL":
                goScaleLeft();
                break;

            case "RR":
                crossLine();
                break;
        }


    }

    private void crossLine()
    {
        addSequential(new DriveTime(7, 0.4F));
    }

    private void goSwitch()
    {
        addSequential(new PurePursuitCommand(Paths.Left.leftSwitch));
        addSequential(new ElevatorAutonCommand(.8F));
        addSequential(new ActiveIntakeMove(0.35, 1));

        emitCube();

    }

    private void goScaleLeft()
    {
        addParallel(new ActiveIntakeMove(0.7, 0.5));
        addSequential(new PurePursuitCommand(Paths.Left.leftScale));
        addSequential(new WaitCommand(0.8F));
        addSequential(new RotateAutonStationary(55));
        addSequential(new ElevatorAutonCommand(2.7F));
        addSequential(new DeadreckoningDrive(0.5F, 2));
        emitCube();

    }

    private void emitCube()
    {
        addSequential(new ShootCubeCommand(1));

    }


}
