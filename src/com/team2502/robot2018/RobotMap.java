package com.team2502.robot2018;

import com.team2502.ctannotationprocessor.Undefined;

import java.util.HashMap;
import java.util.Map;

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
            public static final int INVERSE_DRIVER_CONTROLS = 1;

            /*
             * Eventually, once we have encoders, these buttons will go all the way up/down
             */
            public static final int RAISE_ELEVATOR = 5;
            public static final int LOWER_ELEVATOR = 3;

            /*
             * Then, these buttons will let Driver 2 adjust it manually
             */
            public static final int ADJUST_ELEVATOR_UP = 3;
            public static final int ADJUST_ELEVATOR_DOWN = 3;

            public static final int CLIMBER = 10;

            public static final int SHIFT_GEARBOX_ELEV = 11;


            public static final int OPEN_INTAKE = 1;
            public static final int INTAKE_IN = 6;
            public static final int INTAKE_OUT = 7;

            private Button() { }
        }
    }

    public static final class Electrical
    {
        private Electrical() { }
    }

    public static final class Motor
    {
        // Right side of e-panel, bottom to top
        public static final int DRIVE_TRAIN_FRONT_RIGHT = 1;
        public static final int DRIVE_TRAIN_BACK_RIGHT = 2;
        public static final int DRIVE_TRAIN_FRONT_LEFT = 3;
        public static final int DRIVE_TRAIN_BACK_LEFT = 4;
        public static final int ACTIVE_RIGHT = 5;
        public static final int ACTIVE_ROTATE = 6;

        // Left side of e-panel, top to bottom
        public static final int SPARE_TALON = 7;
        public static final int ACTIVE_LEFT = 8;
        public static final int ELEVATOR_TOP = 9;
        public static final int ELEVATOR_BOTTOM = 10;
        public static final int CLIMBER_TOP = 11;
        public static final int CLIMBER_BOTTOM = 12;

        private Motor() { }
    }

    public static final class Files
    {
        public static String[] NAMES = { "Right Pos", "Left Pos" };
        public static int FILESMADE = 0;
        public static Map<String, Object> FILEMAP = new HashMap<String, Object>();

        private Files() {}
    }

    public class Solenoid
    {
        public static final int TRANSMISSION_SWITCH = 0;
        public static final int CLIMBER_SOLENOID = 1;
        public static final int ACTIVE_GRABBER = 2;

        private Solenoid() { }
    }
}