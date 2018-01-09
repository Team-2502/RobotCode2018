package com.team2502.robot2018.utils;

import com.team2502.robot2018.data.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathUtils
{
    public static class LinearAlgebra
    {
        public static Vector rotate2D(Vector vector, float theta)
        {
            return new Vector(
                    (float) (vector.get(0) * Math.cos(theta) - vector.get(1) * Math.sin(theta)),
                    (float) (vector.get(0) * Math.sin(theta) + vector.get(1) * Math.cos(theta)));
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
        public static boolean between(float a, float b, float c)
        {
            return (a <= b && b <= c) || (c <= b && b <= a);
        }

        public static boolean positiveMultiplication(float a, float b)
        {
            return a >= 0 && b >= 0 || a < 0 && b < 0;
        }
    }

    public static class Arithmetic
    {
        public static float sign(float number)
        {
            return number >= 0 ? 1 : -1;
        }
    }

    public static class Geometry
    {

        public static List<Vector> getCircleLineIntersectionPoint(Vector pointA, Vector pointB, Vector center, double radius)
        {
            System.out.println("A: " + pointA.toString());
            System.out.println("B: " + pointB.toString());
            System.out.println("Center: " + center.toString());
            System.out.println("radius: " + radius);

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
}
