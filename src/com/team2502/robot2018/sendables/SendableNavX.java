package com.team2502.robot2018.sendables;

import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * There is a gyro widget in Shuffleboard
 * One may only use it if you send a Sendable to the SmartDashboard.
 * While AHRS implements Sendable, the getAngle goes from -180 degrees to 180 degrees. The gyro widget only supports 0 to 360 degrees.
 * This class rectifies that issue.
 */
public class SendableNavX implements Sendable, DashboardData.DashboardUpdater
{
    public static final SendableNavX INSTANCE = new SendableNavX();

    private DoubleSupplier getAngle;
    private DoubleConsumer takeAngle;
    private String name;

    /**
     * This class is a Singleton
     */
    public SendableNavX()
    {
        this.getAngle = () -> 180 + Robot.NAVX.getYaw();
        this.takeAngle = value -> { };
        this.name = "Sendable NavX";
    }

    public SendableNavX(DoubleSupplier getAngle, String name)
    {
        this.getAngle = getAngle;
        this.takeAngle = value -> { };
        this.name = name;
    }

    /**
     * Activate all the static items
     */
    public static void init() { }

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
        return name;
    }

    @Override
    public void setSubsystem(String subsystem)
    {
        this.name = subsystem;
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", getAngle, takeAngle);
    }

    @Override
    public void updateDashboard()
    {
        SmartDashboard.putData(name, this);
    }
}
