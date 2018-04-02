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

    public NavXLocationEstimator()
    {
        initHeading = -Robot.NAVX.getAngle();
        initPosition = estimateLocation();
    }

    @Override
    public float estimateHeading()
    {
        // switch direction of increase
        double yawDegTotal = -Robot.NAVX.getAngle();
        return (float) navXToRad(yawDegTotal - initHeading);
    }

    double navXToRad(double yawDegTot)
    {
        double yawDeg = yawDegTot % 360;
        if(yawDeg < 0) { yawDeg = 360 + yawDeg; }
        return MathUtils.deg2Rad(yawDeg);
    }

    /**
     * @return
     * @deprecated bad! Accurate to 1m
     */
    @Override
    public ImmutableVector2f estimateLocation()
    { return new ImmutableVector2f(Robot.NAVX.getDisplacementX(), Robot.NAVX.getDisplacementY()); }
}
