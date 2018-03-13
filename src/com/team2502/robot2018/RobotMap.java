package com.team2502.robot2018;

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

    /**
     * Define Joystick ID's and button ID's
     */
    public static final class Joystick
    {
        public static final int JOYSTICK_DRIVE_LEFT = 1;
        public static final int JOYSTICK_DRIVE_RIGHT = 0;
        public static final int JOYSTICK_FUNCTION = 2;

        private Joystick() { }

        /**
         * Define Button ID's, which should be used in OI.java
         */
        public static final class Button
        {
            /**
             * Used to shift gearbox on Left drive stick
             */
            public static final int TOGGLE_TRANSMISSION = 1;

            // Trigger
            public static final int OPEN_INTAKE = 1;

            // Side button for thumb
            public static final int INTAKE_OUT_FAST = 2;

            // On function joystick face
            /*
             * Eventually, once we have encoders, these buttons will go all the way up/down
             */
            public static final int RAISE_ELEVATOR = 5;
            public static final int LOWER_ELEVATOR = 3;

            public static final int INTAKE_OUT_SLOW = 4;
            public static final int INTAKE_IN = 6;

            /*
             * Then, these buttons will let Driver 2 adjust it manually
             */
            public static final int ADJUST_ELEVATOR_UP = 3;
            public static final int ADJUST_ELEVATOR_DOWN = 3;

            // On lower platform
            public static final int CLIMBER = 10;

            public static final int SHIFT_GEARBOX_ELEV = 11;

            public static final int DEPLOY_BUTTERFLY = 7;

            /*
             * Debug buttons on Right drive stick
             */
            public static final int RUN_DEBUG_TESTS = 9;
            public static final int CALIBRATE_ELEV_ENCODER = 11;

            private Button() { }
        }
    }

    /**
     * Define Talon ID's
     */
    public static final class Motor
    {
        /*                            Name - Talon ID - Log ID
         *
         *            Left Side of PDP           |            Right Side of PDP
         *                                       |
         * Spare Talon ----------------- 7 - 10  |  Active Rotate SEN ----------- 6 -- 5
         * Active Left ----------------- 8 - 11  |  Active Right ---------------- 5 -- 4
         * Elevator Top SEN ------------ 9 - 12  |  Drive Train Back Left ------- 4 -- 3
         * Elevator Bottom ENC -------- 10 - 13  |  Drive Train Front Left ENC -- 3 -- 2
         * Climber Top ---------------- 11 - 14  |  Drive Train Back Right ------ 2 -- 1
         * Climber Bottom ------------- 12 - 15  |  Drive Train Front Right ENC - 1 -- 0
         */

        public static final int DRIVE_TRAIN_FRONT_RIGHT = 1;
        public static final int DRIVE_TRAIN_BACK_RIGHT = 2;
        public static final int DRIVE_TRAIN_FRONT_LEFT = 3;
        public static final int DRIVE_TRAIN_BACK_LEFT = 4;
        public static final int ACTIVE_RIGHT = 5;
        public static final int ACTIVE_ROTATE = 6;
        public static final int ACTIVE_LEFT = 8;
        public static final int ELEVATOR_TOP = 9;
        public static final int ELEVATOR_BOTTOM = 10;
        public static final int CLIMBER_TOP = 11;
        public static final int CLIMBER_BOTTOM = 12;

        private Motor() { }
    }

    /**
     * Define file names for logging purposes
     */
    public static final class Files
    {
        public static String[] NAMES = { "Right Pos", "Left Pos" };
        public static int FILESMADE = 0;
        public static Map<String, Object> FILEMAP = new HashMap<String, Object>();

        private Files() { }
    }

    /**
     * Define Solenoid ID's
     */
    public class Solenoid
    {
        /*
         * Transmission ------ 0
         * Climber Solenoid -- 1
         * Active Grabber ---- 2
         * Butterfly Release - 3
         */

        public static final int TRANSMISSION_SWITCH = 0;
        public static final int CLIMBER_SOLENOID = 1;
        public static final int ACTIVE_GRABBER = 2;
        public static final int BUTTERFLY_SOLENOID = 3;

        private Solenoid() { }
    }
}