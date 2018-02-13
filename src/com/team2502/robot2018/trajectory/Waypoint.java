package com.team2502.robot2018.trajectory;

import org.joml.ImmutableVector2f;

public class Waypoint
{

    private final float maxSpeed;
    private final ImmutableVector2f location;

    public Waypoint(ImmutableVector2f location, float maxSpeed)
    {
        this.location = location;
        this.maxSpeed = maxSpeed;
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
