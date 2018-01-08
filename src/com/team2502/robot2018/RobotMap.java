package com.team2502.robot2018;

import com.team2502.ctannotationprocessor.Undefined;

public class RobotMap
{
    /**
     * Value is marked as deprecated to trigger a compile time warning to notify
     * end user that the code may not work unless they properly set the proper id's.
     */
    @Deprecated
    public static final int UNDEFINED = -1;

    private RobotMap() { }

    public static final class Joystick
    {
        public static final int JOYSTICK_DRIVE_LEFT = 1;
        public static final int JOYSTICK_DRIVE_RIGHT = 0;
        public static final int JOYSTICK_FUNCTION = 2;

        private Joystick() { }

        public static final class Button
        {
            @Undefined(safe = true)
            public static final int FORCE_LOW_GEAR = UNDEFINED;
            @Undefined(safe = true)
            public static final int INVERSE_DRIVER_CONTROLS = UNDEFINED;

            private Button() { }
        }
    }

    public static final class Electrical
    {
        private Electrical() { }
    }

    public static final class Motor
    {
        public static final float WHEEL_DIAMETER = 6.0F;
        public static final float POS_TO_FEET = (WHEEL_DIAMETER * (float) Math.PI) / (4096 * 12);
        public static final float VEL_TO_RPM = (600 / 4096);
        public static final float VEL_TO_FPS = VEL_TO_RPM * ((float) Math.PI / 2); // VEL_TO_RPM * (6 * (float) Math.PI / 12)

        public static final int INIT_TIMEOUT = 10; // When initializing a sensor/whatever, the timeout will be 10 ms
        public static final int LOOP_TIMEOUT = 0; // When doing things in a loop, there won't be a timeout

        @Undefined(safe = true)
        public static final double SHIFT_UP_THRESHOLD = UNDEFINED;
        @Undefined(safe = true)
        public static final double SHIFT_DOWN_THRESHOLD = UNDEFINED;


        // TODO: Set proper values.
        public static final int DRIVE_TRAIN_FRONT_LEFT = 8;
        public static final int DRIVE_TRAIN_BACK_LEFT = 7;
        public static final int DRIVE_TRAIN_FRONT_RIGHT = 4;
        public static final int DRIVE_TRAIN_BACK_RIGHT = 12;

        @Undefined(safe = true)
        public static final int CLIMBER_LEFT = UNDEFINED;
        @Undefined(safe = true)
        public static final int CLIMBER_RIGHT = UNDEFINED;

        private Motor() { }
    }

    public class Solenoid
    {
        public static final int TRANSMISSION_SWITCH = 0;

        private Solenoid() {}
    }
}
