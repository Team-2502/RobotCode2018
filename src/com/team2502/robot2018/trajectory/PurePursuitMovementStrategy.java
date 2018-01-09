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
    private float pathRadius;
    private float rotVelocity;
    public final float lookAheadDistance;
    private boolean finishedPath = false;
    private static final float THRESHOLD_CURVTURE = 0.001F;

    private Vector usedEstimatedLocation = new Vector(0, 0);
    private float usedHeading = 0.0F;

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
            System.out.println("Vector list: " + vectorList.toString());
            vectorList.removeIf(vector -> !vector.between(lineP1, lineP2)); // remove if vectors not between next 2 waypoints
            System.out.println("Vector list: " + vectorList.toString());
            if(i == lastPath + 1 && !vectorList.isEmpty()) { nextPathI = intersections.size(); }
            intersections.addAll(vectorList);
        }

        Vector toCompare = absoluteGoalPoint;
        if(toCompare == null) {
            toCompare = waypoints.get(1);
            this.absoluteGoalPoint = waypoints.get(1);
        }

        System.out.println("Intersections size: " + intersections.size());

        int closestVectorI = closest(toCompare, intersections);
        if(closestVectorI == -1)
        {
            finishedPath = true;
            return null;
        }



        Vector closest = intersections.get(closestVectorI);

        if(closestVectorI >= nextPathI) { ++lastPath; }
        System.out.println(closest);
        this.absoluteGoalPoint = closest;
        return closest;
    }

    public void update()
    {
        absoluteGoalPoint = calculateAbsoluteGoalPoint();
        System.out.println(usedEstimatedLocation);
        System.out.println(usedHeading);
        System.out.println(absoluteGoalPoint);
        relativeGoalPoint = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteGoalPoint, usedEstimatedLocation, usedHeading); //
        wheelVelocities = calculateWheelVelocities();
    }

    int closest(Vector origin, List<Vector> vectors)
    {
        float minMagSquared = Float.MAX_VALUE;
        int minVectorI = -1;
        for(int i = 0; i < vectors.size(); i++)
        {

            Vector vector = vectors.get(i);

            float magnitudeSquared = origin.subtractBy(vector).getMagnitudeSquared(); // find dist squared

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
    public float getPathRadius()
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
        Vector bestVector = null;

        float v_lMax = tankRobot.getV_lMax();
        float v_rMax = tankRobot.getV_rMax();
        float v_lMin = tankRobot.getV_lMin();
        float v_rMin = tankRobot.getV_rMin();

        if(Math.abs(curvature) < THRESHOLD_CURVTURE){
            bestVector = new Vector(v_lMax, v_rMax);
            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
            pathRadius = Float.MAX_VALUE;
            leftWheelTanVel = bestVector.get(0);
            rightWheelTanVel = bestVector.get(1);
            tangentialSpeed = leftWheelTanVel;
            dThetaToRotate = 0;
        }
        else
        {
            float c = 2 / (tankRobot.getLateralWheelDistance() * curvature);
            float velLeftToRightRatio = -(c + 1) / (1 - c);
            float velRightToLeftRatio = 1 / velLeftToRightRatio;
            float score = Float.MIN_VALUE;
            //TODO: fix clumsy way of optimizing :(

            float v_r = v_lMax * velLeftToRightRatio;
            if (MathUtils.Algebra.between(v_rMin, v_r, v_rMax))
            {
                score = Math.abs(v_lMax + v_r);
                bestVector = new Vector(v_lMax, v_r);
            }

            v_r = v_lMin * velLeftToRightRatio;
            if (MathUtils.Algebra.between(v_rMin, v_r, v_rMax))
            {
                float tempScore = Math.abs(v_lMin + v_r);
                if (tempScore > score)
                {
                    score = tempScore;
                    bestVector = new Vector(v_lMin, v_r);
                }
            }

            float v_l = v_rMax * velRightToLeftRatio;
            if (MathUtils.Algebra.between(v_lMin, v_l, v_lMax))
            {
                float tempScore = Math.abs(v_lMax + v_l);
                if (tempScore > score)
                {
                    score = tempScore;
                    bestVector = new Vector(v_l, v_rMax);
                }
            }

            v_l = v_rMin * velRightToLeftRatio;
            if (MathUtils.Algebra.between(v_lMin, v_l, v_lMax))
            {
                float tempScore = Math.abs(v_lMin + v_l);
                if (tempScore > score)
                {
                    bestVector = new Vector(v_l, v_rMin);
                }
            }
            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
            pathRadius = 1 / curvature;
            leftWheelTanVel = Math.abs((pathRadius - tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
            rightWheelTanVel = Math.abs((pathRadius + tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
            tangentialSpeed = Math.abs(pathRadius * rotVelocity);
            dThetaToRotate = (float) (MathUtils.Arithmetic.sign(rotVelocity) * Math.atan(relativeGoalPoint.get(1) / (Math.abs(pathRadius) - relativeGoalPoint.get(0))));
        }

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
