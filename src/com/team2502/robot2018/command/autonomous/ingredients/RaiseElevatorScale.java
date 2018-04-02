package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RaiseElevatorScale extends CommandGroup
{
    public RaiseElevatorScale()
    {
        this(0);
    }

    public RaiseElevatorScale(double timeout)
    {
        Robot.writeLog("raising elevator scale", 10);
//        addSequential(new WaitCommand(timeout));
        addParallel(new WiggleActiveRotate());
//        addSequential(new ElevatorAutonCommand(3F, Constants.SCALE_ELEV_HEIGHT_FT));
        addSequential(new ElevatorAutonCommand(1.75F, 40));
    }
}
