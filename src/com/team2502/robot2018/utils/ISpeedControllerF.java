package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.SpeedController;

@Deprecated
public interface ISpeedControllerF extends SpeedController
{
    /**
     * Common interface for setting the speed of a speed controller.
     *
     * @param speed The speed to set. Value should be between -1.0 and 1.0.
     */
    void set(float speed);

    /**
     * Common interface for getting the current set speed of a speed controller.
     *
     * @return The current set speed. Value is between -1.0 and 1.0.
     */
    float getF();
}
