package com.team2502.robot2018.pathplanning.purepursuit;

import org.joml.ImmutableVector2f;

import java.util.Objects;

public class Point
{

    private ImmutableVector2f location;

    public Point(ImmutableVector2f location)
    {
        this.location = location;
    }

    public Point(float x, float y)
    {
        this(new ImmutableVector2f(x,y));
    }

    public ImmutableVector2f getLocation()
    {
        return location;
    }

    public void setLocation(ImmutableVector2f location)
    {
        this.location = location;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) { return true; }
        if(o == null || getClass() != o.getClass()) { return false; }
        Point point = (Point) o;
        return Objects.equals(location, point.location);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(location);
    }

    @Override
    public String toString()
    {
        return "Point{" +
               "location=" + location +
               '}';
    }
}
