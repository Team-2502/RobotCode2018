package com.team2502.robot2018.sendables;

import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Arrays;

public class SendableDriveStrategyType extends ToggleSendable implements DashboardData.DashboardUpdater
{

    private String name = "DriveStrategyType";
    private String subsystem = "DriveTrainSubsystem";
    private final static SendableDriveStrategyType INSTANCE = new SendableDriveStrategyType();

    public SendableDriveStrategyType()
    {
        super(Arrays.asList(DriveTrainSubsystem.DriveStrategyType.values()), DriveTrainSubsystem.DriveStrategyType.values()[0]);
    }

    public static SendableDriveStrategyType getInstance()
    {
        return INSTANCE;
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
    public void updateDashboard()
    {
        SmartDashboard.putData(name,this);
    }
}
