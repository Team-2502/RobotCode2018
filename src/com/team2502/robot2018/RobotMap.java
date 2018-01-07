package com.team2502.robot2018;

import com.team2502.undefinedprocessor.Undefined;

public class RobotMap
{
    /**
     * Value is marked as deprecated to trigger a compile time warning to notify
     * end user that the code may not work unless they properly set the proper id's.
     */
    @Deprecated
    public static final int UNDEFINED = -1;

    private RobotMap() {}

    public static final class Joystick
    {
        public static final int JOYSTICK_DRIVE_LEFT = 1;
        public static final int JOYSTICK_DRIVE_RIGHT = 0;
        public static final int JOYSTICK_FUNCTION = 2;

        private Joystick() {}

        public static final class Button
        {
            private Button() {}
        }
    }

    public static final class Electrical
    {
        private Electrical() {}
    }

    public static final class Motor
    {
        private Motor() {}

        // TODO: Set proper values.
        @Undefined
        public static final int DRIVE_TRAIN_FRONT_LEFT = UNDEFINED;
        @Undefined
        public static final int DRIVE_TRAIN_BACK_LEFT = UNDEFINED;
        @Undefined
        public static final int DRIVE_TRAIN_FRONT_RIGHT = UNDEFINED;
        @Undefined
        public static final int DRIVE_TRAIN_BACK_RIGHT = UNDEFINED;

        @Undefined(safe = true)
        public static final int CLIMBER_LEFT = UNDEFINED;
        @Undefined(safe = true)
        public static final int CLIMBER_RIGHT = UNDEFINED;
    }
}