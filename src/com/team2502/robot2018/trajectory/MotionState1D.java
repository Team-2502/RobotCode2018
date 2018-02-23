package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.utils.MathUtils;

/**
 * The (near) Markov State of the robot in 1D space
 * similar to 2017 254 code
 */
public class MotionState1D
{
    private final float position, velocity, acceleration;
    private final float time;

    public MotionState1D(float position, float velocity, float acceleration, float time)
    {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.time = time;
    }

    /**
     * Extrapolate what the position will be at a certain time. The inverse of {@link #extrapForTimeAtPos(float)}.
     *
     * @param time
     * @return
     */
    MotionState1D extrapAtTime(float time)
    {
        float dt = time - this.time;

        return new MotionState1D(MathUtils.Kinematics.getPos(position, velocity, acceleration, dt), velocity + acceleration * dt, acceleration, time);
    }

    public float getPosition()
    {
        return position;
    }

    public float getVelocity()
    {
        return velocity;
    }

    public float getAcceleration()
    {
        return acceleration;
    }

    public float getTime()
    {
        return time;
    }

    /**
     * Extrapolate what the time will be at a certain position. The near-inverse of {@link #extrapAtTime(float)}.
     *
     * @param position
     * @return
     */
    float extrapForTimeAtPos(float position)
    {
        if(MathUtils.epsilonEquals(position, this.position))
        { return time; }
        // 1/2a_{cc}t^2 + vt + p0 = p1
        // quadratic formula, a = 1/2a_{cc}, b = v, c = p0
        float a = 1 / 2 * acceleration;
        float b = velocity;
        float c = position - this.position; // = dp

        float discriminate = b * b - 4 * a * c;
        if(discriminate < 0)
        {
            throw new IllegalArgumentException("Arguments must be inserted such that the quadratic formula can be computerd");
        }
        float sqrtDiscriminate = (float) Math.sqrt(discriminate);

        float dt1 = (-b - sqrtDiscriminate) / (2 * a);
        float dt2 = (-b + sqrtDiscriminate) / (2 * a);

        if(dt1 >= 0 && (dt1 < 0 || dt2 < dt1))
        {
            return time + dt1;
        }
        else if(dt2 >= 0)
        {
            return time + dt2;
        }
        else
        {
            return Float.NaN;
        }


    }


}
