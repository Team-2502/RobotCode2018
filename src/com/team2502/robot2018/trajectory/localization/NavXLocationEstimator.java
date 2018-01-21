package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.Vector2f;

public class NavXLocationEstimator implements IRotationalLocationEstimator, ITranslationalLocationEstimator
{

    float initHeading;
    Vector2f initPosition;

    public NavXLocationEstimator(){
        initHeading = estimateHeading();
        initPosition = estimateLocation();
    }

    @Override
    public float estimateHeading()
    {
        // switch direction of increase
        float yawDeg = -Robot.NAVX.getYaw();
        if(yawDeg < 0)
        {
            yawDeg = 360 + yawDeg;
        }
        return MathUtils.deg2Rad(yawDeg);
    }

    /**
     * @deprecated bad! Accurate to 1m
     * @return
     */
    @Override
    public Vector2f estimateLocation()
    {
        return new Vector2f(Robot.NAVX.getDisplacementX(),Robot.NAVX.getDisplacementY());
    }
}
