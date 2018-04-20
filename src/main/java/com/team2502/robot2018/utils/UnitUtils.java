package com.team2502.robot2018.utils;

import com.team2502.robot2018.Constants;

public class UnitUtils
{

    /**
     *
     * @param value The amount of the unit you want. If you have 7.4 meters ==> feet this will be 7.4F
     * @param unitOne The unit you are converting FROM. If you have 7.4 meters ==> feet this will be UnitUtils.Distance.METER
     * @param unitTwo The unit you are converting TO. if you have 7.4 meters ==> feet this will be UnitUtils.Distance.FEET
     * @return The value in unitTwo units
     */
    public static float convert(float value, float unitOne, float unitTwo)
    {
        // Use dimensional analysis to prove this equation. Hint: The unit of value is unitOne. The units need to cancel.
        return unitTwo/unitOne * value;
    }

    /**
     * Tangential distance units
     */
    public static class Distance
    {
        public static final float WHEEL_REV_FEET = 4*MathUtils.PI_F; //TODO: is this good to put in the Distance class?
        public static final float FEET = 1;
        public static final float METER = 0.3048F;
    }

    /**
     * Rotational distance units
     */
    public static class Rotations
    {
        public static final float REVOLUTION = 1;
        public static float ENC_ROTATIONS = Constants.Physical.DriveTrain.WHEEL_REV_TO_ENC_REV_HIGH;
        public static final float RADIANS = MathUtils.TAU;
        public static final float DEGREES = 360;
        public static final float ENC_UNITS = 4096F; // 1/4096
    }

    /**
     * Time units
     */
    public static class Time
    {
        public static final float SECOND = 1;
        public static final float ENC_TIME = 10F; // the time provided in encoder velocity... is per 100ms
        public static final float MILLIS = 1000F;
        public static final float NANOS = 1E9F;
        public static final float MINUTE = 1/60F;
        public static final float HOUR = 1/3600F;
        public static final float DAY = 1/86400F;
        public static final float WEEK = 1/604800F;
        public static final float  MONTH = 1/2592000F; // 30 days
        public static final float YEAR = 1/31557600F; // 365.25 days
    }
}
