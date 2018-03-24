package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.QuickCommand;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import static com.team2502.robot2018.Constants.Physical.Elevator;

public class LeftCommandGroup extends CommandGroup
{


    public LeftCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        Robot.NAVX.reset();

        if(Robot.autonStrategySelector.getSelected() == AutonStrategy.STRAIGHT)
        {
            crossLine();
        }
        else
        {
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
                    System.out.println("Going cross country");
                    goScaleRight();
                    break;


                case "RL":
                    goScaleLeft();
                    break;

                case "RR":
                    System.out.println("Going cross country!");
                    goScaleRight();
                    break;
            }
        }


    }


    private void crossLine()
    {
        addSequential(new DriveTime(5, 0.4F));
    }

    /**
     * Left side not confident in alliance partners
     */
    private void goSwitch()
    {
        addParallel(new RaiseElevatorSwitch());

        addSequential(new PurePursuitCommand(Paths.Left.leftSwitch));

        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    /**
     * Left side normal
     */
    private void goScaleLeft()
    {
        addSequential(new GoScaleSameSide(Paths.Left.leftScale));
    }

    private void secondCubeLeft()
    {
        addSequential(new WaitCommand(2));
        addParallel(new ActiveIntakeRotate(0.5F, 0.5));

        addParallel(new PurePursuitCommand(Paths.Left.leftScaleToSwitch));

        addSequential(new ElevatorAutonCommand(3F, -Elevator.SCALE_ELEV_HEIGHT_FT));
        addSequential(new QuickCommand(Robot.ELEVATOR::calibrateEncoder));

        addSequential(new ToggleIntakeCommand());
        addParallel(new ShootCubeCommand(2, -1));
        addSequential(new DeadreckoningDrive(1.5, 5));

        addSequential(new ToggleIntakeCommand());

        addParallel(new ActiveIntakeRotate(1F, -0.7));
        addSequential(new ElevatorAutonCommand(3F, Elevator.SWITCH_ELEV_HEIGHT_FT + 1));

        emitCube();
    }

    /**
     * Left side cross country
     */
    private void goScaleRight()
    {
        addSequential(new GoScaleCrossCountry(Paths.Left.rightScale));
    }

    private void emitCube()
    {
        addSequential(new ShootCubeCommand(1, .5F));

    }


}
