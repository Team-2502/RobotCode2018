package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.QuickCommand;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class LeftCommandGroup extends CommandGroup
{


    public LeftCommandGroup()
    {
        Robot.TRANSMISSION_SOLENOID.setLowGear(true);
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
                    case SWITCH_SCALE: //TODO: make sure this is mirrored
                        goScaleLeft();
                        secondCubeLeft();
                        break;
                }
                break;
            case "LR":
                goScaleRight();
                break;


            case "RL":
                goScaleLeft();
                break;

            case "RR":
                System.out.println("Going cross country!");
//                crossLine();
                goScaleRight();
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

        addSequential(new ElevatorAutonCommand(.8F, Constants.SWITCH_ELEV_HEIGHT_FT));

        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    private void goScaleLeft()
    {

        addParallel(new ActiveIntakeRotate(1, 0.5));
        addSequential(new PurePursuitCommand(Paths.Left.leftScale));
//        addSequential(new RaiseElevatorScale());

//        addSequential(new NavXRotateCommand(40,3));

//        addSequential(new DeadreckoningDrive(0.5F,0.5F));

        addSequential(new ToggleIntakeCommand());
        addSequential(new ActiveIntakeRotate(.25F, -0.5));

        emitCube();

        addSequential(new DeadreckoningDrive(0.7F, -4F));
        addSequential(new ElevatorAutonCommand(2.5, 0));

    }

    private void secondCubeLeft()
    {
        addSequential(new WaitCommand(2));
        addParallel(new ActiveIntakeRotate(0.5F, 0.5));
//        addParallel(new RotateAutonStationary(140));
        addSequential(new PurePursuitCommand(Paths.Left.leftScaleToSwitch));

        addSequential(new ElevatorAutonCommand(3F, -Constants.SCALE_ELEV_HEIGHT_FT));
        addSequential(new QuickCommand(Robot.ELEVATOR::calibrateEncoder));

        addSequential(new ToggleIntakeCommand());
        addParallel(new ShootCubeCommand(2, -1));
        addSequential(new DeadreckoningDrive(1.5, 5));

        addSequential(new ToggleIntakeCommand());

        addParallel(new ActiveIntakeRotate(1F, -0.7));
        addSequential(new ElevatorAutonCommand(3F, Constants.SWITCH_ELEV_HEIGHT_FT + 1));

        emitCube();
    }

    private void goScaleRight()
    {
        addSequential(new PurePursuitCommand(Paths.Left.rightScale));

        addParallel(new ActiveIntakeRotate(1F, -0.5));

        emitCube();

        addSequential(new DeadreckoningDrive(0.7F, -4F));
        addSequential(new ElevatorAutonCommand(2.5, 0));

    }

    private void emitCube()
    {
        addSequential(new ShootCubeCommand(1, .5F));

    }


}
