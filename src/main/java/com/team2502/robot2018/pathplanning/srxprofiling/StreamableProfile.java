package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.TrajectoryPoint;

/**
 * An interface for things containing {@link TrajectoryPoint}s that can be streamed to the Talons
 *
 *
 */
public interface StreamableProfile
{
    TrajectoryPoint[] getLeftPoints();

    TrajectoryPoint[] getRightPoints();
}
