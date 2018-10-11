package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.ActiveRotationCommand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class ActiveIntakeSubsystem extends Subsystem
{
    private final WPI_TalonSRX leftIntake;
    private final WPI_TalonSRX rightIntake;
    private final WPI_TalonSRX rotateIntake;

    public ActiveIntakeSubsystem()
    {
        leftIntake = new WPI_TalonSRX(RobotMap.Motor.ACTIVE_LEFT);
        rightIntake = new WPI_TalonSRX(RobotMap.Motor.ACTIVE_RIGHT);
        rotateIntake = new WPI_TalonSRX(RobotMap.Motor.ACTIVE_ROTATE);

        rotateIntake.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        rotateIntake.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
    }

    public void runIntake(double speed)
    {
        leftIntake.set(ControlMode.PercentOutput, -speed);
        rightIntake.set(ControlMode.PercentOutput, -speed);
    }

    public void stopIntake()
    {
        rotateIntake(0.0D);
        runIntake(0.0D);
    }

    public void stopRotate()
    {
        rotateIntake(0.0D);
    }

    public void stopAll()
    {
        rotateIntake(0.0D);
        runIntake(0.0D);
    }

    public void rotateIntake(double x)
    { rotateIntake.set(ControlMode.PercentOutput, -x); }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new ActiveRotationCommand());
    }
}
