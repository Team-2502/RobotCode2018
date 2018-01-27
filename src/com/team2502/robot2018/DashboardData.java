package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.CalibrateRobotCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public final class DashboardData
{

    private static List<DashboardUpdater> updaters = new ArrayList<DashboardUpdater>(4);

    private DashboardData() { }

    static void update()
    {
        for(DashboardUpdater subsystem : updaters) { subsystem.updateDashboard(); }
        updateNavX();
    }

    public static void addUpdater(DashboardUpdater subsystem) { updaters.add(subsystem); }

    private static void updateNavX()
    {
        SmartDashboard.putNumber("NavX: Yaw", Robot.NAVX.getYaw());
        SmartDashboard.putNumber("NavX: X Displacement", Robot.NAVX.getDisplacementX());
        SmartDashboard.putNumber("NavX: Y Displacement", Robot.NAVX.getDisplacementY());
        SmartDashboard.putNumber("NavX: Z Displacement", Robot.NAVX.getDisplacementZ());
        SmartDashboard.putData(new CalibrateRobotCommand());

    }

    public static void versioning()
    {
        Class clazz = DashboardData.class;
        String className = clazz.getSimpleName() + ".class";
        String classPath = clazz.getResource(className).toString();
        if(!classPath.startsWith("jar")) { return; }
        String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                              "/META-INF/MANIFEST.MF";
        Manifest manifest;
        String branch = "unknown";
        String commit = "unknown";
        String version = "unknown";
        String time = "unknown";
        String blame = "unknown";
        try
        {
            manifest = new Manifest(new URL(manifestPath).openStream());
            Attributes attr = manifest.getMainAttributes();
            branch = attr.getValue("branch");
            commit = attr.getValue("commit");
            version = attr.getValue("version");
            time = attr.getValue("time");
            blame = attr.getValue("blame");
        }
        catch(Exception ignored) { }

        SmartDashboard.putString("Branch: ", branch);
        SmartDashboard.putString("Commit: ", commit);
        SmartDashboard.putString("Version: ", version);
        SmartDashboard.putString("Time: ", time);
        SmartDashboard.putString("Blame: ", blame);
    }

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
}