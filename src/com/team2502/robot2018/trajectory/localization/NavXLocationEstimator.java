package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

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
