package com.team2502.robot2018;

public class RobotMap
{
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
            public static final int FORCE_LOW_GEAR = UNDEFINED;

            private Button() {}
        }
    }

    public static final class Electrical
    {
        private Electrical() {}
    }

    public static final class Motor
    {
        public static final double SHIFT_UP_THRESHOLD = UNDEFINED;
        public static final double SHIFT_DOWN_THRESHOLD = UNDEFINED;

        private Motor() {}

        // TODO: Set proper values.
        public static final int DRIVE_TRAIN_FRONT_LEFT = 12;
        public static final int DRIVE_TRAIN_BACK_LEFT_ENC = 4;
        public static final int DRIVE_TRAIN_FRONT_RIGHT = 8;
        public static final int DRIVE_TRAIN_BACK_RIGHT_ENC = 7;

        public static final int CLIMBER_LEFT = UNDEFINED;
        public static final int CLIMBER_RIGHT = UNDEFINED;
    }

    public class Solenoid {
        private Solenoid() {}

        public static final int TRANSMISSION_SWITCH = UNDEFINED;
    }
}