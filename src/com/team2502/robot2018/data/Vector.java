package com.team2502.robot2018.data;

import com.team2502.robot2018.utils.MathUtils;

import java.util.Arrays;

public class Vector
{

    private final float[] x;

    public Vector(float... x){
        this.x = x;
    }

    public Vector add(Vector other) {
        if(other.x.length != x.length) {
            try {
                throw new IllegalAccessException("Vectors must be of same dimension");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        float[] vals = new float[x.length];
        for(int i =0; i < x.length; i++){
            vals[i]=x[i]+other.x[i];
        }
        return new Vector(vals);
    }

    public boolean between(Vector a, Vector c){
        for(int i = 0; i < dimensions(); i++){
            if(!MathUtils.Algebra.between(a.get(i), get(i), c.get(i)))
                return false;
        }
        return true;
    }

    public Vector subtractBy(Vector other) {
        return add(other.clone().multiply(-1));
    }

    public Vector multiply(float scalar){
        float[] vals = new float[x.length];
        for(int i =0; i<x.length; i++){
            vals[i]=x[i]*scalar;
        }
        return new Vector(vals);
    }

    public int dimensions(){
        return x.length;
    }

    public float getMagnitude(){
        return (float) Math.sqrt(getMagnitudeSquared());
    }

    public float getMagnitudeSquared(){
        float xsum = 0;
        for(float xi : x){
            xsum+=xi*xi;
        }
        return xsum;
    }

    public float get(int place){
        return x[place];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Arrays.equals(x, vector.x);
    }

    @Override
    public String toString()
    {
        return "Vector{" +
               "x=" + Arrays.toString(x) +
               '}';
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(x);
    }

    @Override
    protected Vector clone() {
        return new Vector(x);
    }
}
