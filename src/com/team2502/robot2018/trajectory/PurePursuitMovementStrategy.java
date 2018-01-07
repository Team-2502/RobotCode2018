package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PurePursuitMovementStrategy implements TankMovementStrategy{

    private final List<Vector> waypoints;
    private final LocationEstimator estimator;
    private Vector relativeGoalPoint;
    private final TankRobot tankRobot;
    private double pathRadius;
    private double rotVelocity;
    private double lookAheadDistance;
    private boolean finishedPath = false;

    private Vector usedEstimatedLocation;
    private double usedHeading;

    private int lastPath = 0;

    private Vector wheelVelocities;
    private double tangentialSpeed;
    private double leftWheelTanVel;
    private double rightWheelTanVel;
    private Vector absoluteGoalPoint;
    private double dThetaToRotate;

    public PurePursuitMovementStrategy(TankRobot tankRobot, LocationEstimator estimator, List<Vector> waypoints, double lookAheadDistance){
        this.waypoints = waypoints;
        this.tankRobot = tankRobot;
        this.lookAheadDistance = lookAheadDistance;
        this.estimator = estimator;
    }

    /*
    public Function<Double, Double> getEstimateHeadingFromTime() {
        return estimateHeadingFromTime;
    }

    public Function<Double, Vector> estimateTimeToPosition() {
        return timeToPosition;
    }

    public Function<Double, Vector> getEsimatePositionFromRotation() {
        return esimatePositionFromRotation;
    }
    */

    public Vector calculateAbsoluteGoalPoint(){
        if(finishedPath)
            return null;
        List<Vector> intersections = new ArrayList<>();
        int nextPathI = Integer.MAX_VALUE;
        usedEstimatedLocation = estimator.estimateLocation();
        floop:
        for(int i =lastPath; i<= lastPath+1; i++){
            if(i+1 >= waypoints.size())
                continue floop;
            Vector lineP1 = waypoints.get(i);
            Vector lineP2 = waypoints.get(i + 1);
            double toLookAhead = lookAheadDistance;
            List<Vector> vectorList = new ArrayList<>(MathUtils.Geometry.
                    getCircleLineIntersectionPoint(lineP1, lineP2, usedEstimatedLocation, toLookAhead));
            vectorList.removeIf(vector -> !vector.between(lineP1,lineP2));
            if(i == lastPath+1 && !vectorList.isEmpty())
                nextPathI = intersections.size();
            intersections.addAll(vectorList);
        }

        Vector toCompare = absoluteGoalPoint;
        if(toCompare == null){
            toCompare = waypoints.get(1);
        }
        int closestVectorI = closest(toCompare,intersections);
        if(closestVectorI == -1){
            finishedPath = true;
            return null;
        }
        Vector closest = intersections.get(closestVectorI);
        if(closestVectorI >= nextPathI){
            lastPath++;
        }
        return closest;
    }

    public void update() {
        absoluteGoalPoint = calculateAbsoluteGoalPoint();
        relativeGoalPoint = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteGoalPoint,usedEstimatedLocation,usedHeading);
        wheelVelocities = calculateWheelVelocities();
    }

    public double getLookAheadDistance() {
        return lookAheadDistance;
    }

    int closest(Vector origin, List<Vector> vectors){
        double minMagSquared = Double.MAX_VALUE;
        int minVectorI = -1;
        for(int i = 0; i < vectors.size(); i++){
            Vector vector = vectors.get(i);
            double magnitudeSquared = origin.subtractBy(vector).getMagnitudeSquared();
            if(magnitudeSquared < minMagSquared){
                minMagSquared = magnitudeSquared;
                minVectorI = i;
            }
        }
        return minVectorI;
    }

    /**
     * To see if goal points are continuous
     * @param 
     * @return
     */
    boolean isValidGoalPoint(Vector goalPoint){
        if(this.relativeGoalPoint == null)
            return true;
        if(this.relativeGoalPoint.get(1) < 0)
            return false;
        return true;
    }

    private double curvatureToGoal(){
        double lSquared = relativeGoalPoint.getMagnitudeSquared(); // x^2 + y^2 = l^2 (length)
        return -2* relativeGoalPoint.get(0)/lSquared;
    }

    public Vector getCircleCenter(){
        Vector circleRelativeCenter = new Vector(-pathRadius,0);
        Vector circleRelativeCenterRotated = MathUtils.LinearAlgebra.rotate2D(circleRelativeCenter, usedHeading);
        return usedEstimatedLocation.add(circleRelativeCenterRotated);
    }

    /**
     *
     * @return The radius of the circle that the robot is traveling across. Positive if the robot is turning left, negative if right.
     */
    public double getPathRadius() {
        return pathRadius;
    }

    public Vector getWheelVelocities() {
        return wheelVelocities;
    }


    public Vector getUsedEstimatedLocation()
    {
        return usedEstimatedLocation;
    }

    private Vector calculateWheelVelocities(){
        double curvature = curvatureToGoal();
        double c = 2/(tankRobot.getLateralWheelDistance()*curvature);
        double velLeftToRightRatio = -( c + 1 ) / (1-c);
        double velRightToLeftRatio = 1/velLeftToRightRatio;
        double score = -Integer.MAX_VALUE;
        Vector bestVector = null;
        //TODO: fix clumsy way of optimizing :(

        double v_lMax = tankRobot.getV_lMax();
        double v_rMax = tankRobot.getV_rMax();
        double v_lMin = tankRobot.getV_lMin();
        double v_rMin = tankRobot.getV_rMin();

        double v_r = v_lMax * velLeftToRightRatio;
        if(MathUtils.Algebra.between(v_rMin, v_r,v_rMax)){
            score = Math.abs(v_lMax+v_r);
            bestVector = new Vector(v_lMax,v_r);
        }

        v_r = v_lMin * velLeftToRightRatio;
        if(MathUtils.Algebra.between(v_rMin, v_r,v_rMax)){
            double tempScore = Math.abs(v_lMin + v_r);
            if(tempScore > score){
                score = tempScore;
                bestVector = new Vector(v_lMin,v_r);
            }
        }

        double v_l = v_rMax * velRightToLeftRatio;
        if(MathUtils.Algebra.between(v_lMin, v_l,v_lMax)){
            double tempScore = Math.abs(v_lMax + v_l);
            if(tempScore > score){
                score = tempScore;
                bestVector = new Vector(v_l,v_rMax);
            }
        }

        v_l = v_rMin * velRightToLeftRatio;
        if(MathUtils.Algebra.between(v_lMin, v_l,v_lMax)){
            double tempScore = Math.abs(v_lMin + v_l);
            if(tempScore > score){
                bestVector = new Vector(v_l,v_rMin);
            }
        }

        rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
        // Note this can be negative

        pathRadius = 1 / curvature;

        leftWheelTanVel = Math.abs((pathRadius - tankRobot.getLateralWheelDistance() / 2)*rotVelocity);
        rightWheelTanVel = Math.abs((pathRadius + tankRobot.getLateralWheelDistance() / 2)*rotVelocity);
        tangentialSpeed = Math.abs(pathRadius * rotVelocity);
        usedHeading = tankRobot.getHeading();

        dThetaToRotate = MathUtils.Arithmetic.sign(rotVelocity) * Math.atan(relativeGoalPoint.get(1) / (Math.abs(pathRadius) - relativeGoalPoint.get(0)));

        Function<Double,Vector> estimatePositionFromDTheta = dTheta -> {
            double dxRelative = -pathRadius *(1-Math.cos(-dTheta));
            double dyRelative = -pathRadius *Math.sin(-dTheta);
            Vector dRelativeVector = new Vector(dxRelative, dyRelative);
            Vector rotated = MathUtils.LinearAlgebra.rotate2D(dRelativeVector, usedHeading);
            Vector toReturn = rotated.add(usedEstimatedLocation);
            return toReturn;
        };



        /*
        estimateHeadingFromTime = time-> {
            double heading = usedHeading + rotVelocity*time;
            return heading;
        };

        esimatePositionFromRotation = angle -> {
            double dTheta = angle - usedHeading;
            return estimatePositionFromDTheta.apply(dTheta);
        };

        estimatedTime = thetaToRotate / rotVelocity;

        timeToPosition = time -> {
            double dTheta = time * rotVelocity;
            return estimatePositionFromDTheta.apply(dTheta);
        };
        */

        return bestVector;
    }

    public double getTangentialSpeed() {
        return tangentialSpeed;
    }

    public double getLeftWheelTanVel() {
        return leftWheelTanVel;
    }

    public double getRightWheelTanVel() {
        return rightWheelTanVel;
    }

    public List<Vector> getWaypoints()
    {
        return waypoints;
    }

    public Vector getRelativeGoalPoint()
    {
        return relativeGoalPoint;
    }

    public double getRotVelocity()
    {
        return rotVelocity;
    }

    public boolean isFinishedPath()
    {
        return finishedPath;
    }

    public double getUsedHeading()
    {
        return usedHeading;
    }

    public Vector getAbsoluteGoalPoint() {
        return absoluteGoalPoint;
    }

    public double getdThetaToRotate()
    {
        return dThetaToRotate;
    }
}
