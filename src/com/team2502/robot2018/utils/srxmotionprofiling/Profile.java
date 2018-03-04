package com.team2502.robot2018.utils.srxmotionprofiling;

import com.team2502.robot2018.utils.InterpolationMap;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by 64009334 on 3/2/18.
 */
public class Profile
{
    final double[][] waypoints;
    final int numWaypoints;

    public Profile(double[][] waypoints)
    {
        this.waypoints = waypoints;
        numWaypoints = waypoints.length;
    }

    /**
     * Make a new profile from a trapezoid and the interval between waypoints
     * @param velocity_trapezoid An interpolating map representing the trapezoid function for velocity, where the x-axis represents ms.
     * @param dt Interval (in ms) between waypoints
     */
    public Profile(InterpolationMap velocity_trapezoid, int dt) throws InvalidParameterException
    {

        Set<Double> keySet = velocity_trapezoid.keySet();
        Collection<Double> valueSet = velocity_trapezoid.values();

        if(keySet.size() < 3)
        {
            throw new InvalidParameterException("The interpolation must have at least 3 defined points for the velocity curve.");
        }

        double lastTime = 0;

        for(double t : keySet)
        {
            lastTime = Math.max(t, lastTime);
        }

        this.numWaypoints = (int) lastTime / dt;

        waypoints = new double[numWaypoints][3];

        for(int i = 0; i < waypoints.length; i++)
        {
            waypoints[i][0] = 0D;
            waypoints[i][1] = velocity_trapezoid.get((double) dt * i);
            waypoints[i][2] = dt;
        }

    }
}
