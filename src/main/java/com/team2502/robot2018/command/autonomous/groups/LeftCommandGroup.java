package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

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
                        case SCALE_TWICE:
                        {
//                            goScaleLeft();
                            secondCubeLeft();
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

        addSequential(new PurePursuitCommand(PathConfig.Left.leftSwitch, true));

        addSequential(new ActiveIntakeRotate(0.35, 1));

        emitCube();
    }

    /**
     * Left side normal
     */
    private void goScaleLeft()
    {
        addSequential(new GoScaleSameSide(PathConfig.Left.leftScale));
    }

    private void secondCubeLeft()
    {
        //TODO: Uncomment for realism
//        addSequential(new ToggleIntakeCommand());

        addParallel(new RunIntakeCommand(5, -1));

        addSequential(new PurePursuitCommand(PathConfig.Left.leftScaleToSwitch, false));

        addParallel(new ElevatorAutonCommand(3F, 0));




//        addSequential(new PurePursuitCommand(PathConfig.Left.leftSwitchToScale, true));

//        addParallel(new RaiseElevatorScale());
//
//        addSequential(new ToggleIntakeCommand());
//        emitCube();
//        addSequential(new BackOffScale());
    }

    /**
     * Left side cross country
     */
    private void goScaleRight()
    {
        addSequential(new GoScaleCrossCountry(PathConfig.Left.rightScale));
    }

    private void emitCube()
    {
        addSequential(new RunIntakeCommand(1, .5F));

    }


}
