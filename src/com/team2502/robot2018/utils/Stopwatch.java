package com.team2502.robot2018.utils;

public class Stopwatch
{
    private long lastTime = -1;

    /**
     *
     * @return 0 if first time running or the time in seconds since last method call
     */
    public float dTime()
    {
        long nanoTime = System.nanoTime();
        long dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return dTime / 1E9F;
    }
}
