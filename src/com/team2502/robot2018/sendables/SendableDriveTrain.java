package com.team2502.robot2018.sendables;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleConsumer;

/**
 * Created by 64009334 on 1/16/18.
 */
public class SendableDriveTrain implements Sendable, DashboardData.DashboardUpdater
{

    private static SendableDriveTrain instance = new SendableDriveTrain();
    DoubleConsumer doNothing = (double value) -> {};
    String name = "EncoderDriveTrain";

    private SendableDriveTrain() {}

    public static void init() {}

    public static SendableDriveTrain getInstance() {return instance;}

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
        builder.setSmartDashboardType("DifferentialDrive");
        builder.addDoubleProperty("Left Motor Speed", () -> Robot.DRIVE_TRAIN.getLeftVel() / Constants.MAX_FPS_SPEED, doNothing);
        builder.addDoubleProperty("Right Motor Speed", () -> Robot.DRIVE_TRAIN.getRightVel() / Constants.MAX_FPS_SPEED, doNothing);
    }

    @Override
    public void updateDashboard()
    {
        SmartDashboard.putData(name, this);
    }
}
