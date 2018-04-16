package com.team2502.robot2018.command.autonomous.ingredients;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ElevatorLowerCommand extends CommandGroup
{
    public ElevatorLowerCommand()
    {
        addParallel(new ActiveIntakeRaiseCommand());
        addSequential(new ElevatorAutonCommand(2.5, 0));
    }
}
