package com.team2502.robot2018.command.autonomous.ingredients;

import static com.team2502.robot2018.Constants.Physical.Elevator;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;


public class RaiseElevatorScale extends CommandGroup
{
    /**
     * Raise the elevator to the right height for the scale
     */
    public RaiseElevatorScale()
    {
        Robot.writeLog("raising elevator scale", 10);

        // wiggle active so the cube doesn't get stuck on the climbing hooks
        addParallel(new WiggleActiveRotate());
        addSequential(new ElevatorAutonCommand(3F, Elevator.SCALE_ELEV_HEIGHT_FT));
    }
}
