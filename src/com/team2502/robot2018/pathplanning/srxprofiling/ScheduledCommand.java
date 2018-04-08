package com.team2502.robot2018.pathplanning.srxprofiling;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class ScheduledCommand extends CommandGroup
{
    public ScheduledCommand(double wait, Command command)
    {
        addSequential(new WaitCommand(wait));
        addSequential(command);
    }
}
