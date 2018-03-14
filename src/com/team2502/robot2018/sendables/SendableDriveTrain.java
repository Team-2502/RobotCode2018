package com.team2502.robot2018.sendables;

import static com.team2502.robot2018.Constants.Physical.DriveTrain;
import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.function.DoubleConsumer;

/**
 * Like the DifferentialDrive widget, but instead sends speed in FPS.
 */
public class SendableDriveTrain implements Sendable, DashboardData.DashboardUpdater
{
    public static final SendableDriveTrain INSTANCE = new SendableDriveTrain();

    /**
     * Does nothing
     */
    private DoubleConsumer doNothing;

    /**
     * Name of drivetrain
     */
    private String name;

    /**
     * Make a new instance of our drive train
     * <br>
     * This is a singleton because you should have only 1 drivetrain on your robot
     * If there are more, call 911.
     */
    private SendableDriveTrain()
    {
        this.doNothing = value -> {};
        this.name = "EncoderDriveTrain";
    }

    /**
     * Initializes {@link SendableDriveTrain#INSTANCE} because of how Java loads static classes
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
        builder.setSmartDashboardType("DifferentialDrive");
        builder.addDoubleProperty("Left Motor Speed", () -> Robot.DRIVE_TRAIN.getLeftVel() / DriveTrain.MAX_FPS_SPEED, doNothing);
        builder.addDoubleProperty("Right Motor Speed", () -> Robot.DRIVE_TRAIN.getRightVel() / DriveTrain.MAX_FPS_SPEED, doNothing);
    }

    @Override
    public void updateDashboard()
    {
        SmartDashboard.putData(name, this);
    }
}
