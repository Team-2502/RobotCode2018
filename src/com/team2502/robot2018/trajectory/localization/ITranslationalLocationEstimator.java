package com.team2502.robot2018.trajectory.localization;

import org.joml.ImmutableVector2f;

public interface ITranslationalLocationEstimator
{
    ImmutableVector2f estimateLocation();
}
