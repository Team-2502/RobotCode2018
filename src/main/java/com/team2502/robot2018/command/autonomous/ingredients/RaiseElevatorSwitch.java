package com.team2502.robot2018.command.autonomous.ingredients;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

import static com.team2502.robot2018.Constants.Physical.Elevator;

public class RaiseElevatorSwitch extends CommandGroup
{
    /**
     * Raise the elevator to the right height for the switch
     */
    public RaiseElevatorSwitch()
    {
        addSequential(new WaitCommand(0.5));
        addParallel(new ElevatorAutonCommand(3.0F, Elevator.SWITCH_ELEV_HEIGHT_FT));
        addParallel(new ActiveIntakeRotate(1.45, 0.8));
    }
}
