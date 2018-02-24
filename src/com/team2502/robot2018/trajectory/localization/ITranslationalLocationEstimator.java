package com.team2502.robot2018.trajectory.localization;

import org.joml.ImmutableVector2f;

public interface ITranslationalLocationEstimator
{
    ImmutableVector2f estimateLocation();

    default ITranslationalLocationEstimator getInvertedTranslationalLocation()
    {
        return () -> {
            ImmutableVector2f loc = ITranslationalLocationEstimator.this.estimateLocation();
            return new ImmutableVector2f(-loc.x,-loc.y);
        };
    }
}
