package com.team2502.robot2018.command.autonomous.ingredients;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ActiveShootCommand extends CommandGroup
{
    public ActiveShootCommand()
    {
        addSequential(new RunIntakeCommand(0.6F, .7F));
    }
}
