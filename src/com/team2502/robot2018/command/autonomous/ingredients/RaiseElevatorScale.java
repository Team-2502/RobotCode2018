package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Constants;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RaiseElevatorScale extends CommandGroup
{
    public RaiseElevatorScale()
    {
//        addParallel(new WiggleActiveRotate());
        addSequential(new ElevatorAutonCommand(3F, Constants.SCALE_ELEV_HEIGHT_FT));
    }
}
