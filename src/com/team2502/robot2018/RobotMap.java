package com.team2502.robot2018;

import com.team2502.ctannotationprocessor.Undefined;

/**
 * Exclusively for defining Button ID's, Solenoid ID's, Motor ID's, and other kinds of ID's
 * Stuff like conversion constants DO NOT belong here
 */
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

            public static final int FORCE_LOW_GEAR = 1;
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


        private Solenoid() { }
    }
}
