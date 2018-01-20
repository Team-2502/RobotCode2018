package com.team2502.robot2018.sendables;


import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.PIDCommand

public class SendablePIDTuner implements Sendable, DashboardData.DashboardUpdater
{

    String name = "PIDTuner";
    String subsystem;
    PIDTunable pid_values;


    public SendablePIDTuner(Subsystem subsystem, PIDTunable pid_values)
    {
        this.subsystem = subsystem.getName()+"Tuner";
        this.pid_values = pid_values;
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
        builder.addDoubleProperty("p", pid_values::getkP, pid_values::setkP);
        builder.addDoubleProperty("i", pid_values::getkI, pid_values::setkI);
        builder.addDoubleProperty("d", pid_values::getkD, pid_values::setkD);
        builder.addDoubleProperty("f", pid_values::getkF, pid_values::setkF);
//        builder.addDoubleProperty("setpoint", this::getSetpoint, this::setSetpoint);
//        builder.addBooleanProperty("enabled", this::isEnabled, this::setEnabled);
    }

    @Override
    public void updateDashboard()
    {

    }
}
