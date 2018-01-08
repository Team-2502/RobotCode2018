package com.team2502.robot2018.data;

import com.team2502.robot2018.utils.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector
{

    private final double[] elements;

    public Vector(double... elements)
    { this.elements = elements; }

    public Vector add(Vector other) throws IndexOutOfBoundsException
    {
        if(other.elements.length != elements.length) { throw new IndexOutOfBoundsException("Vector lengths must be equal."); }
        double[] vals = new double[elements.length];
        for(int i = 0; i < elements.length; ++i) { vals[i] = elements[i] + other.elements[i]; }
        return new Vector(vals);
    }

    public static List<Vector> genFromArray(double[][] vectorArray)
    {
        if(vectorArray.length == 0 || vectorArray[0].length == 0) { return new ArrayList<>(); }
        List<Vector> vectorList = new ArrayList<>();
        for(double[] vectorElements : vectorArray) { vectorList.add(new Vector(vectorElements)); }
        return vectorList;
    }

    public boolean between(Vector a, Vector c)
    {
        for(int i = 0; i < dimensions(); ++i)
        {
            if(!MathUtils.Algebra.between(a.get(i), get(i), c.get(i))) { return false; }
        }
        return true;
    }

    public Vector multiply(double scalar)
    {
        double[] vals = new double[elements.length];
        for(int i = 0; i < elements.length; ++i)
        { vals[i] = elements[i] * scalar; }
        return new Vector(vals);
    }

    public Vector subtractBy(Vector other)
    { return add(other.clone().multiply(-1)); }

    public int dimensions()
    { return elements.length; }

    public double getMagnitudeSquared()
    {
        double xsum = 0.0D;
        for(double xi : elements) { xsum += xi * xi; }
        return xsum;
    }

    public double getMagnitude()
    { return Math.sqrt(getMagnitudeSquared()); }

    public double get(int place)
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
        return new StringBuilder().append("Vector: { \"elements\"=").append(Arrays.toString(elements)).append(" }").toString();
    }

    @Override
    public Vector clone()
    {
        return new Vector(elements);
    }
}
