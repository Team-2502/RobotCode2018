package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;

public class ClimberCarriageBrakeSubsystem extends NonDefaultSubsystem
{
    public final Solenoid climberBrake;

    private boolean breakEnabled;

    public ClimberCarriageBrakeSubsystem()
    {
        climberBrake = new Solenoid(RobotMap.Solenoid.CLIMBER_BRAKE_SOLENOID);
        breakEnabled = false;
    }

    public void toggle()
    {
        climberBrake.set(breakEnabled = !breakEnabled);
    }

    public void set(boolean state)
    {
        climberBrake.set(breakEnabled = state);
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
