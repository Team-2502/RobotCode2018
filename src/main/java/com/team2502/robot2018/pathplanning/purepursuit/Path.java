package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A path is the conglomerate of several {@link PathSegment}s, which are in turn made from two {@link Waypoint}s.
 * Thus, a Path is the overall Path that the robot will take formed by Waypoints.
 * This class is very helpful when it comes to tracking which segment is currently on and getting the distance
 * on the path at any point (taking arclength ... basically making path 1D).
 */
public class Path
{
    private List<PathSegment> pathSegments;

    private int segmentOnI = -1;
    private PathSegment segmentOn;
    private ImmutableVector2f closestPoint;
    private ImmutableVector2f robotLocationClosestPoint;

    private Path() {}

    public static Path fromSegments(List<PathSegment> pathSegments)
    {
        Path path = new Path();
        path.pathSegments = pathSegments;
        path.moveNextSegment();
        return path;
    }

    public static Path fromPoints(List<? extends Point> waypointList)
    {
        List<PathSegment> pathSegments = new ArrayList<>();
        float distance = 0;
        for(int i = 0; i < waypointList.size() - 1; i++)
        {
            Point waypoint1 = waypointList.get(i);
            Point waypoint2 = waypointList.get(i + 1);
            float length = waypoint1.getLocation().distance(waypoint2.getLocation());
            PathSegment pathSegment = new PathSegment(waypoint1, waypoint2, i == 0, i == waypointList.size() - 2, distance, distance += length, length);
            pathSegments.add(pathSegment);
        }
        return fromSegments(pathSegments);
    }

    public static Path fromPoints(Point... points)
    {
        return fromPoints(Arrays.asList(points));
    }

    /**
     * @return If there are more segments
     */
    public boolean moveNextSegment()
    {
        if(segmentOnI < pathSegments.size() - 1)
        {
            segmentOnI++;
            segmentOn = pathSegments.get(segmentOnI);
            return true;
        }
        return false;
    }

    public boolean exists()
    {
        return !pathSegments.isEmpty();
    }

    public ImmutableVector2f getClosestPoint(ImmutableVector2f origin) // TODO: it might be better to not look purely at the current pathsegment and instead previous path segments
    {
        if(this.robotLocationClosestPoint != null && MathUtils.epsilonEquals(this.robotLocationClosestPoint, origin))
        {
            return closestPoint;
        }

        this.robotLocationClosestPoint = origin;
        PathSegment current = getCurrent();
        Robot.writeLog("origin: %.2f %.2f", 100, origin.x, origin.y);
        closestPoint = current.getClosestPoint(origin);
        return closestPoint;
    }

    public int getSegmentOnI()
    {
        return segmentOnI;
    }

    /**
     * Calculate the goal point that we should be driving at
     *
     * @param distanceLeftCurrentSegment The distance left before we complete our segment
     * @param lookahead Our current lookahead distance
     * @return Where we should drive at
     */
    public ImmutableVector2f getGoalPoint(float distanceLeftCurrentSegment, float lookahead)
    {
        PathSegment current = getCurrent();
        // If our circle intersects on the same path
        if(lookahead < distanceLeftCurrentSegment || current.isEnd())
        {
            float relativeDistance = current.getLength() - distanceLeftCurrentSegment + lookahead;
            return current.getPoint(relativeDistance);
        }
        // If our circle intersects other segments
        else
        {
            lookahead -= distanceLeftCurrentSegment;

            for(int i = segmentOnI + 1; i < pathSegments.size(); i++)
            {
                PathSegment pathSegment = pathSegments.get(i);
                float length = pathSegment.getLength();
                if(lookahead > length && !pathSegment.isEnd())
                {
                    lookahead -= length;
                }
                else
                {
                    return pathSegment.getPoint(lookahead);
                }
            }
        }
        return null;
    }

    public boolean progressIfNeeded(float distanceLeftSegment, float closestPointDist, ImmutableVector2f robotPos)
    {
        PathSegment nextSegment = getNext();
        if(nextSegment == null) // we are on the last segment... we cannot progress
        {
            return false;
        }

        ImmutableVector2f closestPointNext = nextSegment.getClosestPoint(robotPos);
        float nextClosestPointDistance = closestPointNext.distance(robotPos);
        // TODO: add 0.5 as constant
        if((distanceLeftSegment < Constants.PurePursuit.DISTANCE_COMPLETE_SEGMENT_TOLERANCE) || closestPointDist > nextClosestPointDistance + 0.5F)
        {
            return moveNextSegment();
        }
        return false;
    }


    public float getAbsDistanceOfClosestPoint(ImmutableVector2f closestPoint)
    {
        PathSegment current = getCurrent();
        ImmutableVector2f firstLocation = current.getFirst().getLocation();
        return current.getAbsoluteDistanceStart() + firstLocation.distance(closestPoint);
    }

    /**
     * @param maxAheadDistance The distance to look ahead from the last segment
     * @return
     */
    public List<PathSegment> nextSegmentsInclusive(float maxAheadDistance)
    {
        List<PathSegment> segments = new ArrayList<>();
        PathSegment startSegment = getCurrent();
        segments.add(startSegment);
        float distanceStart = startSegment.getAbsoluteDistanceEnd();
        for(int i = segmentOnI + 1; i < pathSegments.size(); i++)
        {
            PathSegment pathSegment = pathSegments.get(i);
            if(pathSegment.getAbsoluteDistanceStart() - distanceStart < maxAheadDistance)
            {
                segments.add(pathSegment);
            }
            else
            {
                break;
            }
        }
        return segments;
    }

    public PathSegment getCurrent()
    {
        return segmentOn;
    }

    public PathSegment getNext()
    {
        int nextSegmentI = segmentOnI + 1;
        if(nextSegmentI >= pathSegments.size())
        {
            return null;
        }
        PathSegment nextSegment = pathSegments.get(nextSegmentI);
        return nextSegment;
    }

    public Point getStart()
    {
        return pathSegments.get(0).getFirst();
    }

    public Point getEnd()
    {
        return pathSegments.get(pathSegments.size() - 1).getLast();
    }

    @Override
    public Path clone()
    {
        Path path = new Path();
        path.pathSegments = pathSegments;
        path.segmentOn = segmentOn;
        path.segmentOnI = segmentOnI;
        path.closestPoint = closestPoint;
        path.robotLocationClosestPoint = robotLocationClosestPoint;
        return path;
    }
}
