package com.team2502.robot2018.utils;

import com.team2502.robot2018.data.Vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MathUtils
{
    public static class LinearAlgebra{
        public static Vector rotate2D(Vector vector, double theta){
            return new Vector(
                    vector.get(0) * Math.cos(theta) - vector.get(1) * Math.sin(theta),
                    vector.get(0) * Math.sin(theta) + vector.get(1) * Math.cos(theta));
        }

        public static Vector absoluteToRelativeCoord(Vector relativeCoord, Vector absoluteLocation, double robotHeading){
            if(relativeCoord.dimensions() != 2)
                throw new IllegalArgumentException("Must be in R2");
            Vector coordDif = relativeCoord.clone().subtractBy(absoluteLocation);
            Vector toReturn = MathUtils.LinearAlgebra.rotate2D(coordDif, -robotHeading);
            return toReturn;
        }
    }
    public static class Algebra {
        /**
         *
         * @param a
         * @param b
         * @param c
         * @return if a <= b <= c or c<= b <= a
         */
        public static boolean between(double a, double b, double c){
            return (a <= b && b <= c) || (c <= b && b <= a);
        }

        public static boolean positiveMultiplication(double a, double b){
            if( a >= 0 && b >= 0)
                return true;
            if( a < 0 && b < 0 )
                return true;
            return false;
        }
    }
    public static class Arithmetic{
        public static double sign(double number){
            if(number >= 0)
                return 1;
            return -1;
        }
    }

    public static class Geometry{

        public static List<Vector> getCircleLineIntersectionPoint(Vector pointA,
                                                    Vector pointB, Vector center, double radius) {
            double baX = pointB.get(0) - pointA.get(0);
            double baY = pointB.get(1) - pointA.get(1);
            double caX = center.get(0) - pointA.get(0);
            double caY = center.get(1) - pointA.get(1);

            double a = baX * baX + baY * baY;
            double bBy2 = baX * caX + baY * caY;
            double c = caX * caX + caY * caY - radius * radius;

            double pBy2 = bBy2 / a;
            double q = c / a;

            double disc = pBy2 * pBy2 - q;
            if (disc < 0) {
                return Collections.emptyList();
            }
            // if disc == 0 ... dealt with later
            double tmpSqrt = Math.sqrt(disc);
            double abScalingFactor1 = -pBy2 + tmpSqrt;
            double abScalingFactor2 = -pBy2 - tmpSqrt;

            Vector p1 = new Vector(pointA.get(0) - baX * abScalingFactor1, pointA.get(1)
                    - baY * abScalingFactor1);
            if (disc == 0) { // abScalingFactor1 == abScalingFactor2
                return Collections.singletonList(p1);
            }
            Vector p2 = new Vector(pointA.get(0) - baX * abScalingFactor2, pointA.get(1)
                    - baY * abScalingFactor2);
            return Arrays.asList(p1, p2);
        }

        public static Vector[] circleLineIntersect(Vector lineP1, Vector lineP2, Vector circleCenter, double circleRadius){

            // Circle-line intersection
            double x_0=lineP1.get(0), y_0=lineP1.get(1);
            double x_1=lineP2.get(0), y_1=lineP2.get(1);
            double x_c=circleCenter.get(0),y_c=circleCenter.get(1);

            double f = x_1 - x_0;
            double g = y_1 - y_0;

            double t = f*(x_c-x_0)+g*(y_c-y_0);
            double inRoot = circleRadius * circleRadius +(f*f+g*g)-Math.pow(f*(y_c-y_0)-g*(x_c-x_0),2);
            if(inRoot < 0)
                return new Vector[0];
            double denominator = (f*f + g*g);
            if(inRoot == 0){
                double intersectT = t / denominator;
                Vector intersection = new Vector(intersectT * f, intersectT * g);
                if(intersection.between(lineP1,lineP2))
                    return new Vector[]{intersection};
                else
                    return new Vector[0];
            }
            double pm = Math.sqrt(inRoot);
            double intersectT1 = (t+pm)/denominator;
            double intersectT2 = (t-pm)/denominator;
            Vector intersect1 = new Vector(intersectT1 * f+x_0, intersectT1 * g+y_0);
            Vector intersect2 = new Vector(intersectT2 * f+x_0, intersectT2 * g+y_0);
            if(intersect1.between(lineP1,lineP2)){
                if(intersect2.between(lineP1,lineP2)){
                    return new Vector[]{
                            intersect1,
                            intersect2
                    };
                }
                else{
                    return new Vector[]{
                            intersect1
                    };
                }
            }
            else{
                if(intersect2.between(lineP1,lineP2)){
                    return new Vector[]{
                            intersect2
                    };
                }
            }
            return new Vector[0];
        }
    }
}
