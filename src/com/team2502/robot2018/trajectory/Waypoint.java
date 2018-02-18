package com.team2502.robot2018.trajectory;

import org.joml.ImmutableVector2f;

public class Waypoint
{

    private final float maxSpeed;
    private final ImmutableVector2f location;
    private boolean forward = false;

    public Waypoint(ImmutableVector2f location, float maxSpeed)
    {
        this.location = location;
        this.maxSpeed = maxSpeed;
    }

    public Waypoint(ImmutableVector2f location, float maxSpeed, boolean forward)
    {
        this(location,maxSpeed);
        this.forward = forward;
    }

    public float getMaxSpeed()
    {
        return maxSpeed;
    }

    public ImmutableVector2f getLocation()
    {
        return location;
    }
}
