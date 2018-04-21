package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;

import static com.team2502.robot2018.Constants.Physical.Elevator;


public class RaiseElevatorScale extends CommandGroup
{
    /**
     * Raise the elevator to the right height for the scale
     */
    public RaiseElevatorScale()
    {
        Robot.writeLog("raising elevator scale", 10);
        addSequential(new ElevatorAutonCommand(4F, Elevator.SCALE_ELEV_HEIGHT_FT));
    }
}
