package com.team2502.robot2018.data;

import com.team2502.robot2018.utils.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class Vector
{
    private final float[] elements;

    public Vector(float... elements)
    { this.elements = elements; }

    public static List<Vector> genFromArray(float[][] vectorArray)
    {
        if(vectorArray.length == 0 || vectorArray[0].length == 0) { return new ArrayList<>(); }
        List<Vector> vectorList = new ArrayList<>();
        for(float[] vectorElements : vectorArray) { vectorList.add(new Vector(vectorElements)); }
        return vectorList;
    }

    public Vector add(Vector other) throws IndexOutOfBoundsException, NullPointerException
    {
        if(other.elements.length != elements.length) { throw new IndexOutOfBoundsException("Vector lengths must be equal."); }

        float[] vals = new float[elements.length];
        for(int i = 0; i < elements.length; ++i) { vals[i] = elements[i] + other.elements[i]; }
        return new Vector(vals);
    }

    public boolean between(Vector a, Vector c)
    {
        for(int i = 0; i < dimensions(); ++i)
        {
            if(!MathUtils.Algebra.between(a.get(i), get(i), c.get(i))) { return false; }
        }
        return true;
    }

    public Vector multiply(float scalar)
    {
        float[] vals = new float[elements.length];
        for(int i = 0; i < elements.length; ++i)
        { vals[i] = elements[i] * scalar; }
        return new Vector(vals);
    }

    public Vector subtractBy(Vector other)
    { return add(other.clone().multiply(-1)); }

    public int dimensions()
    { return elements.length; }

    public float getMagnitudeSquared()
    {
        float xsum = 0.0F;
        for(float xi : elements) { xsum += xi * xi; }
        return xsum;
    }

    public float getMagnitude()
    { return (float) Math.sqrt(getMagnitudeSquared()); }

    public float get(int place)
    { return elements[place]; }

    @Override
    public boolean equals(Object o)
    {
        if(this == o) { return true; }
        if(o == null || getClass() != o.getClass()) { return false; }
        Vector vector = (Vector) o;
        return Arrays.equals(elements, vector.elements);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString()
    {
        String vector = "(";
        for(int i = 0; i < elements.length; i++)
        {
            String toAppend = String.valueOf(elements[i]);
            vector += toAppend;
            if(i != elements.length - 1)
            {
                vector += ",";
            }
        }
        vector += ")";
        return vector;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Vector clone()
    {
        return new Vector(elements);
    }
}
