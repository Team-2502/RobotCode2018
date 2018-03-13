package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.Stopwatch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

/**
 *  Localization using encoders which can is primarily used for estimating the speed of the robot.
 *  If an {@link IRotationalLocationEstimator} such as {@link NavXLocationEstimator} is added, it can do much more,
 *  estimating the absolute position of the robot.
 */
public class EncoderDifferentialDriveLocationEstimator implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator
{
    private ImmutableVector2f location;
    private IRotationalLocationEstimator rotEstimator;
    private Stopwatch stopwatch;

    /**
     * Make a new position estimator
     * @param rotEstimator a rotation estimator
     */
    public EncoderDifferentialDriveLocationEstimator(IRotationalLocationEstimator rotEstimator)
    {
        location = new ImmutableVector2f(0, 0);
        stopwatch = new Stopwatch();
        this.rotEstimator = rotEstimator;
    }

    /**
     * Estimate our location
     * @return our location
     */
    @Override
    public ImmutableVector2f estimateLocation()
    {
        // figure out time since last estimated
        float dTime = stopwatch.poll();
        float leftVel = Robot.DRIVE_TRAIN.getLeftVel();
        float rightVel = Robot.DRIVE_TRAIN.getRightVel();

        // figure out how much our position has changed
        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosLine(leftVel, rightVel, dTime, rotEstimator.estimateHeading());

        // add to our running total
        location = location.add(dPos);

        // log data on shuffleboard
        SmartDashboard.putNumber("posX", location.x);
        SmartDashboard.putNumber("posY", location.y);

        return location;
    }

    /**
     * @return A unit vector pointing in the direction of our movement.
     */
    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        ImmutableVector2f vector = MathUtils.Geometry.getVector(estimateSpeed(), rotEstimator.estimateHeading());
        return vector;
    }

    @Override
    public float getLeftWheelSpeed()
    {
        return Robot.DRIVE_TRAIN.getLeftVel();
    }

    @Override
    public float getRightWheelSpeed()
    {
        return Robot.DRIVE_TRAIN.getRightVel();
    }

    @Override
    public float estimateSpeed()
    {
        return MathUtils.Kinematics.getTangentialSpeed(getLeftWheelSpeed(), getRightWheelSpeed());
    }
}

