package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Constants;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class RaiseElevatorSwitch extends CommandGroup
{
    public RaiseElevatorSwitch()
    {
        addSequential(new WaitCommand(1.5));
        addParallel(new ElevatorAutonCommand(.8F, Constants.SWITCH_ELEV_HEIGHT_FT));

    }
}