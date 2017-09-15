package com.team2502.robot2017;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class DashboardData
{
    private DashboardData() {}

    static void update()
    {
        updateNavX();
    }

    private static void updateNavX()
    {
        SmartDashboard.putNumber("NavX: Yaw", Robot.NAVX.getYaw());
        SmartDashboard.putNumber("NavX: X Displacement", Robot.NAVX.getDisplacementX());
        SmartDashboard.putNumber("NavX: Y Displacement", Robot.NAVX.getDisplacementY());
        SmartDashboard.putNumber("NavX: Z Displacement", Robot.NAVX.getDisplacementZ());
    }
}