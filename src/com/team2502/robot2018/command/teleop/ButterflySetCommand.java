package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ButterflySetCommand extends InstantCommand
{
    private boolean _state;

    public ButterflySetCommand(boolean state)
    {
        requires(Robot.BUTTERFLY_SOLENOID);
        this._state = state;
    }

    @Override
    protected void execute()
    {
        Robot.BUTTERFLY_SOLENOID.set(_state);
    }
}
