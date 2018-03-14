package com.team2502.robot2018.trajectory;

/**
 * Makes segments created by two {@link Waypoint}s easier to work with in {@link Path}
 */
public class PathSegment
{
    private final Waypoint first;
    private final Waypoint last;
    private final float length;
    private final boolean end;
    private final boolean start;
    private final float distanceStart;
    private final float distanceEnd;

    protected PathSegment(Waypoint first, Waypoint last, boolean start, boolean end, float distanceStart, float distanceEnd, float length)
    {
        this.first = first;
        this.last = last;
        this.length = length;
        this.end = end;
        this.start = start;
        this.distanceStart = distanceStart;
        this.distanceEnd = distanceEnd;
    }

    public float getDistanceStart()
    {
        return distanceStart;
    }

    public float getDistanceEnd()
    {
        return distanceEnd;
    }

    public boolean isEnd()
    {
        return end;
    }

    public boolean isStart()
    {
        return start;
    }

    public Waypoint getFirst()
    {
        return first;
    }

    public Waypoint getLast()
    {
        return last;
    }

    public float getLength()
    {
        return length;
    }
}
