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
}
