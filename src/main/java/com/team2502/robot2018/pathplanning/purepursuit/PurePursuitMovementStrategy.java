package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.pathplanning.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.pathplanning.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.pathplanning.localization.ITranslationalVelocityEstimator;
import com.team2502.robot2018.trajectory.record.PurePursuitFrame;
import com.team2502.robot2018.utils.IStopwatch;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.PurePursuitUtils;
import com.team2502.robot2018.utils.RealStopwatch;
import org.joml.ImmutableVector2f;

import java.util.List;

/**
 * The main logic behind Pure Pursuit
 */
public class PurePursuitMovementStrategy implements ITankMovementStrategy
{
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
    private final LookaheadBounds lookahead;
    /**
     * Someone who knows how fast we're going
     */
    private final ITranslationalVelocityEstimator velocityEstimator;
    /**
     * If the robot should brake at the end or drift at the end
     */
    private final boolean driftAtEnd;
    private int updateCount = 0;
    private IStopwatch stopwatch;

    /**
     * Someone who knows where we are
     */

    private ITranslationalLocationEstimator translationalLocationEstimator;

    /**
     * Someone who knows max/min vel+accel of the robot, and how fat it is
     */
    private ITankRobotBounds tankRobot;

    /**
     * The goal point relative to us
     */
    private ImmutableVector2f relativeGoalPoint;

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
     * The lookahead we used
     */

    private float usedLookahead;

    /**
     * The speed we used
     */
    private float speedUsed;

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
    private float dCP;
    private float usedCurvature;

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
                                       List<Waypoint> waypoints, LookaheadBounds lookahead, boolean driftAtEnd)
    {
        this.path = Path.fromPoints(waypoints);

        this.tankRobot = tankRobot;
        this.translationalLocationEstimator = translationalLocationEstimator;

        this.rotEstimator = rotEstimator;
        this.lookahead = lookahead;
        this.velocityEstimator = velocityEstimator;
        this.driftAtEnd = driftAtEnd;
        lastWaypointSpeed = waypoints.get(0).getMaxSpeed();
        speedUsed = lastWaypointSpeed;
        stopwatch = new RealStopwatch();
    }

    public float getSpeedUsed()
    {
        return speedUsed;
    }

    public void setStopwatch(IStopwatch stopwatch)
    {
        this.stopwatch = stopwatch;
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
        if(brakeStage || finishedPath)
        {
            return null;
        }

        // The intersections with the path we are following and the circle around the robot of
        // radius lookAheadDistance. These intersections will determine the "goal point" we
        // will generate an arc to go to.

        return path.getGoalPoint(distanceCurrentSegmentLeft, lookAheadDistance);
    }

    private void updateForNewSegment()
    {
        PathSegment current = path.getCurrent();
        Waypoint currentWaypoint = (Waypoint) current.getFirst();

        if(currentWaypoint != null)
        {
            currentWaypoint.executeCommands(this);
        }

        lastWaypointSpeed = speedUsed;

        stopwatch.reset();
    }

    public int getUpdateCount()
    {
        return updateCount;
    }

    /**
     * Recalculates position, heading, and goalpoint.
     */
    public void update()
    {
        updateCount++;

        if(shouldEnd())
        {
            return;
        }

        if(!stopwatch.isInit())
        {
            stopwatch.reset();
        }

        usedEstimatedLocation = translationalLocationEstimator.estimateLocation();
        usedHeading = rotEstimator.estimateHeading();

        closestPoint = path.getClosestPoint(usedEstimatedLocation);
        float absDistanceOfClosestPoint = path.getAbsDistanceOfClosestPoint(closestPoint);
        float distanceTo = path.getCurrent().getAbsoluteDistanceEnd() - absDistanceOfClosestPoint;
        if(distanceTo <= 0)
        {
            if(path.moveNextSegment())
            {
                updateForNewSegment();
            }
        }


        if(closestPoint == null)
        {
            // TODO: fix jankiness
            path.moveNextSegment();
            updateForNewSegment();
            closestPoint = path.getClosestPoint(usedEstimatedLocation);
        }
        dCP = usedEstimatedLocation.sub(closestPoint).length();

        usedLookahead = PurePursuitUtils.generateLookahead(lookahead, velocityEstimator.estimateSpeed(), dCP);

        distanceLeft = path.getCurrent().getDistanceLeft(closestPoint);

        // This occurs if we are at the end of the path
        if(distanceLeft <= Constants.PurePursuit.STOP_TOLERANCE_FT && path.getCurrent().isEnd())
        {
            if(driftAtEnd)
            {
                finishedPath = true;
            }
            else
            {
                brakeStage = true;
            }
        }

        speedUsed = PurePursuitUtils.generateSpeedUsed(absDistanceOfClosestPoint, lastWaypointSpeed, stopwatch.read(), path);

        if(usedLookahead == Float.NaN)
        {
            commenceBreak();
            return;
        }

        absoluteGoalPoint = calculateAbsoluteGoalPoint(distanceLeft, usedLookahead);

        if(shouldEnd())
        {
            return;
        }
        relativeGoalPoint = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteGoalPoint, usedEstimatedLocation, usedHeading);

        usedCurvature = PurePursuitUtils.calculateCurvature(relativeGoalPoint);
        wheelVelocities = PurePursuitUtils.calculateWheelVelocities(usedCurvature, tankRobot.getLateralWheelDistance(), speedUsed);

        if(path.progressIfNeeded(distanceLeft, dCP, usedEstimatedLocation))
        {
            updateForNewSegment();
        }
    }

    public float getUsedCurvature()
    {
        return usedCurvature;
    }

    public Path getPath()
    {
        return path;
    }

    public ImmutableVector2f getClosestPoint()
    {
        return closestPoint;
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
        ImmutableVector2f circleRelativeCenter = new ImmutableVector2f(-calcMotionRadius(), 0.0F);
        ImmutableVector2f circleRelativeCenterRotated = MathUtils.LinearAlgebra.rotate2D(circleRelativeCenter, usedHeading);
        return usedEstimatedLocation.add(circleRelativeCenterRotated);
    }

    /**
     * @return The radius of the circle that the robot is traveling across. Positive if the robot is turning left, negative if right.
     */
    public float calcMotionRadius()
    { return MathUtils.epsilonEquals(usedCurvature, 0) ? Float.MAX_VALUE : 1 / usedCurvature; }

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
     * @return If the robot is finished traveling the path
     */
    public boolean isFinishedPath()
    {
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

    public PurePursuitFrame getFrame(double timeSinceInitialized)
    {
        ImmutableVector2f goalPoint = absoluteGoalPoint;
        if(goalPoint == null)
        {
            goalPoint = new ImmutableVector2f(Float.NaN, Float.NaN);
        }

        double radius = usedCurvature == 0 ? Double.POSITIVE_INFINITY : 1 / usedCurvature;

        ImmutableVector2f circleCenter = getCircleCenter();
        ImmutableVector2f closestPoint = this.closestPoint;
        if(closestPoint == null)
        {
            closestPoint = new ImmutableVector2f(Float.NaN, Float.NaN);
        }

        return new PurePursuitFrame(timeSinceInitialized, usedEstimatedLocation, usedLookahead, usedHeading, goalPoint, radius, circleCenter, path.getSegmentOnI(), closestPoint);
    }
}
