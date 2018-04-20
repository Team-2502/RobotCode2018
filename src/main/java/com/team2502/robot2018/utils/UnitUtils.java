package com.team2502.robot2018.utils;

import com.team2502.robot2018.Constants;

public class UnitUtils
{

    public static float convert(float value, float unitOne, float unitTwo)
    {
        return unitOne/unitTwo * value;
    }

    public static class Distance
    {
        public static final float FEET = 1;
        public static final float METER = 0.3048F;
    }

    public static class Rotations
    {
        public static final float REVOLUTION = 1;
        public static float ENC_ROTATIONS = Constants.Physical.DriveTrain.WHEEL_REV_TO_ENC_REV_HIGH;
        public static final float RADIANS = MathUtils.TAU;
        public static final float DEGREES = 360;
        public static final float ENC_UNITS = 0.000244140625F; // 1/4096
    }

    public static class Time
    {
        public static final float SECOND = 1;
        public static final float MINUTE = 60;
        public static final float HOUR = 3600;
        public static final float DAY = 86400;
        public static final float WEEK = 604800;
        public static final float  MONTH = 2592000; // 30 days
        public static final float YEAR = 31557600; // 365.25 days
    }
}
