package com.team2502.robot2018.utils;


import com.team2502.robot2018.Robot;
import org.joml.ImmutableVector2f;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public final class MathUtils
{
    public static final double PHI = 1.618033989D;
    public static final float PHI_F = 1.618033989F;

    public static final double ROOT_2 = 1.414213562D;
    public static final double ROOT_3 = 1.732050808D;

    public static final float ROOT_2_F = 1.414213562F;
    public static final float ROOT_3_F = 1.732050808F;

    public static final double TAU_D = 2 * Math.PI;
    public static final float TAU = 2 * (float) Math.PI;

    public static final float PI_F = (float) Math.PI;

    public static final ImmutableVector2f VECTOR_STRAIGHT = new ImmutableVector2f(0, 1);

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

    private MathUtils() { }

    public static boolean signSame(float x, float y)
    { return ((Float.floatToIntBits(x) & 0x80000000) == (Float.floatToIntBits(y) & 0x80000000)); }

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

    public static boolean between(final ImmutableVector2f a, final ImmutableVector2f x, final ImmutableVector2f c)
    {
        return Algebra.between(a.get(0), x.get(0), c.get(0)) && Algebra.between(a.get(1), x.get(1), c.get(1));
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

    //region Logarithmic Functions

    /**
     * Allows for the calculate of logX(in), may have minor performance boost from using direct call to StrictMath lowering stack overhead.
     *
     * @param base The base of the log.
     * @param in   The value to find the log of.
     * @return The logX(in)
     */
    public static double log(final double base, final double in)
    { return StrictMath.log(in) / StrictMath.log(base); }

    /**
     * Allows for the calculate of logX(in), may have minor performance boost from using direct call to StrictMath lowering stack overhead.
     *
     * @param base The base of the log.
     * @param in   The value to find the log of.
     * @return The logX(in)
     */
    public static double logX(final double base, final double in)
    { return StrictMath.log(in) / StrictMath.log(base); }

    /**
     * Use the predefined square log instead of a custom implementation.
     *
     * @param in The value to find the log of.
     * @return The log2(in)
     */
    public static double log2(final double in)
    { return StrictMath.log(in) / 0.6931471806D; }

    /**
     * Use the predefined cube log instead of a custom implementation.
     *
     * @param in The value to find the log of.
     * @return The log3(in)
     */
    public static double log3(final double in)
    { return StrictMath.log(in) / 1.098612289D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log4(in)
     */
    public static double log4(final double in)
    { return StrictMath.log(in) / 1.386294361D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log5(in)
     */
    public static double log5(final double in)
    { return StrictMath.log(in) / 1.609437912D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log6(in)
     */
    public static double log6(final double in)
    { return StrictMath.log(in) / 2.791759469D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log7(in)
     */
    public static double log7(final double in)
    { return StrictMath.log(in) / 2.945910149D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log8(in)
     */
    public static double log8(final double in)
    { return StrictMath.log(in) / 2.079441542D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log9(in)
     */
    public static double log9(final double in)
    { return StrictMath.log(in) / 2.197224577D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the log of.
     * @return The log10(in)
     */
    public static double log10(final double in)
    { return StrictMath.log10(in); }

    /**
     * Because why not.
     *
     * @param in The value to find the log of.
     * @return The logPi(in)
     */
    public static double logPi(final double in)
    { return StrictMath.log(in) / 1.144729886D; }

    /**
     * Calculates the natural logarithm (base e).
     *
     * @param in The value to find the log of.
     * @return The ln(in)
     */
    public static double loge(final double in)
    { return StrictMath.log(in); }

    /**
     * Calculates the natural logarithm (base e).
     *
     * @param in The value to find the log of.
     * @return The ln(in)
     */
    public static double ln(final double in)
    { return StrictMath.log(in); }

    //region Exponentiation Functions
    public static double pow2(final double x)
    { return x * x; }

    public static double pow3(final double x)
    { return x * x * x; }

    public static double pow4(final double x)
    { return x * x * x * x; }

    public static double pow5(final double x)
    { return x * x * x * x * x; }

    public static double pow6(final double x)
    { return x * x * x * x * x * x; }

    public static double pow7(final double x)
    { return x * x * x * x * x * x * x; }

    public static double pow8(final double x)
    { return x * x * x * x * x * x * x * x; }

    public static double pow9(final double x)
    { return x * x * x * x * x * x * x * x * x; }

    public static double pow10(final double x)
    { return x * x * x * x * x * x * x * x * x * x; }
    //endregion

    public static float pow2f(final float x)
    { return x * x; }

    public static float pow3f(final float x)
    { return x * x * x; }

    public static float pow4f(final float x)
    { return x * x * x * x; }

    public static float pow5f(final float x)
    { return x * x * x * x * x; }

    public static float pow6f(final float x)
    { return x * x * x * x * x * x; }

    public static float pow7f(final float x)
    { return x * x * x * x * x * x * x; }

    public static float pow8f(final float x)
    { return x * x * x * x * x * x * x * x; }

    public static float pow9f(final float x)
    { return x * x * x * x * x * x * x * x * x; }

    public static float pow10f(final float x)
    { return x * x * x * x * x * x * x * x * x * x; }
    //endregion

    public static class LinearAlgebra
    {
        public static ImmutableVector2f rotate2D(ImmutableVector2f vector, float theta)
        {
            float sin = sin(theta);
            float cos = cos(theta);
            return new ImmutableVector2f((vector.get(0) * cos - vector.get(1) * sin),
                                         (vector.get(0) * sin + vector.get(1) * cos));
        }

        public static ImmutableVector2f absoluteToRelativeCoord(ImmutableVector2f relativeCoord, ImmutableVector2f absoluteLocation, float robotHeading)
        { return rotate2D(relativeCoord.sub(absoluteLocation), -robotHeading); }
    }

    public static class Algebra
    {

        public static Set<Float> quadratic(float a, float b, float c)
        {
            Set<Float> toReturn = new HashSet<>();
            float discriminate = discriminate(a, b, c);
            if(discriminate < 0)
            {
                return toReturn;
            }
            else if(discriminate == 0)
            {
                toReturn.add(b * b / (2 * a));
            }
            else
            {
                float LHS = -b / (2 * a);
                float RHS = (float) (Math.sqrt(discriminate) / (2 * a));
                toReturn.add(LHS + RHS);
                toReturn.add(LHS - RHS);
            }
            return toReturn;
        }

        public static float discriminate(float a, float b, float c)
        {
            return b * b - 4F * a * c;
        }

        /**
         * @return if a <= x <= b or b<= x <= a
         */
        public static boolean between(final float a, final float x, final float b)
        {
            return bounded(a, x, b) || bounded(b, x, a);
        }

        // TODO: between and bounded are probably not the best names
        public static boolean bounded(final float a, final float x, final float b)
        {
            return a <= x && x <= b;
        }

        /**
         * @param a
         * @param b
         * @return
         * @deprecated Use {@link Math#signum(float)}
         */
        public static boolean positiveMultiplication(final float a, final float b)
        {
            return a >= 0 && b >= 0 || a < 0 && b < 0;
        }
    }

    public static class Kinematics
    {

        /**
         * Get the 1D position of the robot given p0, v0, a0, and dt. Uses elementary physics formulas.
         *
         * @param p0
         * @param v0
         * @param a0
         * @param dt
         * @return
         */
        public static float getPos(float p0, float v0, float a0, float dt)
        {
            return p0 + v0 * dt + 1 / 2F * a0 * dt * dt;
        }

        public static float getAngularVel(float vL, float vR, float l)
        {
            return (vR - vL) / l;
        }

        public static float getTrajectoryRadius(float vL, float vR, float l)
        {
            return (l * (vR + vL)) / (2 * (vR - vL));
        }

        public static ImmutableVector2f getRelativeDPos(float vL, float vR, float l, float dt)
        {
            // To account for an infinite trajectory radius when going straight
            if(Math.abs(vL - vR) <= (vL + vR) * 1E-2)
            {
                // Probably average is not needed, but it may be useful over long distances
                return new ImmutableVector2f(0, (vL + vR) / 2F * dt);
            }
            float w = getAngularVel(vL, vR, l);
            float dTheta = w * dt;

            float r = getTrajectoryRadius(vL, vR, l);

            float dxRelative = -r * (1 - MathUtils.cos(-dTheta));
            float dyRelative = -r * MathUtils.sin(-dTheta);

            return new ImmutableVector2f(dxRelative, dyRelative);
        }

        public static float getTangentialSpeed(float wheelL, float wheelR)
        {
            return (wheelL + wheelR)/2F;
        }

        /**
         * @return
         * @deprecated
         */
        public static float getHeadingAbsolute()
        {
            float navx = (float) Robot.NAVX.getAngle();
            float heading = Geometry.getDTheta(0, navx);
            return heading;
        }

        public static ImmutableVector2f getAbsoluteDPos(float vL, float vR, float l, float dt, float robotHeading)
        {
            ImmutableVector2f relativeDPos = getRelativeDPos(vL, vR, l, dt);
            ImmutableVector2f rotated = MathUtils.LinearAlgebra.rotate2D(relativeDPos, robotHeading);
            return rotated;
        }
    }

    public static class Geometry
    {
        /**
         * @param initDegrees
         * @param finalDegrees
         * @return the difference in radians between the two degrees from [0,2pi). Increases counterclockwise.
         */
        public static float getDTheta(float initDegrees, float finalDegrees)
        {
//            System.out.println("Init degrees "+ initDegrees);
            float degDif = -(finalDegrees - initDegrees);
            double radians = MathUtils.deg2Rad(degDif);
            double radBounded = (radians % TAU);
//            System.out.println("\nNAVX: " + finalDegrees + "\nNAVXD: " + degDif + "\nRadians: " + radians + "\nRadBounded: " + radBounded + "\n");
            if(radBounded < 0) { return (float) (TAU + radBounded); }
            return (float) radBounded;
        }

        public static ImmutableVector2f getClosestPoint(ImmutableVector2f linePointA, ImmutableVector2f linePointB, ImmutableVector2f robotPos)
        {
            // Let s:[0,1]
            // Our line is
            // Dx = (linePointB.x - linePointA.x)s
            // Dy = (linePointB.y - linePointA.y)s
            // This is a transformation from R1 -> R2 we have [ x y ]
            // [ Dx ]   [ linePointB.x - linePointA.x ]
            // [ Dy ] = [ linePointB.y - linePointA.y ][ s ]

            float dx = linePointB.x - linePointA.x;
            float dy = linePointB.y - linePointA.y;

            // Let us assume the shortest distance to a line will be perpendicular to the line. Applying a pi/2 rotation yields
            // [ DxPerp ]   [ cos(pi/2) -sin(pi/2) ] [ linePointB.x - linePointA.x ]
            // [ DyPerp ] = [ sin(pi/2) cos(pi/2)  ] [ linePointB.y - linePointA.y ][ s ]
            //   [ linePointA.y - linePointB.y ]
            // = [ linePointB.x - linePointA.x ][ s ]

            float dxPerp = -dy;
            float dyPerp = dx;

            // We need the point where lines
            // robotPos.x + dxPerp * j
            // robotPos.y + dyPerp*j
            // and
            // linePointB.x + dX * s
            // linePointB.y + dX * s
            // collide

            // robotPos.x + dxPerp * j = linePointB.x + dX * s ==> robotPos.x - linePointB.x
            // robotPos.y + dyPerp * j = linePointB.y + dY * s

            float j, s;
            if(dx == 0)
            {
                j = -(robotPos.x - linePointA.x) / dxPerp;
                s = ((robotPos.y - linePointA.y) + dyPerp * j) / dy;
            }
            else if(dy == 0)
            {
                j = -(robotPos.y - linePointB.y) / dyPerp;
                s = ((robotPos.x - linePointA.x) + dxPerp * j) / dx;
            }
            else
            {
                float slope = dy / dx;
                j = (robotPos.x - linePointA.x - (robotPos.y - linePointA.y)) / (-slope * dxPerp + dyPerp);
                s = (robotPos.x + dxPerp) * j / (robotPos.x + dx);
            }

            // if on line
            if(s >= 0 && s <= 1)
            {
                return linePointA.add(new ImmutableVector2f(dx * s, dy * s));
            }
            // else ... we cannot use a perpendicular line and will need to look on endpoints
            float dist2A = linePointA.distanceSquared(robotPos);
            float dist2B = linePointB.distanceSquared(robotPos);
            return dist2A < dist2B ? linePointA : linePointB;
        }

        public static ImmutableVector2f getVector(float speed, float angle)
        {
            return MathUtils.LinearAlgebra.rotate2D(VECTOR_STRAIGHT, angle);
        }

        public static ImmutableVector2f[] getCircleLineIntersectionPoint(ImmutableVector2f pointA, ImmutableVector2f pointB, ImmutableVector2f center, double radius)
        {
            float baX = pointB.get(0) - pointA.get(0);
            float baY = pointB.get(1) - pointA.get(1);

            float caX = center.get(0) - pointA.get(0);
            float caY = center.get(1) - pointA.get(1);

            float a = baX * baX + baY * baY;
            float bBy2 = baX * caX + baY * caY;
            double c = caX * caX + caY * caY - radius * radius;

            float pBy2 = bBy2 / a;
            double q = c / a;

            double disc = pBy2 * pBy2 - q;
            if(disc < 0) { return new ImmutableVector2f[0]; }
            // if disc == 0 ... dealt with later
            float tmpSqrt = (float) Math.sqrt(disc);
            float abScalingFactor1 = tmpSqrt - pBy2;

            ImmutableVector2f p1 = new ImmutableVector2f(pointA.get(0) - baX * abScalingFactor1, pointA.get(1) - baY * abScalingFactor1);
            if(disc == 0) { return new ImmutableVector2f[] { p1 }; }

            float abScalingFactor2 = -pBy2 - tmpSqrt;
            ImmutableVector2f p2 = new ImmutableVector2f(pointA.get(0) - baX * abScalingFactor2, pointA.get(1) - baY * abScalingFactor2);
            return new ImmutableVector2f[] { p1, p2 };
        }

        public static ImmutableVector2f[] circleLineIntersect(ImmutableVector2f lineP1, ImmutableVector2f lineP2, ImmutableVector2f circleCenter, float circleRadius)
        {
            // Circle-line intersection
            float x_0 = lineP1.get(0), y_0 = lineP1.get(1);
            float x_1 = lineP2.get(0), y_1 = lineP2.get(1);
            float x_c = circleCenter.get(0), y_c = circleCenter.get(1);

            float f = x_1 - x_0;
            float g = y_1 - y_0;

            float xc0 = x_c - x_0;
            float yc0 = y_c - y_0;

            float t = f * xc0 + g * yc0;

            float fg2 = f * f + g * g;

            float inRoot = (circleRadius * circleRadius + fg2 - pow2f(f * yc0 - g * xc0));
            if(inRoot < 0) { return new ImmutableVector2f[0]; }
            if(inRoot == 0)
            {
                float intersectT = t / fg2;
                ImmutableVector2f intersection = new ImmutableVector2f(intersectT * f, intersectT * g);
                if(between(lineP1, intersection, lineP2)) { return new ImmutableVector2f[] { intersection }; }
                else { return new ImmutableVector2f[0]; }
            }
            float pm = (float) Math.sqrt(inRoot);
            float intersectT1 = (t + pm) / fg2;
            float intersectT2 = (t - pm) / fg2;
            ImmutableVector2f intersect1 = new ImmutableVector2f(intersectT1 * f + x_0, intersectT1 * g + y_0);
            ImmutableVector2f intersect2 = new ImmutableVector2f(intersectT2 * f + x_0, intersectT2 * g + y_0);
            if(between(lineP1, intersect1, lineP2))
            {
                if(between(lineP1, intersect2, lineP2)) { return new ImmutableVector2f[] { intersect1, intersect2 }; }
                else { return new ImmutableVector2f[] { intersect1 }; }
            }
            else if(between(lineP1, intersect2, lineP2)) { return new ImmutableVector2f[] { intersect2 }; }
            return new ImmutableVector2f[0];
        }
    }
}
