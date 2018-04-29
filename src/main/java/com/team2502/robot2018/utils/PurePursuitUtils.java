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
        float lookaheadForSpeed = lookahead.getLookaheadForSpeed(robotTangentialVelocity);

        return lookaheadForSpeed + closestPointDistance;
    }

    public static float generateSpeedUsed(float closestPointDistAbsAlongPath, float lastWaypointSpeed, float cycleTime, Path path)
    {
        Waypoint waypointEnd = (Waypoint) path.getCurrent().getLast();
        float finalSpeed = waypointEnd.getMaxSpeed();

        float speed = Float.MAX_VALUE;

        // Looks within 15 feet ahead
        for(PathSegment pathSegment : path.nextSegmentsInclusive(15))
        {
            Waypoint last = (Waypoint) pathSegment.getLast();
            float distanceTo = pathSegment.getAbsoluteDistanceEnd() - closestPointDistAbsAlongPath;
            float maxSpeed = getMaxSpeed(lastWaypointSpeed, last.getMaxSpeed(), distanceTo, waypointEnd.getMaxDeccel());

            if(maxSpeed < speed)
            {
                speed = maxSpeed;
            }
        }

        if(speed < lastWaypointSpeed)
        {
            return speed;
        }
        else if((finalSpeed > 0 && finalSpeed > lastWaypointSpeed) || (finalSpeed < 0 && finalSpeed < lastWaypointSpeed))
        {
            return  MathUtils.minF(finalSpeed, lastWaypointSpeed + cycleTime * waypointEnd.getMaxAccel());
        }
        return Float.NaN;
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
//        float curvature = calcCurvatureToGoal();
        ImmutableVector2f bestVector = null;

        float v_lMax = speedUsed;
        float v_rMax = speedUsed;
        float v_lMin = -speedUsed;
        float v_rMin = -speedUsed;

        if(Math.abs(curvature) < THRESHOLD_CURVATURE) // if we are a straight line ish (lines are not curvy -> low curvature)
        {
            bestVector = new ImmutableVector2f(v_lMax, v_rMax);
//            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
//            motionRadius = Float.MAX_VALUE;
//            leftWheelTanVel = bestVector.get(0);
//            rightWheelTanVel = bestVector.get(1);
//            tangentialSpeed = leftWheelTanVel;
//            dThetaToRotate = 0;
        }
        else // if we need to go in a circle
        {
            float c = 2 / (lateralWheelDistance * curvature);
            float velLeftToRightRatio = -(c + 1) / (1 - c); // an equation pulled out of some paper probably
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
     * @return The curvature (1/radius) to the goal point
     */
    public static float calculateCurvature(ImmutableVector2f relativeGoalPoint)
    {
        float lSquared = relativeGoalPoint.lengthSquared(); // x^2 + y^2 = l^2 (length)

        // curvature = 2x / l^2 (from Pure Pursuit paper)
        // added - so it is positive when counterclockwise
        return -2 * relativeGoalPoint.get(0) / lSquared;
    }
}
