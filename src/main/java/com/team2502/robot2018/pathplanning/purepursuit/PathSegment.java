package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

/**
 * Makes segments created by two {@link Waypoint}s easier to work with in {@link Path}
 */
public class PathSegment
{
    private final Point first;
    private final Point last;
    protected float length;
    private final boolean end;
    private final boolean start;
    private final float distanceStart;
    private final float distanceEnd;
    private final ImmutableVector2f dPos;


    private final ImmutableVector2f startLocation;

    protected PathSegment(Point first, Point last, boolean start, boolean end, float distanceStart, float distanceEnd, float length)
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
        return MathUtils.Geometry.getClosestPointLineSegments(first.getLocation(), last.getLocation(), robotPos);
    }

    public ImmutableVector2f getPoint(float relativeDistance)
    {
        return dPos.mul(relativeDistance / length).add(startLocation);
    }

    /**
     * Get the distance left squared
     *
     * @param point a close point
     * @return
     * @deprecated
     */
    public float getDistanceLeft2(ImmutableVector2f point)
    {
        ImmutableVector2f lastLocation = last.getLocation();
        return lastLocation.sub(point).lengthSquared();
    }

    /**
     * Get the distance left
     *
     * @param point a point on the line
     * @return
     */
    public float getDistanceLeft(ImmutableVector2f point)
    {
        float firstX = this.first.getLocation().x;
        float lastX = this.last.getLocation().x;
        if(firstX - lastX != 0)
        {
            return length * (1 - (point.x - firstX) / (lastX - firstX));
        }
        else
        {
            float firstY = this.first.getLocation().y;
            float lastY = this.last.getLocation().y;
            if(firstY - lastY != 0)
            {
                return length * (1 - (point.y - firstY) / (lastY - firstY));
            }
            else
            {
                System.out.println(this);
                throw new IllegalArgumentException("line segment cannot be a point!");
            }
        }
    }

    public boolean isPast(ImmutableVector2f point)
    {
        ImmutableVector2f firstLocation = first.getLocation();
        ImmutableVector2f lastLocation = last.getLocation();
        boolean pastX = lastLocation.x - firstLocation.x > 0 ? point.x > lastLocation.x : point.x < lastLocation.x;
        boolean pastY = lastLocation.y - firstLocation.y > 0 ? point.y > lastLocation.y : point.y < lastLocation.y;
        return pastX && pastY;
    }

    /**
     * @return How far along the entire path that the first point is
     */
    public float getAbsoluteDistanceStart()
    {
        return distanceStart;
    }

    /**
     * @return How far along the entire path that the end point is
     */
    public float getAbsoluteDistanceEnd()
    {
        return distanceEnd;
    }

    /**
     * @return If this segment is the last segment in the path
     */
    public boolean isEnd()
    {
        return end;
    }

    /**
     * @return If this segment is the first segment in the path
     */
    public boolean isStart()
    {
        return start;
    }

    public Point getFirst()
    {
        return first;
    }

    public Point getLast()
    {
        return last;
    }

    public float getLength()
    {
        return length;
    }

    public ImmutableVector2f getdPos()
    {
        return dPos;
    }

    @Override
    public String toString()
    {
        return "PathSegment{" +
               "first=" + first +
               ", last=" + last +
               '}';
    }
}
