package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.SRXProfilingCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.ScheduledCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.TrajConfig;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

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
                        case TEST_SECOND_CUBE:
                        {
                            System.out.println("testing second cube");
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
                        case TEST_SECOND_CUBE:
                        {
                            System.out.println("testing second cube");
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

        addSequential(new ActiveIntakeLowerCommand());

        emitCube();
    }

    /**
     * Left side normal
     */
    private void goScaleLeft()
    {
        Robot.writeLog("Going Scale Same Side Left",200);
//        addParallel(new RaiseElevatorScale());
        addParallel(new ActiveIntakeLowerCommand());
        addSequential(new GoScaleSameSide(PathConfig.Left.leftScale));
        addSequential(new RaiseElevatorScale());
//        addSequential(new SRXProfilingCommand(NO_COMMANDS,
//                                              1,
//                                              TrajConfig.Left.firstCube));
//        addSequential(new WaitCommand(0.3));
        addSequential(new ActiveShootCommand());
    }

    private void secondCubeLeft()
    {
//        secondCubeLeftPP();
        secondCubeLeftMP();
    }

    private void secondCubeLeftMP()
    {
        addSequential(new ElevatorLowerCommand());

        addParallel(new ActiveIntakeLowerCommand());
        addSequential(new FastRotateCommand(170,8,-0.4F));

        addSequential(new WaitCommand(0.3));

        addParallel(new RunIntakeCommand(2, -1));

        addSequential(new SRXProfilingCommand(NO_COMMANDS,
                                              1,
                                              TrajConfig.Left.goForward));

        addParallel(new RaiseElevatorScale());
        addSequential(new SRXProfilingCommand(NO_COMMANDS,
                                              1,
                                              TrajConfig.Left.goBack));


        addSequential(new WaitCommand(0.3));
        addSequential(new FastRotateCommand(30, 1.75F, -0.4F));


        addSequential(new RunIntakeCommand(2F, .5F));
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
        addParallel(new ElevatorLowerCommand());
        addSequential(new FastRotateCommand(120,8,-0.4F));

        addParallel(new ActiveIntakeLowerCommand());
        addParallel(new RunIntakeCommand(4, -0.5));

        PurePursuitCommand forward = new PurePursuitCommand.Builder() // (0,0) is replaced with current robot position
                .addWaypoint(0,0,8,20,-16)
                .addWaypoint(5,15,0,20,-16)
                .build();

        PurePursuitCommand back = new PurePursuitCommand.Builder() // (0,0) is replaced with current robot position
                                         .addWaypoint(0,0,8,20,-16)
                                         .addWaypoint(1,20,0,20,-16)
                                       .setForward(false)
                                         .build();

        addSequential(forward);
        addSequential(back);

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
