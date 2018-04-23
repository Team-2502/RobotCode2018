package com.team2502.robot2018.command.autonomous.ingredients.activeintake;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ActiveShootCommand extends CommandGroup
{
    public ActiveShootCommand()
    {
        addSequential(new RunIntakeCommand(0.3F, 0.7F));
    }
}
