package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

import java.util.ArrayList;
import java.util.List;

public class Path
{
    private List<PathSegment> pathSegments;

    private int segmentOnI = -1;
    private PathSegment segmentOn;
    private ImmutableVector2f closestPoint;
    private ImmutableVector2f robotLocationClosestPoint;

    public Path(List<Waypoint> waypointList)
    {
        pathSegments = new ArrayList<>();
        float distance = 0;
        for(int i = 0; i < waypointList.size()-1; i++)
        {
            Waypoint waypoint1 = waypointList.get(i);
            Waypoint waypoint2 = waypointList.get(i+1);
            float length = waypoint1.getLocation().distance(waypoint2.getLocation());
            PathSegment pathSegment = new PathSegment(waypoint1,waypoint2, i == 0, i == waypointList.size()-2,distance,distance+=length,length);
            pathSegments.add(pathSegment);
        }
        moveNextSegment();
    }

    /**
     * @return If there are more segments
     */
    boolean moveNextSegment()
    {
        if(segmentOnI < pathSegments.size()-1)
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

    ImmutableVector2f getClosestPoint(ImmutableVector2f robotLocationClosestPoint) // TODO: it might be better to not look purely at the current pathsegment and instead previous path segments
    {
        if(this.robotLocationClosestPoint != null && MathUtils.epsilonEquals(this.robotLocationClosestPoint, robotLocationClosestPoint))
        {
            return closestPoint;
        }

        this.robotLocationClosestPoint = robotLocationClosestPoint;
        PathSegment current = getCurrent();
        closestPoint = MathUtils.Geometry.getClosestPoint(current.getFirst().getLocation(), current.getLast().getLocation(), robotLocationClosestPoint);
        return MathUtils.Geometry.getClosestPoint(current.getFirst().getLocation(), current.getLast().getLocation(), robotLocationClosestPoint);
    }

    float getClosestPointPathDistance(ImmutableVector2f closestPoint)
    {
        PathSegment current = getCurrent();
        ImmutableVector2f firstLocation = current.getFirst().getLocation();
        return current.getDistanceStart()+firstLocation.distance(closestPoint);
    }

    List<PathSegment> nextSegmentsInclusive(float maxAheadDistance)
    {
        List<PathSegment> segments = new ArrayList<>();
        PathSegment startSegment = pathSegments.get(segmentOnI);
        float distanceStart = startSegment.getDistanceEnd();
        for(int i = segmentOnI; i < pathSegments.size(); i++)
        {
            PathSegment pathSegment = pathSegments.get(i);
            if(pathSegment.getDistanceStart()-distanceStart < maxAheadDistance)
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
        return pathSegments.get(segmentOnI+1);
    }

    Waypoint getStart()
    {
        return pathSegments.get(0).getFirst();
    }

    Waypoint getEnd()
    {
        return pathSegments.get(pathSegments.size()-1).getLast();
    }

}
