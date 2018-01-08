package com.team2502.robot2018;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.LinkedList;

public final class DashboardData
{
    public interface Updater
    {
        void update();
    }

    private static LinkedList<Updater> updaters;

    private DashboardData()
    {
    }

    static void update()
    {
        updateNavX();
        for(Updater subsystem : updaters)
        {
            subsystem.update();
        }
    }

    static void addUpdater(Updater subsystem)
    {
        updaters.add(subsystem);
    }

    private static void updateNavX()
    {
        SmartDashboard.putNumber("NavX: Yaw", Robot.NAVX.getYaw());
        SmartDashboard.putNumber("NavX: X Displacement", Robot.NAVX.getDisplacementX());
        SmartDashboard.putNumber("NavX: Y Displacement", Robot.NAVX.getDisplacementY());
        SmartDashboard.putNumber("NavX: Z Displacement", Robot.NAVX.getDisplacementZ());
    }
}
