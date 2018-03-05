package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.Paths;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        addSequential(new PurePursuitCommand(Paths.Center.rightSwitch));
    }
}
