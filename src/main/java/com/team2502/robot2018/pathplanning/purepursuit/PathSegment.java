package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

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
    private final ImmutableVector2f dPos;
    private final ImmutableVector2f startLocation;

    protected PathSegment(Waypoint first, Waypoint last, boolean start, boolean end, float distanceStart, float distanceEnd, float length)
    {
        this.first = first;
        this.last = last;
        this.length = length;
        this.end = end;
        this.start = start;
        this.distanceStart = distanceStart;
        this.distanceEnd = distanceEnd;
        startLocation = first.getLocation();
        dPos = last.getLocation().sub(first.getLocation());
    }

    public ImmutableVector2f getClosestPoint(ImmutableVector2f robotPos)
    {
        return MathUtils.Geometry.getClosestPoint(first.getLocation(), last.getLocation(), robotPos);
    }

    public ImmutableVector2f getPoint(float relativeDistance)
    {
        return dPos.mul(relativeDistance / length).add(startLocation);
    }

    /**
     * Get the "distance" left in an efficient manner
     * Instead of calculating sqrt(dx^2 + dy^2) we simply calculate dx + dy
     *
     * @param point a close point
     * @return
     */
    public float getDistanceLeftEff(ImmutableVector2f point)
    {
        ImmutableVector2f lastLocation = last.getLocation();
        return Math.abs(point.x - lastLocation.x) + Math.abs(point.y - lastLocation.y);
    }

    /**
     * Get the distance left squared
     *
     * @param point a close point
     * @return
     */
    public float getDistanceLeft2(ImmutableVector2f point)
    {
        ImmutableVector2f lastLocation = last.getLocation();
        return lastLocation.sub(point).lengthSquared();
    }

    /**
     * Get the distance left
     *
     * @param point a close point
     * @return
     */
    public float getDistanceLeft(ImmutableVector2f point)
    {
        return (float) Math.sqrt(getDistanceLeft2(point));
    }

    public boolean isPast(ImmutableVector2f point)
    {
        ImmutableVector2f firstLocation = first.getLocation();
        ImmutableVector2f lastLocation = last.getLocation();
        boolean pastX = lastLocation.x - firstLocation.x > 0 ? point.x > lastLocation.x : point.x < lastLocation.x;
        boolean pastY = lastLocation.y - firstLocation.y > 0 ? point.y > lastLocation.y : point.y < lastLocation.y;
        return pastX && pastY;
    }

    public float getAbsoluteDistanceStart()
    {
        return distanceStart;
    }

    public float getAbsoluteDistanceEnd()
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
