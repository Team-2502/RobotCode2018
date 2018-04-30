package com.team2502.robot2018.utils;

/**
 * Each time it is read, SimulatedStopwatch increases by dt
 */
public class SimulatedStopwatch implements IStopwatch
{

    private final float dt;

    public SimulatedStopwatch(float dt)
    {
        this.dt = dt;
    }

    float count = -1;

    @Override
    public float read()
    {
        if(count == -1)
        {
            count = 0;
        }
        count+=dt;
        return count;
    }

    @Override
    public void reset()
    {
        count = 0;
    }

    @Override
    public boolean isInit()
    {
        return count != -1;
    }
}
