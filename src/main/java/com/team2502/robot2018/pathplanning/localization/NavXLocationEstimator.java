package com.team2502.robot2018.pathplanning.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

/**
 * Uses the navX to estimate angle of the robot using the gyro
 */
public class NavXLocationEstimator implements IRotationalLocationEstimator
{
    double initHeading;

    // TODO: Need to fix to have pi/2 init heading

    /**
     * Make a new estimator for our angle
     */
    public NavXLocationEstimator()
    {
        initHeading = Robot.NAVX.getAngle();
//        initPosition = estimateLocation();
    }

    /**
     * Read the value from the NavX and convert the angle to radians
     *
     * @return Theta of our robot in radians
     */
    @Override
    public float estimateHeading()
    {
        // switch direction to have angle increase ccw like radians do
        double yawDegTotal = Robot.NAVX.getAngle();
        return (float) MathUtils.Kinematics.navXToRad(yawDegTotal - initHeading);
    }
}
