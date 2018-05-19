package com.team2502.robot2018.pathplanning.purepursuit;

import java.util.ArrayList;
import java.util.List;

public class SplinePath extends Path
{

    private SplinePath()
    {
        super();
    }

    public static SplinePath splineFromSegments(List<PathSegment> pathSegments)
    {
        SplinePath path = new SplinePath();
        path.pathSegments = pathSegments;
        path.moveNextSegment();
        return path;
    }

    public static SplinePath splineFromPoints(List<SplineWaypoint> waypointList)
    {
        List<PathSegment> pathSegments = new ArrayList<>();
        float distance = 0;
        for(int i = 0; i < waypointList.size() - 1; i++)
        {
            Point waypoint1 = waypointList.get(i);
            Point waypoint1Slope = waypointList.get(i).getSlopeVec();

            Point waypoint2 = waypointList.get(i + 1);
            Point waypoint2Slope = waypointList.get(i).getSlopeVec();

            float length = (float) SplinePathSegment.getArcLength(waypoint1, waypoint2, waypoint1Slope, waypoint2Slope, 0, 1);

            SplinePathSegment pathSegment = new SplinePathSegment(waypoint1, waypoint2, waypoint1Slope, waypoint2Slope,i == 0, i == waypointList.size() - 2, distance, distance += length, length);
            pathSegments.add(pathSegment);
        }
        return splineFromSegments(pathSegments);
    }
}
