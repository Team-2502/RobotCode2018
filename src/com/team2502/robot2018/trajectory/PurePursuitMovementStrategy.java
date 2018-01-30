package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.trajectory.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.utils.MathUtils;
import logger.Log;
import org.joml.ImmutableVector2f;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PurePursuitMovementStrategy implements ITankMovementStrategy
{
    /**
     * This represents the threshold curvature beyond which it's basically a straight line.
     */
    private static final float THRESHOLD_CURVATURE = 0.001F;
    public final List<ImmutableVector2f> waypoints;
    public final float lookAheadDistance;
    private final ITranslationalLocationEstimator transEstimator;
    private final ITankRobotBounds tankRobot;
    private final IRotationalLocationEstimator rotEstimator;
    private final float lookAheadDistanceSquared;
    private final float distanceStopSq;
    private ImmutableVector2f relativeGoalPoint;
    private float pathRadius;
    private float rotVelocity;
    private boolean finishedPath = false;
    private ImmutableVector2f usedEstimatedLocation = new ImmutableVector2f();
    private float usedHeading = 0.0F;

    private int lastSegmentSearched = 0;

    private ImmutableVector2f wheelVelocities;
    private float tangentialSpeed;
    private float leftWheelTanVel;
    private float rightWheelTanVel;
    private ImmutableVector2f absoluteGoalPoint;
    private float dThetaToRotate;
    private boolean isClose = false;
    private boolean isSuccessfullyFinished;

    /**
     * Strategize your movement!
     *
     * @param tankRobot         An instance of ITanRobotBounds, an interface that has getters for robot max speed and accel.
     * @param transEstimator    An estimator for the absolute position of the robot
     * @param rotEstimator      An estimator for the heading of the robot
     * @param waypoints         A list of waypoints for the robot to drive through
     * @param lookAheadDistance The lookahead distance for the pure pursuit algorithm
     * @param distanceStop
     */
    public PurePursuitMovementStrategy(ITankRobotBounds tankRobot, ITranslationalLocationEstimator transEstimator, IRotationalLocationEstimator rotEstimator, List<ImmutableVector2f> waypoints, float lookAheadDistance, float distanceStop)
    {
        this.waypoints = waypoints;
        this.tankRobot = tankRobot;
        this.lookAheadDistance = lookAheadDistance;
        this.transEstimator = transEstimator;
        this.rotEstimator = rotEstimator;
        lookAheadDistanceSquared = lookAheadDistance * lookAheadDistance;
        distanceStopSq = distanceStop * distanceStop;
    }

    /**
     * @return The absolute location of the selected goal point.
     * The goal point is a point on the path 1 lookahead distance away from us.
     * We want to drive at it.
     */
    public ImmutableVector2f calculateAbsoluteGoalPoint()
    {
        // The path is finished — there are no more goal points to compute
        if(finishedPath) { return null; }

        List<ImmutableVector2f> intersections = new ArrayList<>();

        // We have intersections.get(nextPathI) as the first goal intersection on the next segment.
        // This is Integer.MAX_VALUE as there might not be any.
        int nextPathI = Integer.MAX_VALUE;

        // Loop looks for intersections on last segment searched and one after that
        for(int i = lastSegmentSearched; i <= lastSegmentSearched + 1; ++i)
        {
            // We are on the last segment. There is no _next_ segment to search
            if(i + 1 >= waypoints.size()) { continue; }

            ImmutableVector2f lineP1 = waypoints.get(i);
            ImmutableVector2f lineP2 = waypoints.get(i + 1);

            // Since goal points are within radius LOOK_AHEAD_DISTANCE from us, the robot would normally stop
            // when the distance from the last waypoint < LOOK_AHEAD_DISTANCE. However, we prevent this
            // by setting the last waypoint as a goal point when this happens.
            // TODO: test this to see if the robot is jerky near the end
            if(i + 2 == waypoints.size())
            {
                float distanceWaypointSq = lineP2.sub(usedEstimatedLocation).lengthSquared();
                if(distanceWaypointSq <= lookAheadDistanceSquared)
                {
//                    System.out.println("is close to: "+lineP2);
                    isClose = true;
                    // We want to stop if the distance is within the desired amount
                    if(distanceWaypointSq < distanceStopSq)
                    {
                        isSuccessfullyFinished = true;
//                        System.out.println("is finished w/: "+lineP2);
                        finishedPath = true;
                        return null;
                    }
                    return lineP2;
                }
            }

            // Get intersections of r=lookAheadDistance circle and segments between waypoints
            List<ImmutableVector2f> vectorList = new ArrayList<>(Arrays.asList(MathUtils.Geometry.getCircleLineIntersectionPoint(lineP1, lineP2, usedEstimatedLocation, lookAheadDistance)));

            // above statement assumes lineP1 lineP2 defines a (non-segment) line. This is to define it as a segment
            // (we are removing points that are not between lineP1 and lineP2)
            vectorList.removeIf(vector -> !MathUtils.between(lineP1, vector, lineP2)); // remove if vectors not between next 2 waypoints

            if(i == lastSegmentSearched + 1 && !vectorList.isEmpty()) { nextPathI = intersections.size(); }

            // We are adding all circle-line intersections to be checked. The best (and valid ones) will be selected.
            intersections.addAll(vectorList);
        }

        // The last goal point we are comparing the intersections to. We will want to chose the one closest to last intersection
        ImmutableVector2f toCompare = absoluteGoalPoint;

        // if there is no last goal point, then just chose the first intersection
        if(toCompare == null)
        {
            toCompare = waypoints.get(1);
            this.absoluteGoalPoint = waypoints.get(1);
        }


        // Finds i where ||\vec{toCompare} - \vec{intersections_i}|| (the distance between the 2 vectors) is minimized
        int closestVectorI = bestGoalPoint(toCompare, intersections);

        // There is no closest vector ==> finish path
        if(closestVectorI == -1)
        {
            Log.info("closest vector not found!");
            System.out.printf("loc: %.2f, %.2f", usedEstimatedLocation.get(0), usedEstimatedLocation.get(1));
            finishedPath = true;
            return null;
        }

        ImmutableVector2f closest = intersections.get(closestVectorI);

        // If the closest vector is on the next segment, set that segment as the current segment
        if(closestVectorI >= nextPathI) { ++lastSegmentSearched; }

        // closest is our new goal point
        return closest;
    }

    /**
     * @return true If the robot is close (within lookahead distance) of the last waypoint.
     */
    public boolean isClose()
    {
        return isClose;
    }

    /**
     * Recalculates position, heading, and goalpoint.
     */
    public void update()
    {

        usedEstimatedLocation = transEstimator.estimateLocation();
        usedHeading = rotEstimator.estimateHeading();

        absoluteGoalPoint = calculateAbsoluteGoalPoint();

        // Sometimes the above method will cause isFinished to return true if no more goal points are found.
        if(isFinishedPath())
        {
            Log.info("\nFinished path!!!!\n");
            return;
        }

        relativeGoalPoint = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteGoalPoint, usedEstimatedLocation, usedHeading);
        wheelVelocities = calculateWheelVelocities();
    }

    /**
     * Chooses which goal point should be chosen given a list of possible goal points (generated by circle-line intersection)
     *
     * @param origin             the absolute location of the robot
     * @param possibleGoalPoints the absolute location of goal points
     */
    int bestGoalPoint(ImmutableVector2f origin, List<ImmutableVector2f> possibleGoalPoints)
    {
        float minMagSquared = Float.MAX_VALUE;
        int minVectorI = -1;
        for(int i = 0; i < possibleGoalPoints.size(); i++)
        {
            ImmutableVector2f vector = possibleGoalPoints.get(i);

            float magnitudeSquared = origin.sub(vector).lengthSquared(); // find dist squared

            if(magnitudeSquared < minMagSquared)
            {
                minMagSquared = magnitudeSquared;
                minVectorI = i;
            }
        }
        return minVectorI;
    }

    /**
     * @return The curvature (1/radius) to the goal point
     */
    private float calcCurvatureToGoal()
    {
        float lSquared = relativeGoalPoint.lengthSquared(); // x^2 + y^2 = l^2 (length)

        // curvature = 2x / l^2 (from Pure Pursuit paper)
        // added - so it is positive when counterclockwise
        return -2 * relativeGoalPoint.get(0) / lSquared;
    }

    /**
     *
     * @return The lateral distance (with respect to the robot) between the robot and the goal point.
     */
    public float getCrossTrackError(){
        return relativeGoalPoint.get(0);
    }

    /**
     * @return The center of the circle that the robot is travelling on
     */
    public ImmutableVector2f getCircleCenter()
    {
        ImmutableVector2f circleRelativeCenter = new ImmutableVector2f(-pathRadius, 0.0F);
        ImmutableVector2f circleRelativeCenterRotated = MathUtils.LinearAlgebra.rotate2D(circleRelativeCenter, usedHeading);
        return usedEstimatedLocation.add(circleRelativeCenterRotated);
    }

    /**
     * @return The vector to drive along. first component = left speed, second component = right speed
     * @throws NullPointerException only if it gets confused and doesn't know what vector to drive along
     */
    private ImmutableVector2f calculateWheelVelocities() throws NullPointerException
    {
        float curvature = calcCurvatureToGoal();
        ImmutableVector2f bestVector = null;


        // TODO: get max acceleration from actual wheel velocities
        float v_lMax = Math.min(getWheelVelocities().get(0)+tankRobot.getA_lMax(), tankRobot.getV_lMax());
        float v_rMax = Math.min(getWheelVelocities().get(1)+tankRobot.getA_rMax(), tankRobot.getV_rMax());
        float v_lMin = Math.max(getWheelVelocities().get(0)+tankRobot.getA_lMin(), tankRobot.getV_lMax());
        float v_rMin = Math.max(getWheelVelocities().get(1)+tankRobot.getA_rMin(), tankRobot.getV_rMin());


        if(Math.abs(curvature) < THRESHOLD_CURVATURE) // if we are a straight line ish (lines are not curvy -> low curvature)
        {

            bestVector = new ImmutableVector2f(v_lMax, v_rMax);
            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
            pathRadius = Float.MAX_VALUE;
            leftWheelTanVel = bestVector.get(0);
            rightWheelTanVel = bestVector.get(1);
            tangentialSpeed = leftWheelTanVel;
            dThetaToRotate = 0;
        }
        else // if we need to go in a circle
        {
            float c = 2 / (tankRobot.getLateralWheelDistance() * curvature);
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
                throw new NullPointerException(MessageFormat.format("`bestVector` was equal to null.\n\t" +
                                                                    "{\n\t\t\"curvature\" = \"{0}\",\n\t\t" +
                                                                    "[ \"v_lMax\", \"v_lMin\", \"v_rMax\", \"v_rMin\" ] = [ \"{1}\", \"{2}\", \"{3}\", \"{4}\" ],\n\t\t" +
                                                                    "\"c\" = \"{5}\",\n\t\t" +
                                                                    "\"velLeftToRightRatio\" = \"{6}\",\n\t\t" +
                                                                    "\"velRightToLeftRatio\" = \"{7}\",\n\t\t" +
                                                                    "\"v_r\" = \"{8}\",\n\t\t" +
                                                                    "\"v_l\" = \"{9}\"\n\t}", curvature, v_lMax, v_lMin, v_rMax, v_rMin, c, velLeftToRightRatio, velRightToLeftRatio, v_r, v_l));
            }

            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
            pathRadius = 1 / curvature;
            leftWheelTanVel = Math.abs((pathRadius - tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
            rightWheelTanVel = Math.abs((pathRadius + tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
            tangentialSpeed = Math.abs(pathRadius * rotVelocity);
            dThetaToRotate = (float) (Math.signum(rotVelocity) * Math.atan(relativeGoalPoint.get(1) / (Math.abs(pathRadius) - relativeGoalPoint.get(0))));
        }

        return bestVector;
    }

    /**
     * @return The radius of the circle that the robot is traveling across. Positive if the robot is turning left, negative if right.
     */
    public float getPathRadius()
    { return pathRadius; }

    /**
     * @return The velocities (left,right) of the wheels. If you are setting input as voltage, "velocities" will actually be voltages. Magnitude doesn't matter the most; ratio does.
     */
    public ImmutableVector2f getWheelVelocities()
    { return wheelVelocities; }

    public ImmutableVector2f getUsedEstimatedLocation()
    { return usedEstimatedLocation; }

    /**
     * @return The tangential speed of the robot
     */
    public float getTangentialSpeed()
    { return tangentialSpeed; }

    /**
     * @return The tangential speed of the left wheels
     */
    public float getLeftWheelTanVel()
    { return leftWheelTanVel; }

    /**
     * @return The tangential speed of the right wheels
     */
    public float getRightWheelTanVel()
    { return rightWheelTanVel; }

    /**
     * @return The goal point with respect to the robot
     */
    public ImmutableVector2f getRelativeGoalPoint()
    { return relativeGoalPoint; }

    /**
     * @return The rotational velocity of the robot. WARNING: will not work if you are inputting voltage instead of actual velocities
     * @deprecated
     */
    public float getRotVelocity()
    { return rotVelocity; }

    public boolean isSuccessfullyFinished()
    { return isSuccessfullyFinished; }

    /**
     * @return If the robot is finished traveling the path
     */
    public boolean isFinishedPath()
    { return finishedPath || waypoints.size() < 1; }

    /**
     * @return Get the heading (angle) of the robot
     */
    public float getUsedHeading()
    { return usedHeading; }

    /**
     * @return The absolute location of the goal point
     */
    public ImmutableVector2f getAbsoluteGoalPoint()
    { return absoluteGoalPoint; }

    /**
     * @return The distance (theta) robot must rotate to face directly at goal point
     */
    public float getdThetaToRotate()
    { return dThetaToRotate; }
}
