package com.team2502.robot2018.sendables;

import com.team2502.robot2018.DashboardData;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SendablePIDTuner implements Sendable, DashboardData.DashboardUpdater
{
    private String name;
    private String subsystem;
    private PIDTunable pidValues;

    public SendablePIDTuner(Subsystem subsystem, PIDTunable pidValues)
    {
        this.name = "PIDTuner";
        this.subsystem = subsystem.getName() + "Tuner";
        this.pidValues = pidValues;
    }

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

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.setSmartDashboardType("PIDController");
        builder.addDoubleProperty("p", pidValues::getkP, pidValues::setkP);
        builder.addDoubleProperty("i", pidValues::getkI, pidValues::setkI);
        builder.addDoubleProperty("d", pidValues::getkD, pidValues::setkD);
        builder.addDoubleProperty("f", pidValues::getkF, pidValues::setkF);
    }

    @Override
    public void updateDashboard()
    {
        // TODO: should this be name or subsystem? The subsystem will allow us to differentiate it.
        SmartDashboard.putData(subsystem, this);
    }
}
