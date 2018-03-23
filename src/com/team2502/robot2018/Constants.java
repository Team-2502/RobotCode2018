package com.team2502.robot2018;

import com.team2502.ctannotationprocessor.Undefined;
import com.team2502.robot2018.trajectory.Lookahead;
import com.team2502.robot2018.utils.InterpolationMap;

/**
 * Constant variables generally pertaining to Pure Pursuit and encoders
 * <br>
 * Notes
 * <ul>
 * <li>E (EVEL, ENC_RES, EPOS) is special encoder units</li>
 * <li>The placement of the encoder means that its</li>
 * </ul>
 */
public class Constants
{
    /* Tweak */
    public static final float INTAKE_SPEED_PERCENT_LIMIT = 0.1F;
    public static final float MAX_ROT_DEG_PER_SEC = 30;
    public static final int INIT_TIMEOUT = 10; // When initializing a sensor/whatever, the timeout will be 10 ms
    public static final int LOOP_TIMEOUT = 0; // When doing things in a loop, there won't be a timeout

    /**
     * Variables that pertain to Pure Pursuit, such as lookahead and max speed for PP
     */
    public static class PurePursuit
    {


        /**
         * How many ft you need to be away from the end of a segment to complete it
         * @deprecated can be unreliable as if you do not get within that tolerance, you are screwed.
         */
        public static final float DISTANCE_COMPLETE_SEGMENT_TOLERANCE = 0.16F; // .32ft is max distance robot should cover in 20ms interval at 16fps ==> .16F

        // The lookahead distance (feet) for Pure Pursuit
        public static final float LOOKAHEAD_MIN_DISTANCE_FT = 2F;
        public static final float LOOKAHEAD_MAX_DISTANCE_FT = 5.5F;
        public static final float LOOKAHEAD_MIN_SPEED_FPS = 1F;
        public static final float LOOKAHEAD_MAX_SPEED_FPS = 10F;
        public static final Lookahead LOOKAHEAD = new Lookahead(LOOKAHEAD_MIN_DISTANCE_FT, LOOKAHEAD_MAX_DISTANCE_FT,
                                                                LOOKAHEAD_MIN_SPEED_FPS, LOOKAHEAD_MAX_SPEED_FPS);
        public static final float STOP_DIST_TOLERANCE_FT = 1.5F; //TODO: completely remove tolerance
        // half of the width of the wheel that is in contact with the ground
        public static final float WHEEL_ROLLING_RADIUS_INCH = 1.5F;
        public static final float WHEEL_ROLLING_RADIUS_FT = WHEEL_ROLLING_RADIUS_INCH * 1.5F / 12F;
        // The distance between wheels (laterally) in feet. Measure from the centerpoints of the wheels.
        public static final float LATERAL_WHEEL_DISTANCE_FT = 23.25F / 12F *5F; //* 10F;

        private PurePursuit() { }
    }

    /**
     * Conversion constants for Encoders and FPS/RPM as well as robot details
     */
    public static class Physical
    {
        public static InterpolationMap ACCELERATION_FOR_ELEVATOR_HEIGHT;

        private Physical() { }

        /**
         * Contains encoder conversion constants and details about the drivetrain gitwheels
         */
        public static class DriveTrain
        {

            public static final float WHEEL_DIAMETER_INCH = 6F; // 3.6944444443F;

            public static final float WHEEL_DIAMETER_FT = WHEEL_DIAMETER_INCH / 12F;
            public static final float WHEEL_REV_TO_ENC_REV_LOW = 4.285F;
            public static final float WHEEL_REV_TO_ENC_REV_HIGH = 2.083F;
            public static final float MAX_FPS_SPEED = 18.0F;

            // For autoshifting, which is unimplemented on Daedalus because it is not needed.
            @Undefined(safe = true)
            public static final double SHIFT_UP_THRESHOLD = RobotMap.UNDEFINED;
            @Undefined(safe = true)
            public static final double SHIFT_DOWN_THRESHOLD = RobotMap.UNDEFINED;

            /* Drivetrain */
            public static final float EPOS_TO_FEET_DT = (WHEEL_DIAMETER_FT * (float) Math.PI) / Encoder.ENC_RES;
            public static final float FEET_TO_EPOS_DT = 1 / EPOS_TO_FEET_DT;
            public static final float RPM_TO_FPS_DT = (WHEEL_DIAMETER_FT * (float) Math.PI) / 60F;
            //TODO: change bad name
            public static final float FAKE_EVEL_TO_FPS_DT = Encoder.EVEL_TO_RPM * RPM_TO_FPS_DT;
            public static final float FPS_TO_RPM_DT = 60F / (WHEEL_DIAMETER_FT * (float) Math.PI);
            public static final float RPM_TO_EVEL_DT = Encoder.ENC_RES / 600F;
            public static final float FPS_TO_EVEL_DT = FPS_TO_RPM_DT * RPM_TO_EVEL_DT;

            private DriveTrain() { }
        }

        /**
         * Contains encoder conversion constants and details about the elevator
         */
        public static class Elevator
        {
            public static final double ELEV_SHAFT_DIAMETER_IN = 1;
            public static final double ELEV_SHAFT_DIAMETER_FT = ELEV_SHAFT_DIAMETER_IN / 12D;
            public static final double EPOS_TO_FEET_ELEV = (ELEV_SHAFT_DIAMETER_FT * (float) Math.PI) / Encoder.ENC_RES;

            public static final double RPM_TO_FPS_ELEV = (ELEV_SHAFT_DIAMETER_FT * (float) Math.PI) / 60F;

            // 600 = amount of 100 ms in a minute
            public static final double EVEL_TO_RPM_ELEV = (600.0F / Encoder.ENC_RES);
            public static final double EVEL_TO_FPS_ELEV = EVEL_TO_RPM_ELEV * RPM_TO_FPS_ELEV;
            public static final double FEET_TO_EPOS_ELEV = 1 / EPOS_TO_FEET_ELEV;
            public static final double FPS_TO_RPM_ELEV = 60F / (DriveTrain.WHEEL_DIAMETER_FT * (float) Math.PI);
            public static final double RPM_TO_EVEL_ELEV = Encoder.ENC_RES / 600F;
            public static final double FPS_TO_EVEL_ELEV = FPS_TO_RPM_ELEV * RPM_TO_EVEL_ELEV;

            /**
             * How high the elevator must be in order to put a cube in the switch
             */
            public static final float SWITCH_ELEV_HEIGHT_FT = 2F;
            /**
             * How high the elevator must be in order to put a cube in the scale
             */
            public static final float SCALE_ELEV_HEIGHT_FT = 5.75F;
            public static final float ELEVATOR_SPEED_PERCENT_LIMIT = 0.6F;

            /**
             * When using motion magic, where should the top of the pyramid be?
             * <br>
             * In feet per second
             */
            public static final double CRUISE_VELOCITY_FPS = 600D;

            /**
             * When using motion magic, how fast can the elevator accelerate / decelerate?
             * <br>
             * In feet per second per second
             */
            public static final double MAX_ACCEL_FPS2 = 1200;


            private Elevator() { }
        }

        public static class Encoder
        {


            public static final float ENC_RES = 4096.0F;
            // 600 = amount of 100 ms in a minute
            public static final float EVEL_TO_RPM = (600.0F / ENC_RES);
            public static final float RAW_UNIT_PER_ROT = 4096F;

            private Encoder() { }
        }
    }
}
