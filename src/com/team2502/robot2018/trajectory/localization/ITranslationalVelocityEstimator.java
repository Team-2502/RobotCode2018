package com.team2502.robot2018.trajectory.localization;

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

    default ITranslationalVelocityEstimator getInverted()
    {
        return new ITranslationalVelocityEstimator() {
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
                return (getLeftWheelSpeed()+getRightWheelSpeed())/2F;
            }
        };
    }
}
