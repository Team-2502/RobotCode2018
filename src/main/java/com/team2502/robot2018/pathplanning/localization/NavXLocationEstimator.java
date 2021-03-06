package com.team2502.robot2018.pathplanning.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

/**
 * Uses the navX to estimate angle of the robot using the gyro
 * (magnetometer = compass is too inaccurate and slow + it is bad w/ motors ... just overall horrible)
 * navX can also be used to estimate the absolute location of the robot using the accelerometer.
 * However, this is generally very inaccurate and should instead be done by combining this with
 * {@link EncoderDifferentialDriveLocationEstimator}.
 */
public class NavXLocationEstimator implements IRotationalLocationEstimator, ITranslationalLocationEstimator
{
    double initHeading;
    ImmutableVector2f initPosition;

    // TODO: Need to fix to have pi/2 init heading


    /**
     * Make a new estimator for our angle
     */
    public NavXLocationEstimator()
    {
        initHeading = -Robot.NAVX.getAngle();
        initPosition = estimateLocation();
    }

    /**
     * Read the value from the NavX and convert the angle to radians
     *
     * @return Theta of our robot in radians
     */
    @Override
    public float estimateHeading()
    {
        // switch direction of increase
        double yawDegTotal = -Robot.NAVX.getAngle();
        return (float) MathUtils.Kinematics.navXToRad(yawDegTotal - initHeading);
    }

    /**
     * @return
     * @deprecated bad! Accurate to 1m after 15 s because of accelerometer noise
     */
    @Override
    public ImmutableVector2f estimateLocation()
    { return new ImmutableVector2f(Robot.NAVX.getDisplacementX(), Robot.NAVX.getDisplacementY()); }
}
