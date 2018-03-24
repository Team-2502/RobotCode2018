package com.team2502.robot2018.trajectory;

/**
 * An attempt at Trapezoidal Motion Profiling that might work but has never been tested.
 *
 * @deprecated
 */
public class TrapezoidalMotionProfiling
{
    final float vel_max, v_start, v_end, t_total;
    float accel;
    /**
     * accelerate to atTime t_1
     */
    float t_1;

    /**
     * decelerate after atTime t_1
     */
    float t_2;

    boolean emergencyBrake;

    public TrapezoidalMotionProfiling(float accel, float vel_max, float v_start, float v_end, float t_total)
    {
        this.accel = accel;
        this.vel_max = vel_max;
        this.v_start = v_start;
        this.v_end = v_end;
        this.t_total = t_total;
    }

    boolean isEmergencyBreak()
    {
        return emergencyBrake;
    }

    void generate()
    {
        float t_acc = (vel_max - v_start) / accel;
        float t_dec = (vel_max - v_end) / accel;
        float t_max = t_total - t_acc - t_dec;
        if(t_max < 0) // not enough atTime for trapezoidal motion profiling
        {
            if(t_dec > t_total)
            {
                t_1 = 0;
                t_2 = 0;
                emergencyBrake = true;
                accel = -(v_end - v_start) / t_total;
            }
            else //no steady
            {
                // intersection between the lines:
                // y = t*accel + v_start --rising
                // = accel*(t_total - t) + v_end --lowering
                t_1 = (v_end + accel * t_total - v_start) / (2 * accel);
                t_2 = t_1;
            }

        }
        else
        {
            // we can do the whole cycle of accelerate, steady, decelerate
            t_1 = t_acc;
            t_2 = t_acc + t_max;
        }
    }


}
