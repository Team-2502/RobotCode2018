package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.MathUtils.ParametricFunction;
import org.joml.ImmutableVector2f;

public class SplinePathSegment extends PathSegment implements ParametricFunction
{

    private final Point firstSlope;
    private final Point lastSlope;

    /**
     * t^3 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f a;

    /**
     * t^2 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f b;

    /**
     * t^1 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f c;

    /**
     * t^0 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f d;

    public SplinePathSegment(Point first, Point last, Point firstSlope, Point lastSlope, boolean start, boolean end, float distanceStart, float distanceEnd, float length)
    {
        super(first, last, start, end, distanceStart, distanceEnd, length);
        this.firstSlope = firstSlope;
        this.lastSlope = lastSlope;

        // 2p_0 - 2p_1 + p'_0 + p'_1
        a = first.getLocation().mul(2).sub(last.getLocation().mul(2)).add(firstSlope.getLocation()).add(lastSlope.getLocation());

        // -3p_0 + 3p_1 - 2p'_0 - p_1
        b = first.getLocation().mul(-3).add(last.getLocation().mul(3)).sub(firstSlope.getLocation().mul(2)).sub(lastSlope.getLocation());

        // p'_0
        c = firstSlope.getLocation();

        // p_0
        d = first.getLocation();

    }

    @Override
    public ImmutableVector2f getClosestPoint(ImmutableVector2f robotPos)
    {
        return MathUtils.Geometry.getClosestPointParametricFunc(this, robotPos);
    }

    @Override
    public ImmutableVector2f getPoint(float relativeDistance)
    {
        return fromArcLength(relativeDistance);
    }

    @Override
    public float getDistanceLeft2(ImmutableVector2f point)
    {
        return MathUtils.pow2f(getDistanceLeft(point));
    }

    @Override
    public float getDistanceLeft(ImmutableVector2f point)
    {
        return (float) (getArcLength(0, 1) - getArcLength(0, getT(point, 0 , 1)));
    }

    @Override
    public float getLength()
    {
        return (float) getArcLength(0, 1);
    }

    @Override
    public String toString()
    {
        return "SplinePathSegment{" +
               "first=" + super.getFirst() +
               ", last=" + super.getLast() +
               ", firstSlope=" + firstSlope +
               ", lastSlope=" + lastSlope +
               '}';
    }

    /**
     *
     * @param t:[0,1]
     * @return
     */
    @Override
    public double getX(double t)
    {
        return MathUtils.pow3f((float) t) * a.x + MathUtils.pow2f((float) t) * b.x + t * c.x + d.x;
    }

    /**
     *
     * @param t:[0,1]
     * @return
     */
    @Override
    public double getY(double t)
    {
        return MathUtils.pow3f((float) t) * a.y + MathUtils.pow2f((float) t) * b.y + t * c.y + d.y;
    }

    public static double getArcLength(Point first, Point last, Point firstSlope, Point lastSlope, double lowerBound, double upperBound)
    {
        ImmutableVector2f a = first.getLocation().mul(2).sub(last.getLocation().mul(2)).add(firstSlope.getLocation()).add(lastSlope.getLocation());
        ImmutableVector2f b = first.getLocation().mul(-3).add(last.getLocation().mul(3)).sub(firstSlope.getLocation().mul(2)).sub(lastSlope.getLocation());
        ImmutableVector2f c = firstSlope.getLocation();
        ImmutableVector2f d = first.getLocation();

        ParametricFunction spline = new ParametricFunction() {
            @Override
            public double getX(double t)
            {
                return MathUtils.pow3f((float) t) * a.x + MathUtils.pow2f((float) t) * b.x + t * c.x + d.x;
            }

            @Override
            public double getY(double t)
            {
                return MathUtils.pow3f((float) t) * a.y + MathUtils.pow2f((float) t) * b.y + t * c.y + d.y;
            }
        };
        return spline.getArcLength(lowerBound, upperBound);
    }

    public Point getLastSlope()
    {
        return lastSlope;
    }

    public Point getFirstSlope()
    {
        return firstSlope;
    }
}
