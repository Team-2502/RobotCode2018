package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * Created by 64009334 on 2/18/18.
 */
public class ShiftDriveTrainCommand extends InstantCommand
{
    public ShiftDriveTrainCommand()
    {
        requires(Robot.TRANSMISSION_SOLENOID);
    }

    protected void execute()
    {
        Robot.TRANSMISSION_SOLENOID.switchGear();
    }
}
