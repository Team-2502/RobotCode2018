package com.team2502.robot2018.command.autonomous.ingredients;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class WiggleActiveRotate extends CommandGroup
{
    public WiggleActiveRotate()
    {

        // out
        addSequential(new ActiveIntakeRotate(0.5, -0.4));

        // in
        addSequential(new ActiveIntakeRotate(1, 0.4));

    }
}
