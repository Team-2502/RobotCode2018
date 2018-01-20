package com.team2502.robot2018;

import com.team2502.ctannotationprocessor.Undefined;

/**
 * Note E (EVEL, ENC_RES, EPOS) is special encoder units
 */
public class Constants
{

    /*
    Pure Pursuit
     */

    // Currently the max percent VOLTAGE that can be given to each to each wheel
    public static final float VR_MAX = .75F;
    public static final float VL_MAX = .75F;
    public static final float VR_MIN = -.75F;
    public static final float VL_MIN = -.75F;

    // The lookahead distance (feet) for Pure Pursuit
    public static final float LOOKAHEAD_DISTANCE_FT = 2F;

    /*
    Physical / Other
     */

    public static final float WHEEL_DIAMETER_INCH = 4F;
    public static final float WHEEL_DIAMETER_FT = WHEEL_DIAMETER_INCH / 12F;

    // half of the width of the wheel that is in contact with the ground
    public static final float WHEEL_ROLLING_RADIUS_INCH = 1.5F;
    public static final float WHEEL_ROLLING_RADIUS_FT = WHEEL_ROLLING_RADIUS_INCH * 1.5F / 12F;

    // The distance between wheels (laterally) in feet. Measure from the centerpoints of the wheels.
    public static final float LATERAL_WHEEL_DISTANCE_INCH = 23.25F / 12F;

    public static final float MAX_FPS_SPEED = 18.0F;

    public static final float ENC_RES = 4096.0F;

    public static final float EPOS_TO_FEET = (WHEEL_DIAMETER_FT * (float) Math.PI) / ENC_RES;

    // 600 = amount of 100 ms in a minute
    public static final float EVEL_TO_RPM = (600.0F / ENC_RES);
    public static final float EVEL_TO_FPS = EVEL_TO_RPM / 60 * (WHEEL_DIAMETER_FT * (float) Math.PI);

    public static final int INIT_TIMEOUT = 10; // When initializing a sensor/whatever, the timeout will be 10 ms
    public static final int LOOP_TIMEOUT = 0; // When doing things in a loop, there won't be a timeout

    @Undefined(safe = true)
    public static final double SHIFT_UP_THRESHOLD = RobotMap.UNDEFINED;
    @Undefined(safe = true)
    public static final double SHIFT_DOWN_THRESHOLD = RobotMap.UNDEFINED;
}
