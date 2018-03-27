package com.team2502.robot2018.utils;


import com.team2502.robot2018.Robot;
import org.joml.ImmutableVector2f;

import java.util.HashSet;
import java.util.Set;

/**
 * A class containing very many useful mathematical constants and operations
 *
 * @see MathUtils.Algebra
 * @see MathUtils.Geometry
 * @see MathUtils.LinearAlgebra
 * @see MathUtils.Kinematics
 */
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

    /**
     * @param x A number
     * @param y Another number
     * @return Returns true if numbers are same sin
     */
    public static boolean signSame(float x, float y)
    { return ((Float.floatToIntBits(x) & 0x80000000) == (Float.floatToIntBits(y) & 0x80000000)); }

    /**
     * sin looked up in a table
     */
    public static float sin(final float value)
    { return SIN_TABLE[(int) (value * 10430.378F) & 65535]; }

    /**
     * @param a a number
     * @param b another number
     * @return the number closer to 0
     */
    public static float minAbs(final float a, final float b)
    {
        return Math.abs(a) > Math.abs(b) ? b : a;
    }

    /**
     * @param a a number
     * @param b another number
     * @return the number farther from 0
     */
    public static float maxAbs(final float a, final float b)
    {
        return Math.abs(a) < Math.abs(b) ? b : a;
    }

    /**
     * cos looked up in the sin table with the appropriate offset
     */
    public static float cos(final float value)
    { return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & 65535]; }

    /**
     * @param a Lower/Upper bound
     * @param x Vector to check
     * @param c Upper/Lower bound
     * @return Returns true if x's x-component is in between that of a and c AND if x's y component is in between that of a and c.
     * @see MathUtils.Algebra#between(float, float, float)
     */
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

    public static boolean epsilonEquals(ImmutableVector2f vecA, ImmutableVector2f vecB)
    { return epsilonEquals(vecA.x, vecB.x) && epsilonEquals(vecA.y, vecB.y); }

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

    /**
     * @return the smaller number between a and b
     */
    public static float minF(final int a, final float b)
    { return a < b ? a : b; }

    /**
     * @return the smaller number between a and b
     */
    public static float minF(final float a, final int b)
    { return a < b ? a : b; }

    /**
     * Unchecked implementation to determine the larger of two Floats. Parameters should be known to be valid in advance.
     */
    public static float maxF(final float a, final float b)
    { return a > b ? a : b; }

    /**
     * @return the larger number between a and b
     */
    public static float maxF(final int a, final float b)
    { return a > b ? a : b; }

    /**
     * @return the larger number between a and b
     */
    public static float maxF(final float a, final int b)
    { return a > b ? a : b; }

    //region Logarithmic Functions

    /**
     * Allows for the calculate of logX(in), may have minor performance boost from using direct call to StrictMath lowering stack overhead.
     *
     * @param base The base of the logPop.
     * @param in   The value to find the logPop of.
     * @return The logX(in)
     */
    public static double log(final double base, final double in)
    { return StrictMath.log(in) / StrictMath.log(base); }

    /**
     * Allows for the calculate of logX(in), may have minor performance boost from using direct call to StrictMath lowering stack overhead.
     *
     * @param base The base of the logPop.
     * @param in   The value to find the logPop of.
     * @return The logX(in)
     */
    public static double logX(final double base, final double in)
    { return StrictMath.log(in) / StrictMath.log(base); }

    /**
     * Use the predefined square logPop instead of a custom implementation.
     *
     * @param in The value to find the logPop of.
     * @return The log2(in)
     */
    public static double log2(final double in)
    { return StrictMath.log(in) / 0.6931471806D; }

    /**
     * Use the predefined cube logPop instead of a custom implementation.
     *
     * @param in The value to find the logPop of.
     * @return The log3(in)
     */
    public static double log3(final double in)
    { return StrictMath.log(in) / 1.098612289D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log4(in)
     */
    public static double log4(final double in)
    { return StrictMath.log(in) / 1.386294361D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log5(in)
     */
    public static double log5(final double in)
    { return StrictMath.log(in) / 1.609437912D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log6(in)
     */
    public static double log6(final double in)
    { return StrictMath.log(in) / 2.791759469D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log7(in)
     */
    public static double log7(final double in)
    { return StrictMath.log(in) / 2.945910149D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log8(in)
     */
    public static double log8(final double in)
    { return StrictMath.log(in) / 2.079441542D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log9(in)
     */
    public static double log9(final double in)
    { return StrictMath.log(in) / 2.197224577D; }

    /**
     * Use pre calculated math for optimization.
     *
     * @param in The value to find the logPop of.
     * @return The log10(in)
     */
    public static double log10(final double in)
    { return StrictMath.log10(in); }

    /**
     * Because why not.
     *
     * @param in The value to find the logPop of.
     * @return The logPi(in)
     */
    public static double logPi(final double in)
    { return StrictMath.log(in) / 1.144729886D; }

    /**
     * Calculates the natural logarithm (base e).
     *
     * @param in The value to find the logPop of.
     * @return The ln(in)
     */
    public static double loge(final double in)
    { return StrictMath.log(in); }

    /**
     * Calculates the natural logarithm (base e).
     *
     * @param in The value to find the logPop of.
     * @return The ln(in)
     */
    public static double ln(final double in)
    { return StrictMath.log(in); }

    //region Exponentiation Functions

    /**
     * @return x ^ 2
     */
    public static double pow2(final double x)
    { return x * x; }

    /**
     * @return x ^ 3
     */
    public static double pow3(final double x)
    { return x * x * x; }

    /**
     * @return x ^ 4
     */
    public static double pow4(final double x)
    { return x * x * x * x; }

    /**
     * @return x ^ 5
     */
    public static double pow5(final double x)
    { return x * x * x * x * x; }

    /**
     * @return x ^ 6
     */
    public static double pow6(final double x)
    { return x * x * x * x * x * x; }

    /**
     * @return x ^ 7
     */
    public static double pow7(final double x)
    { return x * x * x * x * x * x * x; }

    /**
     * @return x ^ 8
     */
    public static double pow8(final double x)
    { return x * x * x * x * x * x * x * x; }

    /**
     * @return x ^ 9
     */
    public static double pow9(final double x)
    { return x * x * x * x * x * x * x * x * x; }

    /**
     * @return x ^ 10
     */
    public static double pow10(final double x)
    { return x * x * x * x * x * x * x * x * x * x; }
    //endregion

    /**
     * @return x ^ 2
     */
    public static float pow2f(final float x)
    { return x * x; }

    /**
     * @return x ^ 3
     */
    public static float pow3f(final float x)
    { return x * x * x; }

    /**
     * @return x ^ 4
     */
    public static float pow4f(final float x)
    { return x * x * x * x; }

    /**
     * @return x ^ 5
     */
    public static float pow5f(final float x)
    { return x * x * x * x * x; }

    /**
     * @return x ^ 6
     */
    public static float pow6f(final float x)
    { return x * x * x * x * x * x; }

    /**
     * @return x ^ 7
     */
    public static float pow7f(final float x)
    { return x * x * x * x * x * x * x; }

    /**
     * @return x ^ 8
     */
    public static float pow8f(final float x)
    { return x * x * x * x * x * x * x * x; }

    /**
     * @return x ^ 9
     */
    public static float pow9f(final float x)
    { return x * x * x * x * x * x * x * x * x; }

    /**
     * @return x ^ 10
     */
    public static float pow10f(final float x)
    { return x * x * x * x * x * x * x * x * x * x; }
    //endregion

    public interface Integrable
    {
        double integrate(double a, double b);

    }

    @FunctionalInterface
    public interface Function
    {
        double get(double x);
    }

    /**
     * A class containing methods pertaining to vector manipulation
     */
    public static class LinearAlgebra
    {
        /**
         * Rotate the input vector theta radians counterclockwise
         *
         * @param vector The input vector
         * @param theta  How much to rotate it by
         * @return The rotated vector
         */
        public static ImmutableVector2f rotate2D(ImmutableVector2f vector, float theta)
        {
            float sin = sin(theta);
            float cos = cos(theta);
            return new ImmutableVector2f((vector.get(0) * cos - vector.get(1) * sin),
                                         (vector.get(0) * sin + vector.get(1) * cos));
        }

        public static ImmutableVector2f absoluteToRelativeCoord(ImmutableVector2f coordinateAbsolute, ImmutableVector2f robotCoordAbs, float robotHeading)
        { return rotate2D(coordinateAbsolute.sub(robotCoordAbs), -robotHeading); }
    }

    /**
     * A class containing methods pertaining to manipulation of real numbers
     */
    public static class Algebra
    {

        /**
         * Solve for the roots of a quadratic of the form ax^2 + bx + c
         *
         * @param a x^2 coefficient
         * @param b x coefficient
         * @param c added thing
         * @return roots of the quadratic
         */
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

        /**
         * Solve for the discriminant of a quadratic of the form ax^2 + bx + c
         *
         * @param a x^2 coefficient
         * @param b x coefficient
         * @param c added thing
         * @return roots of the quadratic
         * @see MathUtils.Algebra#quadratic
         */
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


        /**
         * @param a lower bound
         * @param x some number
         * @param b upper bound
         * @return if x is between lower and upper bound
         */
        public static boolean bounded(final float a, final float x, final float b)
        {
            return a <= x && x <= b;
        }

        /**
         * @param a A number
         * @param b Another number
         * @return If they have the same sign
         * @deprecated Use {@link Math#signum(float)}
         */
        public static boolean positiveMultiplication(final float a, final float b)
        {
            return a >= 0 && b >= 0 || a < 0 && b < 0;
        }
    }

    /**
     * A class containing methods pertaining to determining our robot's movement information
     */
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

        public static ImmutableVector2f getRelativeDPosCurve(float vL, float vR, float l, float dt)
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
            return (wheelL + wheelR) / 2F;
        }

        /**
         * @return
         * @deprecated
         */
        public static float getHeadingAbsolute()
        {
            float navx = (float) Robot.NAVX.getAngle();
            return Geometry.getDTheta(0, navx);
        }

        public static ImmutableVector2f getAbsoluteDPosLine(float vL, float vR, float dt, float robotHeading)
        {
            float tangentialSpeed = getTangentialSpeed(vL, vR);
            float tangentialDPos = getTangentialSpeed(vL, vR) * dt;
            ImmutableVector2f dPos = VECTOR_STRAIGHT.mul(tangentialDPos);
            return LinearAlgebra.rotate2D(dPos, robotHeading);
        }

        public static ImmutableVector2f getAbsoluteDPosCurve(float vL, float vR, float l, float dt, float robotHeading)
        {
            ImmutableVector2f relativeDPos = getRelativeDPosCurve(vL, vR, l, dt);
            ImmutableVector2f rotated = MathUtils.LinearAlgebra.rotate2D(relativeDPos, robotHeading);
            return rotated;
        }

        /**
         * Turn NavX angle into radians
         *
         * @param yawDegTot What the NavX is reading
         * @return The angle in radians, between 0 and 2pi.
         */

        public static double navXToRad(double yawDegTot)
        {
            double yawDeg = yawDegTot % 360;
            if(yawDeg < 0) { yawDeg = 360 + yawDeg; }
            return MathUtils.deg2Rad(yawDeg);
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
            float degDif = -(finalDegrees - initDegrees);
            double radians = MathUtils.deg2Rad(degDif);
            double radBounded = (radians % TAU);
            if(radBounded < 0) { return (float) (TAU + radBounded); }
            return (float) radBounded;
        }

        /**
         * Given a line defined by two points, find the point on the line closest to our robot's position
         *
         * @param linePointA One point on the line
         * @param linePointB Another point on the line
         * @param robotPos   The point at which our robot is
         * @return The point on the line closest to the robot
         */
        public static ImmutableVector2f getClosestPoint(ImmutableVector2f linePointA, ImmutableVector2f linePointB, ImmutableVector2f robotPos)
        {

            double d1 = Math.hypot(linePointA.x - robotPos.x, linePointA.y - robotPos.y);
            double d2 = Math.hypot(linePointB.x - robotPos.x, linePointB.y - robotPos.y);

            double dPerp;

            Line lineSegment = new Line(linePointA, linePointB);

            Line linePerp = lineSegment.getPerp(robotPos);

            ImmutableVector2f intersect = linePerp.intersection(lineSegment);

            double d3 = Math.hypot(intersect.x - robotPos.x, intersect.y - robotPos.y);

            if(d1 < d2 && d1 < d3)
            {
                return linePointA;
            }
            else if(d2 < d1 && d2 < d3)
            {
                return linePointB;
            }
            else
            {
                return intersect;
            }
        }

        /**
         * @param speed Vector's magnitude
         * @param angle Angle at which it is at
         * @return A vector in <x, y> form
         * @see ImmutableVector2f
         */
        public static ImmutableVector2f getVector(float speed, float angle)
        {
            return MathUtils.LinearAlgebra.rotate2D(VECTOR_STRAIGHT, angle);
        }

        /**
         * Given a circle and a line, find where the circle intersects the line
         *
         * @param pointA One point on the line
         * @param pointB Another point on the line
         * @param center The center of the circle
         * @param radius The radius of the circle
         * @return All points on both the line and circle, should they exist.
         */
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

        public static class Line implements Integrable
        {
            final double slope;
            final double y_intercept;
            final double x_intercept;

            final double x1;
            final double x2;
            final double y1;
            final double y2;

            final ImmutableVector2f a;
            final ImmutableVector2f b;

            public Line(ImmutableVector2f a, ImmutableVector2f b)
            {
                x1 = a.x;
                x2 = b.x;
                y1 = a.y;
                y2 = b.y;

                this.a = a;
                this.b = b;

                if(a.x - b.x != 0)
                {
                    slope = (a.y - b.y) / (a.x - b.x);

                }
                else
                {
                    slope = Double.MAX_VALUE;
                }
                y_intercept = a.y - slope * a.x;
                x_intercept = -y_intercept / slope;
            }


            public double evaluateY(double x)
            {
                return slope * x + y_intercept;
            }

            public double integrate(double a, double b)
            {
                // integral of y = mx + b is
                // mx^2/2 + bx + c
                // at start of integration bound it should be 0
                double c = -(a * a / 2 + b * a);

                Function indefiniteIntegral = (x) -> slope * x * x / 2 + y_intercept * x + c;

                return indefiniteIntegral.get(b) - indefiniteIntegral.get(a);
            }

            public double integrate()
            {
                return integrate(x1, x2);
            }

            public Line getPerp(ImmutableVector2f point)
            {
                double perpSlope;
                if(slope == Double.MAX_VALUE)
                {
                    perpSlope = 0;
                }
                else
                {
                    perpSlope = -1 / slope;
                }
                return new Line(point, new ImmutableVector2f(point.x + 1, (float) (point.y + perpSlope)));
            }

            public ImmutableVector2f intersection(Line other)
            {
                if(other.slope == slope)
                {
                    if(other.x_intercept != other.x_intercept)
                    {
                        return null;
                    }
                    else
                    {
                        // TODO: is this a good idea to return?
                        return new ImmutableVector2f((float) other.x1, (float) other.y2);
                    }
                }
                // mx + b = cx + d
                // (m-c) x = d - b
                double x = (other.y_intercept - this.y_intercept) / (this.slope - other.slope);
                double y = evaluateY(x);
                return new ImmutableVector2f((float) x, (float) y);


            }
        }
    }
}
