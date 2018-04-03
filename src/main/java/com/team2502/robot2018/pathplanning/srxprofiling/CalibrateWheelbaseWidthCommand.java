package com.team2502.robot2018.pathplanning.srxprofiling;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class CalibrateWheelbaseWidthCommand extends CommandGroup
{
    public CalibrateWheelbaseWidthCommand() throws Exception
    {

        addSequential(new SRXProfilingCommand(new Waypoint(0, 0, 0),
                                              new Waypoint(0, 0, Math.PI / 2)));
    }

}
