package com.team2502.robot2018.utils.srxmotionprofiling;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.utils.InterpolationMap;


import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by 64009334 on 3/2/18.
 */
public class Profile
{
    final List<Map<Point, Double>> waypoints;
    final int numWaypoints;

    /**
     * Given a double-array-array, make a motion profile
     * @param waypoints A double double array of waypoints as the provided Talon SRX Motion Profile Generator might generate
     */
    public Profile(double[][] waypoints)
    {
        this.waypoints = new ArrayList<>();

        for(double[] point : waypoints)
        {
            Map<Point, Double> new_point = new HashMap<>();
            new_point.put(Point.POS, point[0]);
            new_point.put(Point.VEL, point[1]);
            new_point.put(Point.DT, point[2]);

            this.waypoints.add(new_point);
        }

        numWaypoints = waypoints.length;
    }

    /**
     * Make a new profile from a trapezoid and the interval between waypoints
     * @param velocity_trapezoid An interpolating map representing the trapezoid function for velocity, where the x-axis represents ms.
     * @param dt Interval (in ms) between waypoints
     * @throws InvalidParameterException Only thrown if your velocity_trapezoid is not set up right
     */
    public Profile(InterpolationMap velocity_trapezoid, int dt) throws InvalidParameterException
    {
        waypoints = new ArrayList<>();

        Set<Double> keySet = velocity_trapezoid.keySet();

        if(keySet.size() < 3)
        {
            throw new InvalidParameterException("The interpolation must have at least 3 defined points for the velocity curve.");
        }

        double finalTime = 0;

        for(double t : keySet)
        {
            finalTime = Math.max(t, finalTime);
        }

        if(velocity_trapezoid.get(finalTime) != 0)
        {
            throw new InvalidParameterException("Your motion profile does not slow down after speeding up; remember, it's a velocity profile that you're giving us.");
        }

        this.numWaypoints = (int) (finalTime / dt + 0.5D);

        for(int i = 0; i < numWaypoints; i++)
        {
            double timeElapsed = (double) dt * i;
            Map<Point, Double> point = new HashMap<>();

            point.put(Point.POS, velocity_trapezoid.integrate(0, timeElapsed));
            point.put(Point.VEL, velocity_trapezoid.get(timeElapsed));
            point.put(Point.DT, (double) dt);

            waypoints.add(point);
        }

    }

    public void streamPoints(WPI_TalonSRX... talons)
    {

    }
}
