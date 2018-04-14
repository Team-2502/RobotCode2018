package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ActiveIntakeCommand;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.SRXProfilingCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.ScheduledCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.TrajConfig;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Constants.SRXProfiling.NO_COMMANDS;

public class LeftCommandGroup extends CommandGroup
{
    public LeftCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        if(Robot.autonStrategySelector.getSelected() == AutonStrategy.STRAIGHT)
        {
            crossLine();
        }
        else
        {
            switch(AUTO_GAME_DATA)
            {
                case "LL":
                    goScaleRight();
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case FCC:
                        {
                            goScaleRight();
                            break;
                        }
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
                            goScaleLeft();
                            secondCubeLeft();
                            break;
                        }
                    }
                    break;
                case "LR":
                    System.out.println("Going cross country");
                    goScaleRight();
                    break;


                case "RL":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case FCC:
                        {
                            goScaleRight();
                            break;
                        }
                        case SCALE:
                        {
                            goScaleLeft();
                            break;
                        }
                        case SCALE_TWICE:
                        {
                            goScaleLeft();
                            secondCubeLeft();
                            break;
                        }
                    }
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
        addParallel(new ActiveIntakeRotate(2.0, 0.6));
//        addSequential(new GoScaleSameSide(PathConfig.Left.leftScale));
        goScaleLeftMP();
    }

    private void goScaleLeftMP()
    {
        addSequential(new SRXProfilingCommand(NO_COMMANDS,
                                              1,
                                              TrajConfig.Left.firstCube));

        addSequential(new RunIntakeCommand(0.3F, .5F));
        addSequential(new ToggleIntakeCommand());
    }

    private void secondCubeLeft()
    {
//        secondCubeLeftPP();
        secondCubeLeftMP();
    }

    private void secondCubeLeftMP()
    {
        addParallel(new ElevatorAutonCommand(3, 0));
        addSequential(new SRXProfilingCommand(new ScheduledCommand[]{new ScheduledCommand(3, new RunIntakeCommand(3, -1))},
                                              1,
                                              TrajConfig.Left.twoCube));
    }

    private void secondCubeLeftDeadReckoning()
    {
        addSequential(new NavXRotateCommand(150, 3));

        addParallel(new RunIntakeCommand(3, -1));
        addSequential(new DeadreckoningDrive(1.5, 4));

        addSequential(new DeadreckoningDrive(1.5, -4));

        addSequential(new NavXRotateCommand(0, 3));

        addSequential(new DeadreckoningDrive(.5, 4));

        addSequential(new RaiseElevatorScale());
    }

    private void secondCubeLeftPP()
    {
        addSequential(new PurePursuitCommand(PathConfig.Left.leftScaleToSwitch, false));

        addParallel(new ToggleIntakeCommand());
        emitCube();

//        addSequential(new WaitCommand(2));
        addSequential(new BackOffCommand());
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
        addParallel(new RunIntakeCommand(0.3F, .5F));

    }


}
