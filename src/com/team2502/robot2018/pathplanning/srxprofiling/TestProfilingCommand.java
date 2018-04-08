package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Notifier;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

public class TestProfilingCommand
{
    public static final Trajectory.Config CONFIG_SETTINGS = new Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_LOW, 10, 7, 14, 28);

    public static void main(String[] args) throws Exception
    {
        Trajectory traj = generate(new Waypoint(0, 0, 0),
                                   new Waypoint(0, 0, Math.PI / 2));

        TankModifier modifier = new TankModifier(traj);

        modifier.modify(20);

        System.out.println("modifier.getLeftTrajectory() = " + modifier.getLeftTrajectory());




    }

    public static Trajectory generate(Waypoint... waypoints) throws Exception
    {
        return Pathfinder.generate(waypoints, CONFIG_SETTINGS);
    }
}
