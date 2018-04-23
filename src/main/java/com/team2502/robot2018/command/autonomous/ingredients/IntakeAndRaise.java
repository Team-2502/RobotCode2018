package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.command.autonomous.ingredients.activeintake.ActiveIntakeRotate;
import com.team2502.robot2018.command.autonomous.ingredients.activeintake.RunIntakeCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class IntakeAndRaise extends CommandGroup
{
    public IntakeAndRaise()
    {
        addSequential(new RunIntakeCommand(1.3F, -1));
        addSequential(new ActiveIntakeRotate(.1F, 1));
        addSequential(new RaiseElevatorScale());
    }
}
