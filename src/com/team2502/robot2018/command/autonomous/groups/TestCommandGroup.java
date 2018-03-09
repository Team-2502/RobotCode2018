package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.DeadreckoningDrive;
import com.team2502.robot2018.command.autonomous.ingredients.RaiseElevatorSwitch;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
//        addParallel(new DeadreckoningDrive(5,0.5));
        addSequential(new RaiseElevatorSwitch());
//        addSequential(new RaiseElevatorSwitch());
    }
}
