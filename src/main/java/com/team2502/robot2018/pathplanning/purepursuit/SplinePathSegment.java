package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.guitools.splineviz.SplinePoint;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.MathUtils.ParametricFunction;
import org.joml.ImmutableVector2f;

public class SplinePathSegment extends PathSegment implements ParametricFunction
{

    private final Point firstSlope;
    private final Point lastSlope;

    /**
     * t^5 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f a;

    /**
     * t^4 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f b;

    /**
     * t^3 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f c;

    /**
     * t^2 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f d;

    /**
     * t^1 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f e;

    /**
     * t^0 coefficient for the cubic spline equation
     */
    private final ImmutableVector2f f;


    public SplinePathSegment(Point first, Point last, Point firstSlope, Point lastSlope, boolean start, boolean end, float distanceStart, float distanceEnd, float length)
    {
        super(first, last, start, end, distanceStart, distanceEnd, length);
        this.firstSlope = firstSlope;
        this.lastSlope = lastSlope;

        // -3 (2p_0 - 2p_1 + p'_0 + p'_1)
        a = (first.getLocation().mul(2)
                  .sub(last.getLocation().mul(2))
                  .add(firstSlope.getLocation())
                  .add(lastSlope.getLocation())
            ).mul(-3);


        //  15 p_0 - 15 p_1 + 8p'_0 + 7p'_1
        b = first.getLocation().mul(15)
                 .sub(last.getLocation().mul(15))
                 .add(firstSlope.getLocation().mul(8))
                 .add(lastSlope.getLocation().mul(7));

        // -2 ( 5p_0 - 5p_1 + 3p'_0 + 2p'_1 )
        c = (first.getLocation().mul(5)
                  .sub(last.getLocation().mul(5))
                  .add(firstSlope.getLocation().mul(3))
                  .add(lastSlope.getLocation().mul(2))
            ).mul(-2);

        // 0
        d = new ImmutableVector2f(0, 0);

        // p'_0
        e = firstSlope.getLocation();

        // p_0
        f = first.getLocation();

    }

    public SplinePathSegment(SplinePoint first, SplinePoint last, boolean start, boolean end, float distanceStart, float distanceEnd, float length)
    {
        super(first, last, start, end, distanceStart, distanceEnd, length);
        this.firstSlope = new Point(first.getTangentVec());
        this.lastSlope = new Point(last.getTangentVec());

        // -3 (2p_0 - 2p_1 + p'_0 + p'_1)
        a = (first.getLocation().mul(2)
                  .sub(last.getLocation().mul(2))
                  .add(firstSlope.getLocation())
                  .add(lastSlope.getLocation())
        ).mul(-3);


        //  15 p_0 - 15 p_1 + 8p'_0 + 7p'_1
        b = first.getLocation().mul(15)
                 .sub(last.getLocation().mul(15))
                 .add(firstSlope.getLocation().mul(8))
                 .add(lastSlope.getLocation().mul(7));

        // -2 ( 5p_0 - 5p_1 + 3p'_0 + 2p'_1 )
        c = (first.getLocation().mul(5)
                  .sub(last.getLocation().mul(5))
                  .add(firstSlope.getLocation().mul(3))
                  .add(lastSlope.getLocation().mul(2))
        ).mul(-2);

        // 0
        d = new ImmutableVector2f(0, 0);

        // p'_0
        e = firstSlope.getLocation();

        // p_0
        f = first.getLocation();

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
        return (float) (getArcLength(0, 1) - getArcLength(0, getT(point, 0, 1)));
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
     * @param t:[0,1]
     * @return
     */
    @Override
    public double getX(double t)
    {
        return MathUtils.pow5(t) * a.x + MathUtils.pow4(t) * b.x + MathUtils.pow3(t) * c.x + MathUtils.pow2(t) * d.x + t * e.x + f.x;
    }

    /**
     * @param t:[0,1]
     * @return
     */
    @Override
    public double getY(double t)
    {
        return MathUtils.pow5(t) * a.y + MathUtils.pow4(t) * b.y + MathUtils.pow3(t) * c.y + MathUtils.pow2(t) * d.y + t * e.y + f.y;
    }

    public static double getArcLength(Point first, Point last, Point firstSlope, Point lastSlope, double lowerBound, double upperBound)
    {
        // -3 (2p_0 - 2p_1 + p'_0 + p'_1)
        ImmutableVector2f a = (first.getLocation().mul(2)
                  .sub(last.getLocation().mul(2))
                  .add(firstSlope.getLocation())
                  .add(lastSlope.getLocation())
        ).mul(-3);


        //  15 p_0 - 15 p_1 + 8p'_0 + 7p'_1
        ImmutableVector2f b = first.getLocation().mul(15)
                 .sub(last.getLocation().mul(15))
                 .add(firstSlope.getLocation().mul(8))
                 .add(lastSlope.getLocation().mul(7));

        // -2 ( 5p_0 - 5p_1 + 3p'_0 + 2p'_1 )
        ImmutableVector2f c = (first.getLocation().mul(5)
                  .sub(last.getLocation().mul(5))
                  .add(firstSlope.getLocation().mul(3))
                  .add(lastSlope.getLocation().mul(2))
        ).mul(-2);

        // 0
        ImmutableVector2f d = new ImmutableVector2f(0, 0);

        // p'_0
        ImmutableVector2f e = firstSlope.getLocation();

        // p_0
        ImmutableVector2f f = first.getLocation();
        
        ParametricFunction spline = new ParametricFunction()
        {
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
