package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.autonomous.ingredients.*;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.SRXProfilingCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.ScheduledCommand;
import com.team2502.robot2018.pathplanning.srxprofiling.TrajConfig;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Constants.SRXProfiling.NO_COMMANDS;


public class CenterCommandGroup extends CommandGroup
{
    public CenterCommandGroup()
    {
        // done in autonomousInit()
//        addSequential(new QuickCommand(Robot.ELEVATOR::calibrateEncoder));
        // Begin by calibrating the navX
        Robot.NAVX.reset();

        // Choose a path to take
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 1);

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
        addSequential(new PurePursuitCommand(PathConfig.Center.leftSwitch, true));
        emitCubeSwitch();
        addParallel(new ToggleIntakeCommand());


        addSequential(new SRXProfilingCommand(new ScheduledCommand[]{new ScheduledCommand(1, new ElevatorAutonCommand(2, 0)),
                                                                     new ScheduledCommand(3.5, new RunIntakeCommand(3, -1)),
                                                                     new ScheduledCommand(7, new RaiseElevatorSwitch()),},
                                              (double) 1,
                                              TrajConfig.Center.secondCubeLeftSwitch),
                      11);
    }

    private void goSwitchRight()
    {
        moveElevator();
        addSequential(new PurePursuitCommand(PathConfig.Center.rightSwitch, true));
        // Just in case . . .
//        addSequential(new SRXProfilingCommand(NO_COMMANDS,
//                                              1,
//                                              TrajConfig.Center.firstCubeRightSwitch));
        emitCubeSwitch();

        addParallel(new ToggleIntakeCommand());


        addSequential(new SRXProfilingCommand(new ScheduledCommand[]{new ScheduledCommand(1, new ElevatorAutonCommand(2, 0)),
                                                                     new ScheduledCommand(3.5, new RunIntakeCommand(3, -1)),
                                                                     new ScheduledCommand(7, new RaiseElevatorSwitch()),},
                                              (double) 1,
                                              TrajConfig.Center.secondCubeRightSwitch),
                      11);



//        addSequential(new SRXProfilingCommand(NO_COMMANDS,
//                                              (double) TrajConfig.Center.Right.toSecondCubeDir,
//                                              TrajConfig.Center.Right.toSecondCube));
//
//
//
//        addSequential(new NavXRotateCommand(0, 0.5F, false));
//        addParallel(new RunIntakeCommand(1, -1));
//        addSequential(new SRXProfilingCommand(NO_COMMANDS,
//                                              1,
//                                              TrajConfig.Center.Right.toSecondCubePt2));
//
//        addSequential(new SRXProfilingCommand(NO_COMMANDS,
//                                              -1,
//                                              TrajConfig.Center.Right.toSecondCubePt2));
//
//        addSequential(new SRXProfilingCommand(NO_COMMANDS,
////                new ScheduledCommand[] { new ScheduledCommand(0, new ElevatorAutonCommand(2.0, Constants.Physical.Elevator.SWITCH_ELEV_HEIGHT_FT)) },
//                                              1,
//                                              TrajConfig.Center.Right.backToSwitch));

        emitCubeSwitch();
    }

    private void moveElevator()
    {
        addParallel(new RaiseElevatorSwitch());
        addParallel(new ActiveIntakeRotate(0.35, 1));
    }

    private void emitCubeSwitch()
    {
//        addSequential(new ActiveIntakeRotate(0.4, 0.7));
        addSequential(new ToggleIntakeCommand());
        addSequential(new RunIntakeCommand(1));
    }

}
