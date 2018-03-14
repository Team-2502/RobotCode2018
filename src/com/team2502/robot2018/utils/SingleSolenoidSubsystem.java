package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.Solenoid;


public abstract class SingleSolenoidSubsystem
{
    private Solenoid solenoid;
    private boolean solenoidOut;

    public SingleSolenoidSubsystem(Solenoid solenoid, boolean defaultState)
    {
        this.solenoid = solenoid;
        solenoidOut = defaultState;
        this.solenoid.set(defaultState);
    }

    public void toggle()
    {
        solenoid.set(solenoidOut = !solenoidOut);
    }

    public void set(boolean out)
    {
        solenoid.set(solenoidOut = out);
    }
}
