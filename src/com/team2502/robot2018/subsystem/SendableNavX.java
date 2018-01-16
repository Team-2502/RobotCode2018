package com.team2502.robot2018.subsystem;

import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * Created by 64009334 on 1/16/18.
 */
public class SendableNavX implements Sendable, DashboardData.DashboardUpdater
{
    private static SendableNavX instance = new SendableNavX();

    DoubleSupplier getAngle = () -> 180 + Robot.NAVX.getAngle();
    DoubleConsumer takeAngle = (double value) -> {};

    private SendableNavX() {}

    public static void init()
    {

    }

    public static SendableNavX getInstance() { return instance; }

    public String name = "Sendibble NavX";

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
        SmartDashboard.putData(name,this);
        System.out.println("Angle: " + getAngle.getAsDouble());
    }
}
