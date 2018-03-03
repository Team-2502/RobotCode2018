package com.team2502.robot2018;

import com.team2502.robot2018.trajectory.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalVelocityEstimator;
import org.joml.ImmutableVector2f;

public class RobotLocalizationThread extends Thread implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator, IRotationalLocationEstimator
{

    private final IRotationalLocationEstimator rotEstimator;
    private final ITranslationalVelocityEstimator velocityEstimator;
    private final ITranslationalLocationEstimator locationEstimator;
    private final long msPeriod;
    private float heading, leftWheelSpeed, rightWheelSpeed,speed;
    private ImmutableVector2f location,velocity;

    public RobotLocalizationThread(IRotationalLocationEstimator rotEstimator,
                                   ITranslationalVelocityEstimator velocityEstimator, ITranslationalLocationEstimator locationEstimator, long msPeriod)
    {
        this.rotEstimator = rotEstimator;
        this.velocityEstimator = velocityEstimator;
        this.locationEstimator = locationEstimator;
        this.msPeriod = msPeriod;
    }

    @Override
    public void run()
    {
        while(!isInterrupted())
        {
            heading = rotEstimator.estimateHeading();
            leftWheelSpeed = velocityEstimator.getLeftWheelSpeed();
            rightWheelSpeed = velocityEstimator.getRightWheelSpeed();
            location = locationEstimator.estimateLocation();
            velocity = velocityEstimator.estimateAbsoluteVelocity();
            speed = velocityEstimator.estimateSpeed();
            try
            {
                sleep(msPeriod);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public float estimateHeading()
    {
        return heading;
    }

    @Override
    public ImmutableVector2f estimateLocation()
    {
        return location;
    }

    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        return velocity;
    }

    @Override
    public float getLeftWheelSpeed()
    {
        return leftWheelSpeed;
    }

    @Override
    public float getRightWheelSpeed()
    {
        return rightWheelSpeed;
    }

    @Override
    public float estimateSpeed()
    {
        return speed;
    }
}
