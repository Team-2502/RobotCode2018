package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.Paths;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        addSequential(new PurePursuitCommand(Paths.Center.rightSwitch));
    }
}
