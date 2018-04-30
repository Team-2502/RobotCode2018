package com.team2502.robot2018.utils;

import com.team2502.robot2018.pathplanning.purepursuit.LookaheadBounds;
import com.team2502.robot2018.pathplanning.purepursuit.Path;
import com.team2502.robot2018.pathplanning.purepursuit.PathSegment;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import org.joml.ImmutableVector2f;

public class PurePursuitUtils
{

    /**
     * This represents the threshold curvature beyond which it's basically a straight line.
     */
    private static final float THRESHOLD_CURVATURE = 0.001F;

    /**
     * @param lookahead
     * @param robotTangentialVelocity
     * @param closestPointDistance    The distance from the robot of the closest point on the path
     * @return
     */
    public static float generateLookahead(LookaheadBounds lookahead, float robotTangentialVelocity, float closestPointDistance)
    {
        if(closestPointDistance < 0)
        {
            throw new IllegalArgumentException("Closest point cannot have negative distance!");
        }
        float lookaheadForSpeed = lookahead.getLookaheadForSpeed(robotTangentialVelocity);

        return lookaheadForSpeed + closestPointDistance;
    }

    /**
     *
     * @param positionOnPath often the distance along the path the closest point is
     * @param speedAtLastWaypoint
     * @param cycleTime
     * @param path
     * @return
     */
    public static float generateSpeedUsed(float positionOnPath, float speedAtLastWaypoint, float cycleTime, Path path)
    {
        float start = path.getCurrent().getAbsoluteDistanceStart();
        if(start > positionOnPath)
        {
            System.out.println("start = " + start);
            System.out.println("positionOnPath = " + positionOnPath);
            throw new IllegalArgumentException("Looking behind on path");
        }
        Waypoint waypointEnd = (Waypoint) path.getCurrent().getLast();
        float finalSpeed = waypointEnd.getMaxSpeed();

        float speed = Float.MAX_VALUE;

        // Looks within 15 feet ahead
        for(PathSegment pathSegment : path.nextSegmentsInclusive(15))
        {
            Waypoint last = (Waypoint) pathSegment.getLast();
            float distanceTo = pathSegment.getAbsoluteDistanceEnd() - positionOnPath;
            if(distanceTo < 0)
            {
                // TODO: fix this... kinda jank
                if(distanceTo < -0.5)
                {
                    System.out.println("distanceTo = " + distanceTo);
                    throw new IllegalArgumentException("Path should have progressed (looking ahead on path)");
                }
                else
                {
                    distanceTo = 0;
                }
            }
            float maxSpeed = getMaxSpeed(speedAtLastWaypoint, last.getMaxSpeed(), distanceTo, waypointEnd.getMaxDeccel());

            if(maxSpeed < speed)
            {
                speed = maxSpeed;
            }
        }

        if(speed < speedAtLastWaypoint)
        {
            return speed;
        }
        else if((finalSpeed > 0 && finalSpeed > speedAtLastWaypoint) || (finalSpeed < 0 && finalSpeed < speedAtLastWaypoint))
        {
            return  MathUtils.minF(finalSpeed, speedAtLastWaypoint + cycleTime * waypointEnd.getMaxAccel());
        }
        return speedAtLastWaypoint;
    }

    private static float getMaxSpeed(float initSpeed, float finalSpeed,float distanceLeft, float currentMaxDeccel)
    {
        float speed;
        float maxVel = (float) Math.sqrt(finalSpeed * finalSpeed - 2 * currentMaxDeccel * distanceLeft);
        speed = Math.min(initSpeed, maxVel);
        return speed;
    }

    public static ImmutableVector2f calculateWheelVelocities(float curvature, float lateralWheelDistance, float speedUsed)
    {

        ImmutableVector2f bestVector = null;

        float v_lMax = speedUsed;
        float v_rMax = speedUsed;
        float v_lMin = -speedUsed;
        float v_rMin = -speedUsed;

        if(Math.abs(curvature) < THRESHOLD_CURVATURE) // if we are a straight line ish (lines are not curvy -> low curvature)
        {
            return new ImmutableVector2f(v_lMax, v_rMax);
//            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
//            motionRadius = Float.MAX_VALUE;
//            leftWheelTanVel = bestVector.get(0);
//            rightWheelTanVel = bestVector.get(1);
//            tangentialSpeed = leftWheelTanVel;
//            dThetaToRotate = 0;
        }
        else // if we need to go in a circle
        {

            // Formula for differential drive radius of cricle
            // r = L/2 * (vl + vr)/(vr - vl)
            // 2(vr - vl) * r = L(vl + vr)
            // L*vl + L*vr - 2r*vr + 2r*vl = 0
            // vl(L+2r) + vr(L-2r) = 0
            // vl(L+2r) = -vr(L-2r)
            // vl/vr = -(L+2r)/(L-2r)
            float r = 1/curvature;

            float velLeftToRightRatio = -(lateralWheelDistance + 2*r)/(lateralWheelDistance - 2*r);
            float velRightToLeftRatio = 1 / velLeftToRightRatio; // invert the ratio

            // This first big repetitive section is just finding the largest possible velocities while maintaining a ratio.
            float score = Float.MIN_VALUE;

            float v_r = v_lMax * velLeftToRightRatio;

            if(MathUtils.Algebra.between(v_rMin, v_r, v_rMax))
            {
                score = Math.abs(v_lMax + v_r);
                bestVector = new ImmutableVector2f(v_lMax, v_r);
            }

            v_r = v_lMin * velLeftToRightRatio;
            if(MathUtils.Algebra.between(v_rMin, v_r, v_rMax))
            {
                float tempScore = Math.abs(v_lMin + v_r);
                if(tempScore > score)
                {
                    score = tempScore;
                    bestVector = new ImmutableVector2f(v_lMin, v_r);
                }
            }

            float v_l = v_rMax * velRightToLeftRatio;
            if(MathUtils.Algebra.between(v_lMin, v_l, v_lMax))
            {
                float tempScore = Math.abs(v_lMax + v_l);
                if(tempScore > score)
                {
                    score = tempScore;
                    bestVector = new ImmutableVector2f(v_l, v_rMax);
                }
            }

            v_l = v_rMin * velRightToLeftRatio;
            if(MathUtils.Algebra.between(v_lMin, v_l, v_lMax))
            {
                float tempScore = Math.abs(v_lMin + v_l);
                if(tempScore > score)
                {
                    bestVector = new ImmutableVector2f(v_l, v_rMin);
                }
            }

            if(bestVector == null)
            {
                throw new NullPointerException("bestVector is null!");
            }

//            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
//            motionRadius = 1 / curvature;
//            leftWheelTanVel = Math.abs((motionRadius - tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
//            rightWheelTanVel = Math.abs((motionRadius + tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
//            tangentialSpeed = (leftWheelTanVel + rightWheelTanVel) / 2;
//            dThetaToRotate = (float) (Math.signum(rotVelocity) * Math.atan(relativeGoalPoint.get(1) / (Math.abs(motionRadius) - relativeGoalPoint.get(0))));
        }

        return bestVector;
    }

    /***
     * @return The curvature (1/radius) to the goal point ... positive when CCW
     */
    public static float calculateCurvature(ImmutableVector2f relativeGoalPoint)
    {
        float lSquared = relativeGoalPoint.lengthSquared(); // x^2 + y^2 = l^2 (length)

        // curvature = 2x / l^2 (from Pure Pursuit paper)
        // added - so it is positive when counterclockwise
        float toReturn = -2 * relativeGoalPoint.get(0) / lSquared;
        return toReturn;
    }
}
