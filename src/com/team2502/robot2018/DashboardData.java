package com.team2502.robot2018;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.List;

public final class DashboardData
{
    /**
     * An interface to allow you to automatically update stuff
     * on the Smart Dashboard.
     */
    public interface DashboardUpdater
    {
        /**
         * Called every tick to update data on the Smart Dashboard.
         */
        void updateDashboard();
    }

    /**
     * 64 + 4 + 16 bytes
     * 64 = 16 * 4 (4 => the size of a pointer)
     * 4 => The size of the internal `size` variable
     * 16 => The JVM internal data for an array
     */
    private static List<DashboardUpdater> updaters = new ArrayList<DashboardUpdater>(16);

    static void update()
    {
        updateNavX();
        for(DashboardUpdater subsystem : updaters) { subsystem.updateDashboard(); }
    }

    static void addUpdater(DashboardUpdater subsystem)
    { updaters.add(subsystem); }

    private static void updateNavX()
    {
        SmartDashboard.putNumber("NavX: Yaw", Robot.NAVX.getYaw());
        SmartDashboard.putNumber("NavX: X Displacement", Robot.NAVX.getDisplacementX());
        SmartDashboard.putNumber("NavX: Y Displacement", Robot.NAVX.getDisplacementY());
        SmartDashboard.putNumber("NavX: Z Displacement", Robot.NAVX.getDisplacementZ());
    }

    private DashboardData() { }
}
