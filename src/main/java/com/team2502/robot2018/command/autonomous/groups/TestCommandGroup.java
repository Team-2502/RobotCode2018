package com.team2502.robot2018.command.autonomous.groups;


import com.team2502.robot2018.command.autonomous.ingredients.ElevatorAutonCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import static com.team2502.robot2018.Constants.Physical.Elevator;

/**
 * Should be used for all testing to make sure that we don't test something
 * by taking out working code and forget to change it back
 */
public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        addSequential(new ElevatorAutonCommand(3, Elevator.SWITCH_ELEV_HEIGHT_FT));
        addSequential(new WaitCommand(1));
        addSequential(new ElevatorAutonCommand(3, 0));
        addSequential(new WaitCommand(1));
        addSequential(new ElevatorAutonCommand(3, Elevator.SCALE_ELEV_HEIGHT_FT));
        addSequential(new WaitCommand(1));
        addSequential(new ElevatorAutonCommand(3, 0));
    }
}
