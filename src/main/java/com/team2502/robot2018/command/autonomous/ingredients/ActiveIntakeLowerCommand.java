package com.team2502.robot2018.command.autonomous.ingredients;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ActiveIntakeLowerCommand extends CommandGroup
{
    public ActiveIntakeLowerCommand()
    {
        addSequential(new ActiveIntakeRotate(0.2, 1));
    }
}
