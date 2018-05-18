package com.team2502.robot2018.trajectory.record;

import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;

import java.util.List;

public class PurePursuitFrame
{
    private final List<Waypoint> waypoints;

    private final float robotX, robotY, lookahead, curvature, speedUsed, actualSpeed;
    private final long time;
    private final float robotHeading;

    public PurePursuitFrame(List<Waypoint> waypoints, float robotX, float robotY, float lookahead, float curvature, float speedUsed, float actualSpeed,  float robotHeading, long time)
    {
        this.waypoints = waypoints;
        this.robotX = robotX;
        this.robotY = robotY;
        this.lookahead = lookahead;
        this.curvature = curvature;
        this.speedUsed = speedUsed;
        this.actualSpeed = actualSpeed;
        this.time = time;
        this.robotHeading = robotHeading;
    }

    public float getRobotHeading()
    {
        return robotHeading;
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

    public float getSpeedUsed()
    {
        return speedUsed;
    }

    public float getActualSpeed()
    {
        return actualSpeed;
    }

    public long getTime()
    {
        return time;
    }
}
