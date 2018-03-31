package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.CalibrateRobotCommand;
import com.team2502.robot2018.trajectory.record.connectDB;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.List;

/**
 * A class with mostly static members to manage putting things onto the dashboard
 */
public final class DashboardData
{

    /**
     * A list of subsystems (which extend the functional interface DashboardUpdater)
     */
    private static List<DashboardUpdater> updaters = new ArrayList<>(4);

    private DashboardData() { }

    /**
     * Run each updater to update the dashboard
     */
    static void update()
    {
        for(DashboardUpdater subsystem : updaters) { subsystem.updateDashboard(); }
        updateNavX();
        ppRecord();
    }

    /**
     * Add an updater
     *
     * @param subsystem An updater (which is usually a subsystem) that will continually put stuff on the smartdashboard
     */
    public static void addUpdater(DashboardUpdater subsystem) { updaters.add(subsystem); }

    /**
     * Update the heading data for the NavX, because AHRS does not extend our functional interface.
     */
    private static void updateNavX()
    {
        SmartDashboard.putNumber("NavX: Yaw", Robot.NAVX.getYaw());
        SmartDashboard.putNumber("NavX: X Displacement", Robot.NAVX.getDisplacementX());
        SmartDashboard.putNumber("NavX: Y Displacement", Robot.NAVX.getDisplacementY());
        SmartDashboard.putNumber("NavX: Z Displacement", Robot.NAVX.getDisplacementZ());
        SmartDashboard.putData(new CalibrateRobotCommand());

    }

    /**
     * Dashboard widget used to enable and disable Pure Pursuit Record function.
     */
    private static void ppRecord(){
        connectDB.setEnabled(SmartDashboard.getBoolean("", false));
        SmartDashboard.putBoolean("", connectDB.isStorage());
    }

    /**
     * An interface to allow you to automatically update stuff on the Smart Dashboard.
     */
    @FunctionalInterface
    public interface DashboardUpdater
    {
        /**
         * Called every tick to update data on the Smart Dashboard.
         */
        void updateDashboard();
    }
}