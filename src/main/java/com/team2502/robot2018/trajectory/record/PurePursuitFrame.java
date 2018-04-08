package com.team2502.robot2018.trajectory.record;

import com.team2502.robot2018.trajectory.Waypoint;
import org.joml.ImmutableVector2f;

import java.util.List;

public class PurePursuitFrame
{
    private final List<Waypoint> waypoints;

    private final float robotX, robotY, lookahead, curvature, robotDX, robotDY;
    private final long time;

    public PurePursuitFrame(List<Waypoint> waypoints, float robotX, float robotY, float lookahead, float curvature, float robotDX, float robotDY, long time)
    {
        this.waypoints = waypoints;
        this.robotX = robotX;
        this.robotY = robotY;
        this.lookahead = lookahead;
        this.curvature = curvature;
        this.robotDX = robotDX;
        this.robotDY = robotDY;
        this.time = time;
    }

    String serialize()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FRAME (").append(time).append(")\n");
        sb.append("WAYPOINTS\n");
        sb.append("x,y\n");
        for(Waypoint waypoint : waypoints)
        {
            ImmutableVector2f location = waypoint.getLocation();
            sb.append(location.x).append(",").append(location.y);
        }

        sb.append("ROBOT\n");
        sb.append("x,y,dx,dy,lookahead,curvature\n");
        sb.append(robotX).append(",").append(robotY).append(",").append(robotDX).append(",").append(robotDY).append(",").append(lookahead).append(",").append(curvature);
        sb.append("\n\n");
        return sb.toString();
    }

    public List<Waypoint> getWaypoints()
    {
        return waypoints;
    }

    public float getRobotX()
    {
        return robotX;
    }

    public float getRobotY()
    {
        return robotY;
    }

    public float getLookahead()
    {
        return lookahead;
    }

    public float getCurvature()
    {
        return curvature;
    }

    public float getRobotDX()
    {
        return robotDX;
    }

    public float getRobotDY()
    {
        return robotDY;
    }

    public long getTime()
    {
        return time;
    }
}
