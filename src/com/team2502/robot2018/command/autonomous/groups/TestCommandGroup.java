package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.command.autonomous.ingredients.RaiseElevatorScale;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        addSequential(new RaiseElevatorScale());
    }
}
