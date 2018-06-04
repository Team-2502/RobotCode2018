package com.team2502.robot2018.pathplanning.customprofiling;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.pathplanning.purepursuit.SplinePathSegment;
import com.team2502.robot2018.pathplanning.purepursuit.SplineWaypoint;
import com.team2502.robot2018.utils.InterpolationMap;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

import javax.swing.text.Segment;
import java.util.Iterator;

public class MotionProfilingPipeline implements Iterator<MotionProfilingPipeline.SegmentPair>
{

    private final double maxvel;
    private final double maxacc;
    private final double maxdecc;

    public static class SegmentPair
    {
        public SegmentPair(TrajectoryPoint LEFT, TrajectoryPoint RIGHT, double expectedRatio)
        {
            this.LEFT = LEFT;
            this.RIGHT = RIGHT;
            this.expectedRatio = expectedRatio;
            this.velocities = new ImmutableVector2f((float) LEFT.velocity, (float) RIGHT.velocity);
        }

        public final TrajectoryPoint LEFT;
        public final TrajectoryPoint RIGHT;
        public final double expectedRatio;
        public final ImmutableVector2f velocities;

        @Override
        public String toString()
        {
            return "left(pos=" + LEFT.position + ", vel=" + LEFT.velocity + "), \nright(pos=" + RIGHT.position +  ", vel=" + RIGHT.velocity + ")\n\n";
        }
    }

    private final InterpolationMap velocityProfile;
    private final MathUtils.ParametricFunction pathCenter;
    private final MathUtils.ParametricFunction pathLeft;
    private final MathUtils.ParametricFunction pathRight;
    private final double tF;

    private static final double DELTA_PARAM = 1E-4;
    public static final double DELTA_TIME = 0.01;
    private static final TrajectoryPoint.TrajectoryDuration TIMEDUR = TrajectoryPoint.TrajectoryDuration.Trajectory_Duration_10ms;

    private double timeelapsed = 0;
    private double accruedlength = 0;
    private double accruedLengthLeft = 0;
    private double accruedLengthRight = 0;
    private double curveparam = 0;
    public final SplineWaypoint a;
    public final SplineWaypoint b;
    public MotionProfilingPipeline(SplineWaypoint a, SplineWaypoint b)
    {
        this.a = a;
        this.b = b;
        float centerLength = (float) SplinePathSegment.getArcLength(a, b, a.getSlopeVec(), b.getSlopeVec(), 0, 1);
        pathCenter = new SplinePathSegment(a, b, a.getSlopeVec(), b.getSlopeVec(), true, true, 0, centerLength, centerLength);
        final double offsetBy = 1;
        pathLeft = pathCenter.offsetBy(-offsetBy);
        pathRight = pathCenter.offsetBy(offsetBy);

        maxvel = b.getMaxSpeed();
        maxacc = b.getMaxAccel();
        maxdecc = b.getMaxDeccel();

        double tFinalUpDown = -maxvel / maxdecc + maxvel / maxacc;
        InterpolationMap upDownProfile = new InterpolationMap(0D, 0D);
        upDownProfile.put(maxvel / maxacc, maxvel);
        upDownProfile.put(tFinalUpDown, 0D);

        velocityProfile = new InterpolationMap(0D, 0D);

        if(centerLength > upDownProfile.integrate(0, tFinalUpDown))
        {
            // Trapezoidal profile
            tF = (centerLength - ((0.5 * maxvel * maxvel) / maxdecc) + ((0.5 * maxvel * maxvel) / maxacc)) / maxvel;
            velocityProfile.put(maxvel / maxacc, maxvel);
            velocityProfile.put(tF + (maxvel / maxdecc), maxvel);
            velocityProfile.put(tF, 0D);
        }
        else
        {
            // Triangular profile
            tF = Math.sqrt(centerLength / (-maxdecc * (0.5 + 0.5 * (maxdecc / (maxacc - maxdecc)))));
            double tTransition = -maxdecc * tF / (maxacc - maxdecc);
            velocityProfile.put(tTransition, maxacc * tTransition);
            velocityProfile.put(tF, 0D);
        }
    }

    public void test()
    {
        System.out.println("Integral of vel profile: " + velocityProfile.integrate(0, tF));
        System.out.println("Vel profile at end: " + velocityProfile.get(tF));
        System.out.println("Vel profile right before end: " + velocityProfile.get(tF-0.1));
        System.out.println("Arclength of center path: " + pathCenter.getArcLength(0, 1));
        System.out.println("Arclength of left path: " + pathLeft.getArcLength(0, 1));
        System.out.println("Arclength of right path: " + pathRight.getArcLength(0, 1));
    }

    public void printManyPairs()
    {
        for(double t = 0; t <= 1; t += 1/50D)
        {
            System.out.println(pathLeft.get(t).x + ", " + pathLeft.get(t).y);
        }
    }
    @Override
    public boolean hasNext()
    {
        return curveparam <= 1;
    }

    @Override
    public SegmentPair next()
    {
        double wheelratio = pathLeft.getTanVec(curveparam).length() / pathRight.getTanVec(curveparam).length();

        ImmutableVector2f wheelVels = calculateWheelVelocities(wheelratio);

        double leftwheelpos = pathLeft.getArcLength(0, curveparam, 1/50D);
        double rightwheelpos = pathRight.getArcLength(0, curveparam, 1/50D);

        TrajectoryPoint leftTrajPoint = new TrajectoryPoint();
        TrajectoryPoint rightTrajPoint = new TrajectoryPoint();

        leftTrajPoint.timeDur = TIMEDUR;
        leftTrajPoint.velocity = wheelVels.get(0);
        leftTrajPoint.position = leftwheelpos;

        rightTrajPoint.timeDur = TIMEDUR;
        rightTrajPoint.velocity = wheelVels.get(1);
        rightTrajPoint.position = rightwheelpos;

        timeelapsed += DELTA_TIME;

        double targetpos = velocityProfile.integrate(0, timeelapsed);
        while(accruedlength <= targetpos){
            accruedlength = pathCenter.getArcLength(0, curveparam);
            curveparam += DELTA_PARAM;
        }


        return new SegmentPair(leftTrajPoint, rightTrajPoint, wheelratio);
    }

    public ImmutableVector2f calculateWheelVelocities(double velLeftToRightRatio)
    {
        double vMax = velocityProfile.get(timeelapsed);
        double velRightToLeftRatio = 1 / velLeftToRightRatio; // invert the ratio

        if(velLeftToRightRatio > 1) // if left is going faster than right
        {
            return new ImmutableVector2f((float) vMax, (float) (vMax * velRightToLeftRatio));
        }
        else // if right is going faster than left
        {
            return new ImmutableVector2f((float) (vMax * velLeftToRightRatio), (float) (vMax));
        }
    }

    public static void main(String[] args)
    {
        MotionProfilingPipeline mp = new MotionProfilingPipeline(new SplineWaypoint(new ImmutableVector2f(0, 0), new ImmutableVector2f(0, 10), 0, 0, 0),
                                                                 new SplineWaypoint(new ImmutableVector2f(7, 11), new ImmutableVector2f(0, 10), 10, 5, -8));

//        mp.test();
//        mp.printManyPairs();
        while(mp.hasNext())
        {
            SegmentPair pair = mp.next();
            System.out.print(pair.velocities.x);
            System.out.print(", ");
            System.out.print(pair.velocities.y);
            System.out.print(", ");
            System.out.println(pair.expectedRatio);
        }


    }
}
