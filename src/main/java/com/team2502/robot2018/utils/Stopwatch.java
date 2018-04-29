package com.team2502.robot2018.utils;

/**
 * A handy stopwatch for recording time in seconds every time it is polled
 */
public class Stopwatch
{
    private long lastTime = -1;

    /**
     * @return 0 if first time running or the time in seconds since last method call
     */
    public float pop()
    {
        long nanoTime = System.nanoTime();
        long dTime = (lastTime == -1) ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return dTime / 1E9F;
    }

    public float read()
    {
        long nanoTime = System.nanoTime();
        long dTime = (lastTime == -1) ? 0 : nanoTime - lastTime;
        return dTime / 1E9F;
    }

    public void reset()
    {
        lastTime = System.nanoTime();
    }

    public boolean isInit()
    {
        return lastTime != -1;
    }
}
