package com.team2502.robot2018.trajectory.localization;

import org.joml.ImmutableVector2f;

/**
 * An interface for any class trying to estimate our location
 */
public interface ITranslationalLocationEstimator
{
    /**
     * In (x, y) coordinates where the y axis is parallel to the long side of the field, return our position
     * @return Our position
     */
    ImmutableVector2f estimateLocation();

    default ITranslationalLocationEstimator getInvertedTranslationalLocation()
    {
        return () -> {
            ImmutableVector2f loc = ITranslationalLocationEstimator.this.estimateLocation();
            return new ImmutableVector2f(-loc.x, -loc.y);
        };
    }
}
