package com.team2502.robot2018.data;

import com.team2502.robot2018.DashboardData;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalibrationSendable implements Sendable, DashboardData.DashboardUpdater
{

    private double velocity;

    private String name = "Calibration";
    private String subsystem = "idk";

    private static CalibrationSendable instance = new CalibrationSendable();

    private CalibrationSendable() {}

    public static void init() {}

    public static CalibrationSendable getInstance() {return instance;}

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getSubsystem()
    {
        return subsystem;
    }

    @Override
    public void setSubsystem(String subsystem)
    {
        this.subsystem = subsystem;
    }

    public double getVelocity()
    {
        return velocity;
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.addDoubleProperty("calibration.skid_drive.velocityToTest", ()->velocity,value -> velocity=value);
    }

    @Override
    public void updateDashboard()
    {
        SmartDashboard.putData(this);
    }
}
