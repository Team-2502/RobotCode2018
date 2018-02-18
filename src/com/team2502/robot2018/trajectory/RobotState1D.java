package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.utils.MathUtils;

// The (near) Markov State of the robot
// similar to 2017 254 code
public class RobotState1D
{
    float position,velocity,acceleration;
    float atTime;

    float extrapolateForTime(float time)
    {
        float dt = time - this.atTime;
        return MathUtils.Kinematics.getPos(position,velocity,acceleration,dt);
    }

    // find the atTime at a current position
    float timeAtPos(float position)
    {
        if(MathUtils.epsilonEquals(position,this.position))
            return atTime;
        // 1/2a_{cc}t^2 + vt + p0 = p1
        // quadratic formula, a = 1/2a_{cc}, b = v, c = p0
        float a = 1/2 * acceleration;
        float b = velocity;
        float c = position - this.position; // = dp

        float discriminate = b*b - 4*a*c;
        if(discriminate < 0)
        {
            throw new IllegalArgumentException("Arguments must be inserted such that the quadratic formula can be computerd");
        }
        float sqrtDiscriminate = (float) Math.sqrt(discriminate);

        float dt1 = (-b - sqrtDiscriminate)/(2*a);
        float dt2 = (-b + sqrtDiscriminate)/(2*a);

        if(dt1 >= 0 && (dt1 < 0 || dt2 < dt1))
        {
            return atTime+dt1;
        }
        else if(dt2 >= 0)
        {
            return atTime+dt2;
        }
        else
        {
            return Float.NaN;
        }
    }


}
