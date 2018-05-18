package com.team2502.robot2018.trajectory.record;

import com.team2502.robot2018.utils.CSVAble;
import org.joml.ImmutableVector2f;

public class PurePursuitFrame implements CSVAble
{


    private final double timeElapsedSeconds;
    private final float robotX;
    private final float robotY;
    private final double usedLookahead;
    private final float usedHeading;
    private final float goalPointX;
    private final float goalPointY;
    private final double radius;

    private final float circleCenterX;
    private final float circleCenterY;
    private final int currentSegmentIndex;
    private final float closestPointX;
    private final float closestPointY;
    private final float dCP;

    public PurePursuitFrame(double timeElapsedSeconds, ImmutableVector2f robotPos, double usedLookahead, float usedHeading, ImmutableVector2f goalPoint, double radius, ImmutableVector2f circleCenter, int currentSegmentIndex, ImmutableVector2f closestPoint)
    {

        this.timeElapsedSeconds = timeElapsedSeconds;
        this.robotX = robotPos.x;
        this.robotY = robotPos.y;
        this.usedLookahead = usedLookahead;
        this.usedHeading = usedHeading;
        this.goalPointX = goalPoint.x;
        this.goalPointY = goalPoint.y;
        this.radius = radius;
        this.circleCenterX = circleCenter.x;
        this.circleCenterY = circleCenter.y;
        this.currentSegmentIndex = currentSegmentIndex;
        this.closestPointX = closestPoint.x;
        this.closestPointY = closestPoint.y;
        this.dCP = robotPos.distance(closestPoint);
    }

    public double getTimeElapsedSeconds()
    {
        return timeElapsedSeconds;
    }

    public float getRobotX()
    {
        return robotX;
    }

    public float getRobotY()
    {
        return robotY;
    }

    public double getUsedLookahead()
    {
        return usedLookahead;
    }

    public float getUsedHeading()
    {
        return usedHeading;
    }

    public float getGoalPointX()
    {
        return goalPointX;
    }

    public float getGoalPointY()
    {
        return goalPointY;
    }

    public double getRadius()
    {
        return radius;
    }

    public float getCircleCenterX()
    {
        return circleCenterX;
    }

    public float getCircleCenterY()
    {
        return circleCenterY;
    }

    public int getCurrentSegmentIndex()
    {
        return currentSegmentIndex;
    }

    public float getClosestPointX()
    {
        return closestPointX;
    }

    public float getClosestPointY()
    {
        return closestPointY;
    }

    public float getdCP()
    {
        return dCP;
    }


    public String toCSV()
    {
        return timeElapsedSeconds + ", " +  robotX + ", " +  robotY + ", " + usedLookahead + ", " + usedHeading + ", " + goalPointX + ", " + goalPointY + ", " + radius + ", " + circleCenterX + ", " + circleCenterY + ", " + currentSegmentIndex + ", " + closestPointX + ", " + closestPointY + ", " + dCP;

    }
}
