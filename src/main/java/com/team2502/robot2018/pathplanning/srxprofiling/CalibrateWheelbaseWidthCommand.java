package com.team2502.robot2018.pathplanning.srxprofiling;

import com.team2502.robot2018.Constants;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.util.Arrays;

public class CalibrateWheelbaseWidthCommand extends CommandGroup
{
    public CalibrateWheelbaseWidthCommand()
    {
//        Trajectory traj = Pathfinder.generate(new Waypoint[]{new Waypoint(0, 0, 0), new Waypoint(1, 0, 0)}, Constants.SRXProfiling.CONFIG_SETTINGS);
//
//        System.out.println("Arrays.toString(traj.segments) = " + Arrays.toString(traj.segments));
//
        ScheduledCommand[] commands = new ScheduledCommand[0];

        addSequential(new SRXProfilingCommand(commands, 1, new Waypoint(0, 0, 0),
                                              new Waypoint(5, 5, Math.PI / 2)));
    }

}