package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.DriveTime;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CrossLine extends CommandGroup
{
    public CrossLine()
    {
        addSequential(new DriveTime(5, 0.4F));
    }
}
