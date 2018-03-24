package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * @deprecated Not a mechanical feature yet
 */
public class ClimberCarriageBrakeSubsystem extends NonDefaultSubsystem
{
    public final Solenoid climberBreak;

    private boolean breakEnabled;

    public ClimberCarriageBrakeSubsystem()
    {
        climberBreak = new Solenoid(RobotMap.Solenoid.CLIMBER_BRAKE_SOLENOID);
        breakEnabled = false;
    }

    public void toggle()
    {
        climberBreak.set(breakEnabled = !breakEnabled);
    }

    public void set(boolean state)
    {
        climberBreak.set(breakEnabled = state);
    }

    public void enable()
    {
        set(true);
    }

    public void disable()
    {
        set(false);
    }
}
