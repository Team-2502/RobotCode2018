package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.pathplanning.srxprofiling.SRXProfilingCommand;
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
                // Left scale -- second letter is L
                case "RL":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE_WAIT:
                        case ONLY_SAME_SIDE:
                        case SCALE:
                            goScaleLeft();
                            break;
                        case DEEP_SCALE:
                            goDeepScaleLeft();
                            break;
                        default:
                            crossLine();
                            break;
                    }
                    break;
                    
                case "LL":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE:
                        case ONLY_SAME_SIDE:
                        case SCALE_WAIT:
                            goScaleLeft();
                            break;
                        case SWITCH:
                            goSwitch();
                            break;
                        case DEEP_SCALE:
                            goDeepScaleLeft();
                            break;
                        default:
                            crossLine();
                            break;
                    }
                    break;
                // Right scale - 2nd letter is R
                case "LR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {

                        case SCALE_WAIT:
                            addSequential(new WaitCommand(3));
                        case SCALE:
                        case DEEP_SCALE:
                            goScaleRight();
                            break;

                        case SWITCH:
                            goSwitch();
                            break;

                        case ONLY_SAME_SIDE:
                        default:
                            crossLine();
                            break;
                    }
                    break;
                case "RR":
                    switch(Robot.autonStrategySelector.getSelected())
                    {
                        case SCALE_WAIT:
                            addSequential(new WaitCommand(3));
                        case SCALE:
                            goScaleRight();
                            break;

                        case ONLY_SAME_SIDE: // opposite side
                        case SWITCH: // also opposite side
                        default:
                            crossLine();
                            break;
                    }
                    break;
            }
        }


    }


    private void crossLine()
    {
        addSequential(new CrossLine());
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
        Robot.writeLog("Going Scale Same Side Left", 200);
        addParallel(new RaiseElevatorScale());
        addParallel(new ActiveIntakeLowerCommand());
        addSequential(new GoScaleSameSide(PathConfig.Left.leftScale));
        addSequential(new ActiveShootCommand());
    }

    private void secondCubeLeft()
    {
//        secondCubeLeftPP();
        secondCubeLeftMP();
    }

    private void secondCubeLeftMP()
    {
        addSequential(new SRXProfilingCommand(NO_COMMANDS,
                                              1,
                                              TrajConfig.Left.twoCube));
    }

    /**
     * Second cube left scale
     */
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
        addSequential(new FastRotateCommand(120, 8, -0.4F));

        addParallel(new ActiveIntakeLowerCommand());
        addParallel(new RunIntakeCommand(4, -0.5));

        PurePursuitCommand forward = new PurePursuitCommand.Builder() // (0,0) is replaced with current robot position
                                                                      .addWaypoint(0, 0, 8, 20, -16)
                                                                      .addWaypoint(5, 15, 0, 20, -16)
                                                                      .build();

        PurePursuitCommand back = new PurePursuitCommand.Builder() // (0,0) is replaced with current robot position
                                                                   .addWaypoint(0, 0, 8, 20, -16)
                                                                   .addWaypoint(1, 20, 0, 20, -16)
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
        System.out.println("Going cross country!");
        addSequential(new GoScaleCrossCountry(PathConfig.Left.rightScale));
    }

    private void goDeepScaleLeft()
    {
        addSequential(new PurePursuitCommand(PathConfig.Left.leftScaleDeepNullZone, false)); // Drive to deep scale
        emitCube(); // Shoot cube via active spinning wheels, then open up for safety

        addParallel(new WaitCommand(3)); // lower elevator and back up
        addParallel(new ElevatorAutonCommand(1.7, 0));
        addSequential(new DeadreckoningDrive(5.0, -5.0F));
    }

    private void emitCube()
    {
        addParallel(new RunIntakeCommand(0.3F, .5F));
    }
}
