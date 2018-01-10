package com.team2502.robot2018.utils;

import com.team2502.robot2018.data.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MathUtils
{
    public static final double PHI = 1.618033989D;
    public static final float PHI_F = 1.618033989F;

    public static final double ROOT_2 = 1.414213562D;
    public static final double ROOT_3 = 1.732050808D;

    public static final float ROOT_2_F = 1.414213562F;
    public static final float ROOT_3_F = 1.732050808F;

    /**
     * A table of sin values computed from 0 (inclusive) to 2π (exclusive), with steps of 2π / 65536.
     */
    private static final float[] SIN_TABLE = new float[65536];

    static
    {
        for(int i = 0; i < 65536; ++i) { SIN_TABLE[i] = (float) Math.sin(((double) i) * Math.PI * 2.0D / 65536.0D); }

        SIN_TABLE[0] = 0;       /* 0π */
        SIN_TABLE[16384] = 1;   /* π/2 */
        SIN_TABLE[32768] = 0;   /* 2π */
        SIN_TABLE[49152] = -1;  /* 3π/2 */
    }

    /**
     * sin looked up in a table
     */
    public static float sin(final float value)
    { return SIN_TABLE[(int) (value * 10430.378F) & 65535]; }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static float cos(final float value)
    { return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & 65535]; }

    public static class LinearAlgebra
    {
        public static Vector rotate2D(Vector vector, float theta)
        {
            return new Vector(
                    (float) (vector.get(0) * cos(theta) - vector.get(1) * sin(theta)),
                    (float) (vector.get(0) * sin(theta) + vector.get(1) * cos(theta)));
        }

        public static Vector absoluteToRelativeCoord(Vector relativeCoord, Vector absoluteLocation, float robotHeading)
        {
//            System.out.println(relativeCoord );
            if(relativeCoord.dimensions() != 2) { throw new IllegalArgumentException("Must be in R2"); }
            Vector coordDif = relativeCoord.clone().subtractBy(absoluteLocation);
            return LinearAlgebra.rotate2D(coordDif, -robotHeading);
        }
    }

    public static class Algebra
    {
        /**
         * @return if a <= b <= c or c<= b <= a
         */
        public static boolean between(final float a, final float b, final float c)
        {
            return (a <= b && b <= c) || (c <= b && b <= a);
        }

        public static boolean positiveMultiplication(final float a, final float b)
        {
            return a >= 0 && b >= 0 || a < 0 && b < 0;
        }
    }

    public static class Geometry
    {
        public static List<Vector> getCircleLineIntersectionPoint(Vector pointA, Vector pointB, Vector center, double radius)
        {

            float baX = pointB.get(0) - pointA.get(0);
            float baY = pointB.get(1) - pointA.get(1);

            float caX = center.get(0) - pointA.get(0);
            float caY = center.get(1) - pointA.get(1);

            float a = baX * baX + baY * baY;
            float bBy2 = baX * caX + baY * caY;
            double c = caX * caX + caY * caY - radius * radius;

            float pBy2 = bBy2 / a;
            float q = (float) c / a;

            float disc = pBy2 * pBy2 - q;
            if(disc < 0)
            {
                return Collections.emptyList();
            }
            // if disc == 0 ... dealt with later
            float tmpSqrt = (float) Math.sqrt(disc);
            float abScalingFactor1 = -pBy2 + tmpSqrt;
            float abScalingFactor2 = -pBy2 - tmpSqrt;

            Vector p1 = new Vector(pointA.get(0) - baX * abScalingFactor1, pointA.get(1)
                                                                           - baY * abScalingFactor1);
            if(disc == 0)
            { // abScalingFactor1 == abScalingFactor2
                return Collections.singletonList(p1);
            }
            Vector p2 = new Vector(pointA.get(0) - baX * abScalingFactor2, pointA.get(1)
                                                                           - baY * abScalingFactor2);
            return Arrays.asList(p1, p2);
        }

        public static Vector[] circleLineIntersect(Vector lineP1, Vector lineP2, Vector circleCenter, float circleRadius)
        {
            // Circle-line intersection
            float x_0 = lineP1.get(0), y_0 = lineP1.get(1);
            float x_1 = lineP2.get(0), y_1 = lineP2.get(1);
            float x_c = circleCenter.get(0), y_c = circleCenter.get(1);

            float f = x_1 - x_0;
            float g = y_1 - y_0;

            float t = f * (x_c - x_0) + g * (y_c - y_0);
            float inRoot = (float) (circleRadius * circleRadius + (f * f + g * g) - Math.pow(f * (y_c - y_0) - g * (x_c - x_0), 2));
            if(inRoot < 0) { return new Vector[0]; }
            float denominator = (f * f + g * g);
            if(inRoot == 0)
            {
                float intersectT = t / denominator;
                Vector intersection = new Vector(intersectT * f, intersectT * g);
                if(intersection.between(lineP1, lineP2)) { return new Vector[] { intersection }; }
                else { return new Vector[0]; }
            }
            float pm = (float) Math.sqrt(inRoot);
            float intersectT1 = (t + pm) / denominator;
            float intersectT2 = (t - pm) / denominator;
            Vector intersect1 = new Vector(intersectT1 * f + x_0, intersectT1 * g + y_0);
            Vector intersect2 = new Vector(intersectT2 * f + x_0, intersectT2 * g + y_0);
            if(intersect1.between(lineP1, lineP2))
            {
                if(intersect2.between(lineP1, lineP2)) { return new Vector[] { intersect1, intersect2 }; }
                else { return new Vector[] { intersect1 }; }
            }
            else if(intersect2.between(lineP1, lineP2)) { return new Vector[] { intersect2 }; }
            return new Vector[0];
        }
    }

    /**
     * Multiply degrees by π/180
     *
     * @param deg Number of degrees
     * @return Number of radians
     */
    public static float deg2Rad(float deg)
    { return deg * 0.01745329251994F; }

    /**
     * Multiply degrees by 180/π
     *
     * @param rad Number of radians
     * @return Number of degrees
     */
    public static float rad2Deg(float rad)
    { return rad * 57.29577951308233F; }

    /**
     * Multiply degrees by π/180
     *
     * @param deg Number of degrees
     * @return Number of radians
     */
    public static double deg2Rad(double deg)
    { return deg * 0.01745329251994D; }

    /**
     * Multiply degrees by 180/π
     *
     * @param rad Number of radians
     * @return Number of degrees
     */
    public static double rad2Deg(double rad)
    { return rad * 57.29577951308233D; }

    /**
     * Checks if two numbers are equal while accounting for
     * the possibility of a floating point error.
     *
     * @return x ~= y
     */
    public static boolean epsilonEquals(final float x, final float y)
    { return Math.abs(y - x) < 1.0E-5F; }

    /**
     * Checks if two numbers are equal while accounting for
     * the possibility of a floating point error.
     *
     * @return x ~= y
     */
    public static boolean epsilonEquals(final double x, final double y)
    { return Math.abs(y - x) < 1.0E-5D; }

    /**
     * Unchecked implementation to round a number down. Parameter should be known to be valid in advance.
     * Returns the greatest integer less than or equal to the float argument
     */
    public static int floor(final float value)
    {
        int i = (int) value;
        return value < (float) i ? i - 1 : i;
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    public static int floor(final double value)
    {
        int i = (int) value;
        return value < (double) i ? i - 1 : i;
    }

    /**
     * Long version of floor()
     */
    public static long lfloor(final double value)
    {
        long i = (long) value;
        return value < (double) i ? i - 1L : i;
    }

    /**
     * returns value cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    public static int fastFloor(final float value)
    { return ((int) (value + 1024.0F)) - 1024; }

    /**
     * returns value cast as an int, and no greater than Integer.MAX_VALUE-1024
     */
    public static int fastFloor(final double value)
    { return ((int) (value + 1024.0D)) - 1024; }

    /**
     * Gets the decimal portion of the given double. For instance, {@code frac(5.5)} returns {@code .5}.
     */
    public static double frac(final double number)
    { return number - Math.floor(number); }

    /**
     * Gets the decimal portion of the given double. For instance, {@code frac(5.5)} returns {@code .5}.
     */
    public static float frac(final float number)
    { return number - floor(number); }

    /**
     * Unchecked implementation to determine the smaller of two Floats. Parameters should be known to be valid in advance.
     */
    public static float minF(final float a, final float b)
    { return a < b ? a : b; }

    public static float minF(final int a, final float b)
    { return a < b ? a : b; }

    public static float minF(final float a, final int b)
    { return a < b ? a : b; }

    /**
     * Unchecked implementation to determine the larger of two Floats. Parameters should be known to be valid in advance.
     */
    public static float maxF(final float a, final float b)
    { return a > b ? a : b; }

    public static float maxF(final int a, final float b)
    { return a > b ? a : b; }

    public static float maxF(final float a, final int b)
    { return a > b ? a : b; }

    private MathUtils() { }
}
