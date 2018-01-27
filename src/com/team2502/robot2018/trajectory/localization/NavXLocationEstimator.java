package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.utils.MathUtils;

public class NavXLocationEstimator implements IRotationalLocationEstimator, ITranslationalLocationEstimator
{

    float initHeading;
    Vector initPosition;

    public NavXLocationEstimator()
    {
        initHeading = estimateHeading();
        initPosition = estimateLocation();
    }

    @Override
    public float estimateHeading()
    {
        // switch direction of increase
        float yawDeg = -Robot.NAVX.getYaw();
        return navXToRad(yawDeg);
    }

    float navXToRad(float yawDeg)
    {
        if(yawDeg < 0)
        {
            yawDeg = 360 + yawDeg;
        }
        return MathUtils.deg2Rad(yawDeg);
    }

    /**
     * @return
     * @deprecated bad! Accurate to 1m
     */
    @Override
    public Vector estimateLocation()
    {
        return new Vector(Robot.NAVX.getDisplacementX(), Robot.NAVX.getDisplacementY());
    }
}
