package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.DriverStation;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
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

    public Path(List<Waypoint> waypointList)
    {
        DriverStation.reportError("waypointList = " + waypointList, false);
        pathSegments = new ArrayList<>();
        float distance = 0;
        for(int i = 0; i < waypointList.size() - 1; i++)
        {
            Waypoint waypoint1 = waypointList.get(i);
            Waypoint waypoint2 = waypointList.get(i + 1);
            DriverStation.reportWarning("Waypoint 1: " + waypoint1.getLocation().toString(), false);
            DriverStation.reportWarning("Waypoint 2: " + waypoint2.getLocation().toString(), false);
            float length = waypoint1.getLocation().distance(waypoint2.getLocation());
            PathSegment pathSegment = new PathSegment(waypoint1, waypoint2, i == 0, i == waypointList.size() - 2, distance, distance += length, length);
            pathSegments.add(pathSegment);
        }
        moveNextSegment();
    }

    /**
     * @return If there are more segments
     */
    boolean moveNextSegment()
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

    ImmutableVector2f getClosestPoint(ImmutableVector2f origin) // TODO: it might be better to not look purely at the current pathsegment and instead previous path segments
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

    ImmutableVector2f getGoalPoint(float distanceLeftCurrentSegment, float lookahead)
    {
        PathSegment current = getCurrent();
        if(lookahead < distanceLeftCurrentSegment || current.isEnd())
        {
            float relativeDistance = current.getLength() - distanceLeftCurrentSegment + lookahead;
            Robot.writeLog("look current segment ... relativeDist: %.2f", 100, relativeDistance);
            return current.getPoint(relativeDistance);
        }
        else
        {
            Robot.writeLog("look non-current segment", 80);
            lookahead -= distanceLeftCurrentSegment;

            for(int i = segmentOnI + 1; i < pathSegments.size(); i++)
            {
//                Robot.writeLog("checking segment {segmentOn %d}", 100, segmentOnI);
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
        Robot.writeLog("RETURNING NULL", 80);
        return null;
    }

    boolean progressIfNeeded(float distanceLeft, float closestPointDist, ImmutableVector2f robotPos)
    {
        PathSegment pathSegment = getCurrent();
        PathSegment nextSegment = getNext();
        if(nextSegment == null) // we are on the last segment... we cannot progress
        {
            return false;
        }

        ImmutableVector2f location = pathSegment.getLast().getLocation();
        Robot.writeLog("distanceLeft: %.2f, segmentOnI: %d, point: (%.2f,%.2f)", 80, distanceLeft, segmentOnI, location.x, location.y);
        ImmutableVector2f closestPointNext = nextSegment.getClosestPoint(robotPos);
        float nextClosestPointDistance = closestPointNext.distance(robotPos);
        // TODO: add 0.5 as constant
        if((distanceLeft < Constants.PurePursuit.DISTANCE_COMPLETE_SEGMENT_TOLERANCE) || closestPointDist > nextClosestPointDistance + 0.5F)
        {
            Robot.writeLog("closestPointDist: %.2f, nCPD: %.2f, segmentI: %d, distanceLeft: %.2f", 200, closestPointDist, nextClosestPointDistance, segmentOnI, distanceLeft);
            Robot.writeLog("pos: (%.2f, %.2f)", 200, robotPos.x, robotPos.y);
            boolean moved = moveNextSegment();
            Robot.writeLog("progressing: %b", 80, moved);
            return moved;
        }
        return false;
    }


    float getClosestPointPathDistance(ImmutableVector2f closestPoint)
    {
        PathSegment current = getCurrent();
        ImmutableVector2f firstLocation = current.getFirst().getLocation();
        return current.getAbsoluteDistanceStart() + firstLocation.distance(closestPoint);
    }

    List<PathSegment> nextSegmentsInclusive(float maxAheadDistance)
    {
        List<PathSegment> segments = new ArrayList<>();
        PathSegment startSegment = pathSegments.get(segmentOnI);
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

    PathSegment getCurrent()
    {
        return segmentOn;
    }

    PathSegment getNext()
    {
        int nextSegmentI = segmentOnI + 1;
        if(nextSegmentI >= pathSegments.size())
        {
            return null;
        }
        PathSegment nextSegment = pathSegments.get(nextSegmentI);
        return nextSegment;
    }

    Waypoint getStart()
    {
        return pathSegments.get(0).getFirst();
    }

    Waypoint getEnd()
    {
        return pathSegments.get(pathSegments.size() - 1).getLast();
    }

}
