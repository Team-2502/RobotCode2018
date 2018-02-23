package com.team2502.robot2018.trajectory.localization;

public interface IRotationalLocationEstimator
{
    float estimateHeading();

    /**
     * Since counterclockwise is always counterclockwise, even if reflected (try this in a mirror)!
     * @return
     */
    default float esimateHeadingInverted()
    {
        return estimateHeading();
    }
}
