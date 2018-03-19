package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalVelocityEstimator;
import com.team2502.robot2018.utils.Files;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;
import java.util.List;

/**
 * The main logic behind Pure Pursuit
 */
public class PurePursuitMovementStrategy implements ITankMovementStrategy
{
    /**
     * This represents the threshold curvature beyond which it's basically a straight line.
     */
    private static final float THRESHOLD_CURVATURE = 0.001F;

    /**
     * The path that we're driving on
     */
    private final Path path;

    /**
     * Something that knows where we're pointing
     */
    private final IRotationalLocationEstimator rotEstimator;

    /**
     * Something that can automatically generate a lookahead for us
     */
    private final Lookahead lookahead;

    /**
     * Someone who knows how fast we're going
     */
    private final ITranslationalVelocityEstimator velocityEstimator;

    /**
     * If the robot should brake at the end or drift at the end
     */
    private final boolean driftAtEnd;

    /**
     * Someone who knows where we are
     */

    private ITranslationalLocationEstimator translationalLocationEstimator;

    /**
     * Someone who knows max/min vel+accel of the robot, and how fat it is
     */
    private ITankRobotBounds tankRobot;

    /**
     * Are we driving forward?
     */
    private boolean forward;

    /**
     * The goal point relative to us
     */
    private ImmutableVector2f relativeGoalPoint;

    /**
     * The radius of our motion, assuming that our current path if we didn't change motor velocity would follow a circle
     */
    private float motionRadius;

    /**
     * How fast we're rotating
     */
    private float rotVelocity;

    /**
     * If we're done
     */
    private boolean finishedPath = false;

    /**
     * Where we think we are
     */
    private ImmutableVector2f usedEstimatedLocation = new ImmutableVector2f();

    /**
     * Where we think we're pointing
     */
    private float usedHeading = 0.0F;
    private float lastWaypointSpeed = 0;

    /**
     * Velocities of left and right wheel
     */
    private ImmutableVector2f wheelVelocities;

    /**
     * Speed tangential to our current path
     */
    private float tangentialSpeed;

    private float leftWheelTanVel;
    private float rightWheelTanVel;

    /**
     * Goal point in absolute coordinates, where (0, 0) is our starting point.
     */
    private ImmutableVector2f absoluteGoalPoint;

    /**
     * How much we need to rotate
     */
    private float dThetaToRotate;

    /**
     * If we're close to our next waypoint
     */
    private boolean isClose = false;

    /**
     * If we are within tolerances for finishing
     */
    private boolean withinTolerences;

    /**
     * The lookahead we used
     */

    private float usedLookahead;

    /**
     * The speed we used
     */
    private float speedUsed;

    /**
     * When we last updated where we're going
     */
    private double lastUpdatedS = -1;

    /**
     * The current time
     */
    private double currentS;

    /**
     * Distance remaining
     */
    private float distanceLeft;

    /**
     * If we should start braking
     */
    private boolean brakeStage;

    /**
     * The tangential velocity we used in our calculations
     */
    private float usedTangentialVelocity;
    private ImmutableVector2f closestPoint;

    /**
     * Strategize your movement!
     *
     * @param tankRobot                      An instance of ITanRobotBounds, an interface that has getters for robot max speed and accel.
     * @param translationalLocationEstimator An estimator for the absolute position of the robot
     * @param rotEstimator                   An estimator for the heading of the robot
     * @param waypoints                      A list of waypoints for the robot to drive through
     * @param lookahead                      The lookahead distance for the pure pursuit algorithm
     */
    public PurePursuitMovementStrategy(ITankRobotBounds tankRobot, ITranslationalLocationEstimator translationalLocationEstimator,
                                       IRotationalLocationEstimator rotEstimator, ITranslationalVelocityEstimator velocityEstimator,
                                       List<Waypoint> waypoints, Lookahead lookahead, boolean driftAtEnd)
    {
        this.path = new Path(waypoints);

        this.tankRobot = tankRobot;
        this.translationalLocationEstimator = translationalLocationEstimator;

        this.rotEstimator = rotEstimator;
        this.lookahead = lookahead;
        this.velocityEstimator = velocityEstimator;
        this.driftAtEnd = driftAtEnd;
    }

    /**
     * @return The absolute location of the selected goal point.
     * The goal point is a point on the path 1 lookahead distance away from us.
     * We want to drive at it.
     * @see <a href="https://www.chiefdelphi.com/forums/showthread.php?threadid=162713">Velocity and End Behavior (Chief Delphi)</a>
     */
    private ImmutableVector2f calculateAbsoluteGoalPoint(float distanceCurrentSegmentLeft, float lookAheadDistance)
    {
        // The path is finished — there are no more goal points to compute
        if(brakeStage || finishedPath) { return null; }

        // The intersections with the path we are following and the circle around the robot of
        // radius lookAheadDistance. These intersections will determine the "goal point" we
        // will generate an arc to go to.

        return path.getGoalPoint(distanceCurrentSegmentLeft, lookAheadDistance);
    }

    private void updateForNewSegment()
    {
        PathSegment current = path.getCurrent();
        Waypoint currentWaypoint = current.getFirst();

        if(currentWaypoint != null)
        {
            ImmutableVector2f locationB = currentWaypoint.getLocation();
            Robot.writeLog("next waypoint (%.2f,%.2f) ... lookahead: %.2f ... pos: (%.2f,%.2f)", 10, locationB.x, locationB.y, usedLookahead, usedEstimatedLocation.x, usedEstimatedLocation.y);
            currentWaypoint.executeCommands(this);
        }

        lastWaypointSpeed = speedUsed;

        lastUpdatedS = currentS;

        Robot.writeLog("LAST WAYPOINT SPEED: " + lastWaypointSpeed, 1);
    }

    private float generateLookahead()
    {
        float usedTangentialVelocity = velocityEstimator.estimateSpeed();
        float lookaheadForSpeed = lookahead.getLookaheadForSpeed(usedTangentialVelocity);

        PathSegment current = path.getCurrent();

        Waypoint waypointEnd = current.getLast();

        forward = waypointEnd.isForward();

        float pathSegmentLength = current.getLength();

        closestPoint = path.getClosestPoint(usedEstimatedLocation);
        float closestPointPathDistance = path.getClosestPointPathDistance(closestPoint);
        ImmutableVector2f distanceClosestPoint = usedEstimatedLocation.sub(closestPoint);

        float distanceAlongPath = closestPointPathDistance - current.getAbsoluteDistanceStart();

        distanceLeft = pathSegmentLength - distanceAlongPath;

        Robot.writeLog("distanceLeft: %.2f, pathSegmentLength: %.2f, distanceAlongPath: %.2f", 1, distanceLeft, pathSegmentLength, distanceAlongPath);

        // This occurs if we are at the end of the path
        if(distanceLeft <= 0 && current.isEnd())
        {
            if(driftAtEnd)
            {
                Robot.writeLog("Commencing drift",100);
                finishedPath = true;
            }
            else
            {
                Robot.writeLog("Commencing brake (getLookahead())",100);
                brakeStage = true;
            }
            return Float.NaN;
        }

        float finalSpeed = waypointEnd.isForward() ? waypointEnd.getMaxSpeed() : -waypointEnd.getMaxSpeed();

        Robot.writeLog("distance left: " + distanceLeft, 1);
        Robot.writeLog("speedUsed: " + speedUsed, 1);

        float speed = Float.MAX_VALUE;

        // Looks within 15 feet ahead
        for(PathSegment pathSegment : path.nextSegmentsInclusive(15))
        {
            Waypoint last = pathSegment.getLast();
//            ImmutableVector2f location = last.getLocation();
//            Robot.writeLog("path segment last waypoint: (%.2f,%.2f)", 80, location.x,location.y);
//            Robot.writeLog("last max speed , waypoint speed (%.2f,%.2f)", 80,lastWaypointSpeed, last.getMaxSpeed());
//            if(last.getMaxSpeed() < lastWaypointSpeed)
//            {
                float distanceTo = pathSegment.getAbsoluteDistanceEnd() - closestPointPathDistance;
                float maxSpeed = getMaxSpeed(last.getMaxSpeed(), distanceTo, last.isForward(), waypointEnd.getMaxAccel(), waypointEnd.getMaxDeccel());
//                Robot.writeLog("maxSpeed: %.2f, distanceTo: %.2f", 80, maxSpeed, distanceTo);

                if(maxSpeed < speed)
                {
                    speed = maxSpeed;
                }
//            }
        }

        if(speed < lastWaypointSpeed)
        {
            Robot.writeLog("forward decel", 80);
            speedUsed = speed;
        }
        else if((finalSpeed > 0 && finalSpeed > lastWaypointSpeed) || (finalSpeed < 0 && finalSpeed < lastWaypointSpeed))
        {
            // what the motors should be
            float dTime = (float) (currentS - lastUpdatedS);
            if(forward)
            {
                Robot.writeLog("forward accel", 80);
                Robot.writeLog("forward", 1);
                speedUsed = MathUtils.minF(finalSpeed, lastWaypointSpeed + dTime * waypointEnd.getMaxAccel());
            }
            else
            {
                Robot.writeLog("not forward", 1);
                speedUsed = MathUtils.maxF(finalSpeed, lastWaypointSpeed + dTime * waypointEnd.getMaxDeccel());
            }
            Robot.writeLog("accel ... speedUsed: %.2f, poll: %.2f, lastSpeed: %.2f, aMax %.2f", 1, speedUsed, dTime, lastWaypointSpeed, waypointEnd.getMaxAccel());
        }

        Robot.writeLog("speed: %.2f, speedUsed: %.2f, finalSpeed %.2f", 80, speed, speedUsed,finalSpeed);

        float dCP = distanceClosestPoint.length();

        float usedLookahead = lookaheadForSpeed + dCP;

        Robot.writeLog("usedVel: %.2f, usedLookahead %.2f", 30, usedTangentialVelocity, usedLookahead);

        return usedLookahead;
    }

    private float getMaxSpeed(float finalSpeed, float distanceLeft, boolean forward, float currentMaxAccel, float currentMaxDeccel)
    {
        float speed;
        if(forward)
        {
            float maxVel = (float) Math.sqrt(finalSpeed * finalSpeed - 2 * currentMaxDeccel * distanceLeft);
            speed = Math.min(lastWaypointSpeed, maxVel);
        }
        else
        {
            // TODO: Not sure this works
            // closest to 0 ft/s speed
            float minVel = (float) Math.sqrt(finalSpeed * finalSpeed + 2 * currentMaxAccel * distanceLeft);
            speed = Math.max(lastWaypointSpeed, minVel);
        }
        return speed;
    }

    /**
     * Recalculates position, heading, and goalpoint.
     */
    public void update()
    {
//        Robot.writeLog("update", 80);

        if(shouldEnd())
        {
            return;
        }

        currentS = System.currentTimeMillis() / 1000D;
        if(lastUpdatedS == -1)
        {
            lastUpdatedS = currentS;
        }

        usedEstimatedLocation = translationalLocationEstimator.estimateLocation();
        Robot.writeLog("estimated Loc (%.2f,%.2f)", 80,usedEstimatedLocation.x,usedEstimatedLocation.y);
        usedHeading = rotEstimator.estimateHeading();

        usedLookahead = generateLookahead();
        Robot.writeLog("generated Lookahead, %.2f", 1, usedLookahead);
        if(usedLookahead == Float.NaN)
        {
            Robot.writeLog("generated Lookahead, %.2f", 100, "Lookahead is NaN");
            commenceBreak();
            return;
        }
        Robot.writeLog("lookAhead: %.2f", 80,usedLookahead);
        absoluteGoalPoint = calculateAbsoluteGoalPoint(distanceLeft,usedLookahead);
//        Robot.writeLog("abs GP: (%.2f, %.2f)", 80,absoluteGoalPoint.x,absoluteGoalPoint.y);

        if(shouldEnd())
        {
            return;
        }

        relativeGoalPoint = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteGoalPoint, usedEstimatedLocation, usedHeading);

        Robot.writeLog("relativeGP: (%.2f,%.2f)", 30, relativeGoalPoint.x, relativeGoalPoint.y);

        wheelVelocities = calculateWheelVelocities();

        Files.setNameAndValue("Wheel Velocity (planned) L", wheelVelocities.x);
        Files.setNameAndValue("Wheel Velocity (planned) R", wheelVelocities.y);

        Files.setNameAndValue("Relative Goal Point x", relativeGoalPoint.x);
        Files.setNameAndValue("Relative Goal Point y", relativeGoalPoint.y);

        Files.setNameAndValue("Abs Goal Point x", absoluteGoalPoint.x);
        Files.setNameAndValue("Abs Goal Point y", absoluteGoalPoint.y);

        Files.setNameAndValue("Est Loc x", usedEstimatedLocation.x);
        Files.setNameAndValue("Abs Goal Point y", usedEstimatedLocation.y);

        if(path.progressIfNeeded(closestPoint))
        {
            Robot.writeLog("updating for new segment!",80);
            updateForNewSegment();
        }
    }

    private boolean shouldEnd()
    {
        if(finishedPath)
        {
            return true;
        }

        if(isBrakeStage())
        {
            commenceBreak();
            return true;
        }
        return false;
    }

    private void commenceBreak()
    {
        wheelVelocities = new ImmutableVector2f(0, 0);
        if(Math.abs(velocityEstimator.estimateSpeed()) < 0.1F)
        {
            finishedPath = true;
        }
    }

    public boolean isBrakeStage()
    {
        return brakeStage;
    }

    public float getUsedLookahead()
    {
        return usedLookahead;
    }

    /**
<<<<<<< HEAD
=======
     * Chooses which goal point should be chosen given a list of possible goal points (generated by circle-line intersection)
     *
     * @param origin             the absolute location of the robot
     * @param possibleGoalPoints the absolute location of goal points
     */
    int bestGoalPoint(ImmutableVector2f origin, List<ImmutableVector2f> possibleGoalPoints)
    {
        float minMagSquared = Float.MAX_VALUE;
        int minVectorI = -1;
        for(int i = 0; i < possibleGoalPoints.size(); ++i)
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
>>>>>>> develop
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
     * @return The lateral distance (with respect to the robot) between the robot and the goal point.
     */
    public float getCrossTrackError()
    {
        return relativeGoalPoint.get(0);
    }

    /**
     * @return The center of the circle that the robot is travelling on
     */
    public ImmutableVector2f getCircleCenter()
    {
        ImmutableVector2f circleRelativeCenter = new ImmutableVector2f(-motionRadius, 0.0F);
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

        float v_lMax = speedUsed;
        float v_rMax = speedUsed;
        float v_lMin = -speedUsed;
        float v_rMin = -speedUsed;

        if(Math.abs(curvature) < THRESHOLD_CURVATURE) // if we are a straight line ish (lines are not curvy -> low curvature)
        {
            bestVector = forward ? new ImmutableVector2f(v_lMax, v_rMax) : new ImmutableVector2f(v_lMin, v_rMin);
            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
            motionRadius = Float.MAX_VALUE;
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
                throw new NullPointerException("bestVector is null!");
            }

            rotVelocity = (bestVector.get(1) - bestVector.get(0)) / tankRobot.getLateralWheelDistance();
            motionRadius = 1 / curvature;
            leftWheelTanVel = Math.abs((motionRadius - tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
            rightWheelTanVel = Math.abs((motionRadius + tankRobot.getLateralWheelDistance() / 2) * rotVelocity);
            tangentialSpeed = (leftWheelTanVel + rightWheelTanVel) / 2;
            dThetaToRotate = (float) (Math.signum(rotVelocity) * Math.atan(relativeGoalPoint.get(1) / (Math.abs(motionRadius) - relativeGoalPoint.get(0))));
        }

        if(forward)
        {
            return bestVector;
        }
        else
        {
            return new ImmutableVector2f(-bestVector.y, -bestVector.x);
        }
    }

    /**
     * @return The radius of the circle that the robot is traveling across. Positive if the robot is turning left, negative if right.
     */
    public float getMotionRadius()
    { return motionRadius; }

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

    /**
     * @return If the robot is finished traveling the path
     */
    public boolean isFinishedPath()
    {
        Robot.writeLog("isFinised? %b, %b", 1, finishedPath, !path.exists());
        return finishedPath || !path.exists();
    }

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
