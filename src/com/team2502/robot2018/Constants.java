package com.team2502.robot2018;

import com.team2502.ctannotationprocessor.Undefined;

/**
 * Created by 64009334 on 1/19/18.
 */
public class Constants
{
    public static final float MAX_FPS_SPEED = 18.0F;

    //        public static final float WHEEL_DIAMETER = 6.0F;
    public static final float WHEEL_DIAMETER = 4.0F;

    public static final float POS_TO_FEET = (WHEEL_DIAMETER * (float) Math.PI) / (4096.0F * 12.0F);
    public static final float VEL_TO_RPM = (600.0F / 4096.0F);
    public static final float VEL_TO_FPS = VEL_TO_RPM / 60 * (WHEEL_DIAMETER * (float) Math.PI) / 12.0F;

    public static final int INIT_TIMEOUT = 10; // When initializing a sensor/whatever, the timeout will be 10 ms
    public static final int LOOP_TIMEOUT = 0; // When doing things in a loop, there won't be a timeout

    @Undefined(safe = true)
    public static final double SHIFT_UP_THRESHOLD = RobotMap.UNDEFINED;
    @Undefined(safe = true)
    public static final double SHIFT_DOWN_THRESHOLD = RobotMap.UNDEFINED;

    public static final float WHEEL_DIAMETER_INCH = 4F;
    public static final float WHEEL_DIAMETER_FT = WHEEL_DIAMETER_INCH / 12F;
    public static final float WHEEL_ROLLING_RADIUS_INCH = 1.5F;
    public static final float WHEEL_ROLLING_RADIUS_FT = WHEEL_ROLLING_RADIUS_INCH * 1.5F / 12F;
}
