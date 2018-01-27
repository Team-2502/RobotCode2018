package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.utils.MathUtils;

import static com.team2502.robot2018.Constants.RAW_UNIT_PER_ROT;

/**
 * WIP, do not use yet!!
 */
@Deprecated
public class EncoderSkidSteerLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator
{
    Vector location;
    float encHeading = 0;
    float angularVel = 0;
    private long lastTime = -1;
    private IRotationalLocationEstimator rotEstimator;

    public EncoderSkidSteerLocationEstimator()
    {
        location = new Vector(0, 0);
    }

    public EncoderSkidSteerLocationEstimator(IRotationalLocationEstimator rotEstimator)
    {
        this.rotEstimator = rotEstimator;
    }

    private float getDTime()
    {
        long nanoTime = System.nanoTime();
        double dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return (float) (dTime / 1E9);
    }

    @Override
    public Vector estimateLocation()
    {
        // How many time passed
        float dTime = getDTime();

        // talon inversed
        float leftRevPerS = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0) * 10F / RAW_UNIT_PER_ROT;

        float rightRevPerS = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0) * 10F / RAW_UNIT_PER_ROT;

        float leftVelNoSlide = leftRevPerS * Constants.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        float rightVelNoSlide = rightRevPerS * Constants.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        float vTan = (leftVelNoSlide + rightVelNoSlide) / 2;

        // longitudinal slip proportion
        float i = 1 - vTan / (Constants.WHEEL_ROLLING_RADIUS_FT * angularVel);

        angularVel = MathUtils.Kinematics.getAngularVel(leftVelNoSlide, rightVelNoSlide, Constants.LATERAL_WHEEL_DISTANCE_FT);

        Vector absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVelNoSlide, rightVelNoSlide, Constants.LATERAL_WHEEL_DISTANCE_FT, dTime
                , encHeading);
        Vector absLoc = location.add(absoluteDPos);
        encHeading += angularVel * dTime;
        return absLoc;
    }

    @Override
    public float estimateHeading()
    {
        return encHeading;
    }
}
