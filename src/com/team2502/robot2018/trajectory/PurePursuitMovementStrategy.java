package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PurePursuitMovementStrategy implements TankMovementStrategy
{
    public final List<Vector> waypoints;
    private final LocationEstimator estimator;
    private Vector relativeGoalPoint;
    private final TankRobot tankRobot;
    private double pathRadius;
    private float rotVelocity;
    public final float lookAheadDistance;
    private boolean finishedPath = false;

    private Vector usedEstimatedLocation;
    private float usedHeading;

    private int lastPath = 0;

    private Vector wheelVelocities;
    private float tangentialSpeed;
    private float leftWheelTanVel;
    private float rightWheelTanVel;
    private Vector absoluteGoalPoint;
    private float dThetaToRotate;

    public PurePursuitMovementStrategy(TankRobot tankRobot, LocationEstimator estimator, List<Vector> waypoints, float lookAheadDistance)
    {
        this.waypoints = waypoints;
        this.tankRobot = tankRobot;
        this.lookAheadDistance = lookAheadDistance;
        this.estimator = estimator;
    }

    /*
    public Function<Double, Double> getEstimateHeadingFromTime()
    { return estimateHeadingFromTime; }

    public Function<Double, Vector> estimateTimeToPosition()
    { return timeToPosition; }

    public Function<Double, Vector> getEsimatePositionFromRotation()
    { return estimatePositionFromRotation; }
    */

    public Vector calculateAbsoluteGoalPoint()
    {
        if(finishedPath) { return null; }
        List<Vector> intersections = new ArrayList<>();
        int nextPathI = Integer.MAX_VALUE;
        usedEstimatedLocation = estimator.estimateLocation();

        for(int i = lastPath; i <= lastPath + 1; ++i)
        {
            if(i + 1 >= waypoints.size()) { continue; }
            Vector lineP1 = waypoints.get(i);
            Vector lineP2 = waypoints.get(i + 1);
            float toLookAhead = lookAheadDistance;
            List<Vector> vectorList = new ArrayList<>(MathUtils.Geometry.getCircleLineIntersectionPoint(lineP1, lineP2, usedEstimatedLocation, toLookAhead));
            vectorList.removeIf(vector -> !vector.between(lineP1, lineP2));
            if(i == lastPath + 1 && !vectorList.isEmpty()) { nextPathI = intersections.size(); }
            intersections.addAll(vectorList);
        }

        Vector toCompare = absoluteGoalPoint;
        if(toCompare == null) { toCompare = waypoints.get(1); }

        int closestVectorI = closest(toCompare, intersections);
        if(closestVectorI == -1)
        {
            finishedPath = true;
            return null;
        }

        Vector closest = intersections.get(closestVectorI);
        if(closestVectorI >= nextPathI) { ++lastPath; }
        return closest;
    }

    public void update()
    {
        absoluteGoalPoint = calculateAbsoluteGoalPoint();
        relativeGoalPoint = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteGoalPoint, usedEstimatedLocation, usedHeading);
        wheelVelocities = calculateWheelVelocities();
    }

    int closest(Vector origin, List<Vector> vectors)
    {
        float minMagSquared = Float.MAX_VALUE;
        int minVectorI = -1;
        for(int i = 0; i < vectors.size(); i++)
        {
            Vector vector = vectors.get(i);
            float magnitudeSquared = origin.subtractBy(vector).getMagnitudeSquared();
            if(magnitudeSquared < minMagSquared)
            {
                minMagSquared = magnitudeSquared;
                minVectorI = i;
            }
        }
        return minVectorI;
    }

    /**
     * To see if goal points are continuous
     *
     * @param
     * @return
     */
    boolean isValidGoalPoint(Vector goalPoint)
    {
        if(this.relativeGoalPoint == null)
        { return true; }
        if(this.relativeGoalPoint.get(1) < 0)
        { return false; }
        return true;
    }

    private float curvatureToGoal()
    {
        float lSquared = relativeGoalPoint.getMagnitudeSquared(); // x^2 + y^2 = l^2 (length)
        return -2 * relativeGoalPoint.get(0) / lSquared;
    }

    public Vector getCircleCenter()
    {
        Vector circleRelativeCenter = new Vector((float) -pathRadius, 0.0F);
        Vector circleRelativeCenterRotated = MathUtils.LinearAlgebra.rotate2D(circleRelativeCenter, usedHeading);
        return usedEstimatedLocation.add(circleRelativeCenterRotated);
    }

    /**
     * @return The radius of the circle that the robot is traveling across. Positive if the robot is turning left, negative if right.
     */
    public double getPathRadius()
    {
        return pathRadius;
    }

    public Vector getWheelVelocities()
    {
        return wheelVelocities;
    }


    public Vector getUsedEstimatedLocation()
    {
        return usedEstimatedLocation;
    }

    private Vector calculateWheelVelocities()
    {
        float curvature = curvatureToGoal();
        float c = 2 / (tankRobot.getLateralWheelDistance() * curvature);
        float velLeftToRightRatio = -(c + 1) / (1 - c);
        float velRightToLeftRatio = 1 / velLeftToRightRatio;
        float score = Float.MIN_VALUE;
        Vector bestVector = null;
        //TODO: fix clumsy way of optimizing :(

        float v_lMax = tankRobot.getV_lMax();
        float v_rMax = tankRobot.getV_rMax();
        float v_lMin = tankRobot.getV_lMin();
        float v_rMin = tankRobot.getV_rMin();

        float v_r = v_lMax * velLeftToRightRatio;
        if(MathUtils.Algebra.between(v_rMin, v_r, v_rMax))
        {
            score = Math.abs(v_lMax + v_r);
            bestVector = new Vector(v_lMax, v_r);
        }

        v_r = v_lMin * velLeftToRightRatio;
        if(MathUtils.Algebra.between(v_rMin, v_r, v_rMax))
        {
            float tempScore = Math.abs(v_lMin + v_r);
            if(tempScore > score)
            {
                score = tempScore;
                bestVector = new Vector(v_lMin, v_r);
            }
        }

        float v_l = v_rMax * velRightToLeftRatio;
        if(MathUtils.Algebra.between(v_lMin, v_l, v_lMax))
        {
            float tempScore = Math.abs(v_lMax + v_l);
            if(tempScore > score)
            {
                score = tempScore;
                bestVector = new Vector(v_l, v_rMax);
            }
        }

        v_l = v_rMin * velRightToLeftRatio;
        if(MathUtils.Algebra.between(v_lMin, v_l, v_lMax))
        {
            float tempScore = Math.abs(v_lMin + v_l);
            if(tempScore > score)
            {
                bestVector = new Vector(v_l, v_rMin);
            }
        }

        if(bestVector == null) { return null; }

        rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
        // Note this can be negative

        pathRadius = 1 / curvature;

        leftWheelTanVel = (float) Math.abs((pathRadius - tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
        rightWheelTanVel = (float) Math.abs((pathRadius + tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
        tangentialSpeed = (float) Math.abs(pathRadius * rotVelocity);
        usedHeading = tankRobot.getHeading();

        dThetaToRotate = (float) (MathUtils.Arithmetic.sign(rotVelocity) * Math.atan(relativeGoalPoint.get(1) / (Math.abs(pathRadius) - relativeGoalPoint.get(0))));

        Function<Double, Vector> estimatePositionFromDTheta = dTheta -> {
            float dxRelative = (float) (-pathRadius * (1 - Math.cos(-dTheta)));
            float dyRelative = (float) (-pathRadius * Math.sin(-dTheta));
            Vector dRelativeVector = new Vector(dxRelative, dyRelative);
            Vector rotated = MathUtils.LinearAlgebra.rotate2D(dRelativeVector, usedHeading);
            return rotated.add(usedEstimatedLocation);
        };



        /*
        estimateHeadingFromTime = time-> {
            float heading = usedHeading + rotVelocity*time;
            return heading;
        };

        esimatePositionFromRotation = angle -> {
            float dTheta = angle - usedHeading;
            return estimatePositionFromDTheta.apply(dTheta);
        };

        estimatedTime = thetaToRotate / rotVelocity;

        timeToPosition = time -> {
            float dTheta = time * rotVelocity;
            return estimatePositionFromDTheta.apply(dTheta);
        };
        */

        return bestVector;
    }

    public float getTangentialSpeed()
    {
        return tangentialSpeed;
    }

    public float getLeftWheelTanVel()
    {
        return leftWheelTanVel;
    }

    public float getRightWheelTanVel()
    {
        return rightWheelTanVel;
    }

    public Vector getRelativeGoalPoint()
    {
        return relativeGoalPoint;
    }

    public float getRotVelocity()
    {
        return rotVelocity;
    }

    public boolean isFinishedPath()
    {
        return finishedPath;
    }

    public float getUsedHeading()
    {
        return usedHeading;
    }

    public Vector getAbsoluteGoalPoint()
    {
        return absoluteGoalPoint;
    }

    public float getdThetaToRotate()
    {
        return dThetaToRotate;
    }
}
