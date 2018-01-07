package com.team2502.robot2018.data;

import com.team2502.robot2018.utils.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector
{

    private final double[] x;

    public Vector(double... x){
        this.x = x;
    }

    public Vector add(Vector other) {
        if(other.x.length != x.length) {
            try {
                throw new IllegalAccessException();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        double[] vals = new double[x.length];
        for(int i =0; i < x.length; i++){
            vals[i]=x[i]+other.x[i];
        }
        return new Vector(vals);
    }

    public static List<Vector> genFromArray(double[][] vectorArray){
        if(vectorArray.length == 0 || vectorArray[0].length == 0)
            return new ArrayList<>();
        List<Vector> vectorList = new ArrayList<>();
        for (double[] vectorElements : vectorArray)
        {
            vectorList.add(new Vector(vectorElements));
        }
        return vectorList;
    }

    public boolean between(Vector a, Vector c){
        for(int i = 0; i < dimensions(); i++){
            if(!MathUtils.Algebra.between(a.get(i),get(i),c.get(i)))
                return false;
        }
        return true;
    }

    public Vector subtractBy(Vector other) {
        return add(other.clone().multiply(-1));
    }

    public Vector multiply(double scalar){
        double[] vals = new double[x.length];
        for(int i =0; i<x.length; i++){
            vals[i]=x[i]*scalar;
        }
        return new Vector(vals);
    }

    public int dimensions(){
        return x.length;
    }

    public double getMagnitude(){
        return Math.sqrt(getMagnitudeSquared());
    }

    public double getMagnitudeSquared(){
        double xsum = 0;
        for(double xi : x){
            xsum+=xi*xi;
        }
        return xsum;
    }

    public double get(int place){
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
    public String toString() {
        return "Vector{" +
                "x=" + Arrays.toString(x) +
                '}';
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(x);
    }

    @Override
    public Vector clone() {
        return new Vector(x);
    }
}
