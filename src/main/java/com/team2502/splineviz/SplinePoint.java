package com.team2502.splineviz;

import com.team2502.robot2018.pathplanning.purepursuit.Point;
import javafx.beans.property.SimpleFloatProperty;
import org.joml.ImmutableVector2f;

import java.util.Objects;

public class SplinePoint extends Point
{
    private ImmutableVector2f tangentVec;

    public SimpleFloatProperty posX = new SimpleFloatProperty();
    public SimpleFloatProperty posY = new SimpleFloatProperty();
    public SimpleFloatProperty tanX = new SimpleFloatProperty();
    public SimpleFloatProperty tanY = new SimpleFloatProperty();

    public SplinePoint(float x, float y, float tanX, float tanY)
    {
        super(x, y);
        tangentVec = new ImmutableVector2f(tanX, tanY);

        this.posX.set(getLocation().x);
        this.posY.set(getLocation().y);

        this.tanX.set(getTangentVec().x);
        this.tanY.set(getTangentVec().y);
    }

    public SplinePoint(ImmutableVector2f location, ImmutableVector2f tangentVec)
    {
        super(location);
        this.tangentVec = tangentVec;

        this.posX.set(getLocation().x);
        this.posY.set(getLocation().y);

        this.tanX.set(getTangentVec().x);
        this.tanY.set(getTangentVec().y);
    }

    public ImmutableVector2f getTangentVec()
    {
        return tangentVec;
    }

    public void setTangentVec(ImmutableVector2f tangentVec)
    {
        this.tangentVec = tangentVec;


        this.tanX.set(getTangentVec().x);
        this.tanY.set(getTangentVec().y);
    }


    @Override
    public void setLocation(ImmutableVector2f location)
    {
        super.setLocation(location);
        this.posX.set(getLocation().x);
        this.posY.set(getLocation().y);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) { return true; }
        if(o == null || getClass() != o.getClass()) { return false; }
        SplinePoint point = (SplinePoint) o;
        return Objects.equals(getLocation(), point.getLocation()) && Objects.equals(tangentVec, point.tangentVec);
    }

    @Override
    public int hashCode()
    {
        int a = Objects.hashCode(getLocation());
        int b = Objects.hashCode(tangentVec);

        // This is a pairing function.
        return a >= b ? a * a + a + b : a + b * b;
    }

    @Override
    public String toString()
    {
        return "SplinePoint{" +
                "location=" + getLocation() +
                ", tangent=" + tangentVec +
                '}';
    }
}
