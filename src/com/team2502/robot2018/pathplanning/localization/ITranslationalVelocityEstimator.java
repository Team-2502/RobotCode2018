package com.team2502.robot2018.pathplanning.localization;

import org.joml.ImmutableVector2f;

public interface ITranslationalVelocityEstimator
{
    /**
     * @return The absolute velocity of the robot
     */
    ImmutableVector2f estimateAbsoluteVelocity();

    float getLeftWheelSpeed();

    float getRightWheelSpeed();

    float estimateSpeed();

    default float avgWheelSpeed()
    {
        return (Math.abs(getLeftWheelSpeed()) + Math.abs(getRightWheelSpeed())) / 2F;
    }

    default ITranslationalVelocityEstimator getInvertedVelocity()
    {
        return new ITranslationalVelocityEstimator()
        {
            @Override
            public ImmutableVector2f estimateAbsoluteVelocity()
            {
                return ITranslationalVelocityEstimator.this.estimateAbsoluteVelocity();
            }

            @Override
            public float getLeftWheelSpeed()
            {
                return -ITranslationalVelocityEstimator.this.getRightWheelSpeed();
            }

            @Override
            public float getRightWheelSpeed()
            {
                return -ITranslationalVelocityEstimator.this.getLeftWheelSpeed();
            }

            @Override
            public float estimateSpeed()
            {
                return (getLeftWheelSpeed() + getRightWheelSpeed()) / 2F;
            }
        };
    }
}
