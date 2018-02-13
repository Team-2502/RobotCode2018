package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.ActiveCommand;
import com.team2502.robot2018.utils.baseoverloads.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import logger.Log;


public class ActiveSubsystem extends Subsystem
{
    public final WPI_TalonSRXF leftIntake;
    public final WPI_TalonSRXF rightIntake;
    public final WPI_TalonSRXF rotateIntake;

    public ActiveSubsystem()
    {
        leftIntake = new WPI_TalonSRXF(RobotMap.Motor.ACTIVE_LEFT);
        rightIntake = new WPI_TalonSRXF(RobotMap.Motor.ACTIVE_RIGHT);
        rotateIntake = new WPI_TalonSRXF(RobotMap.Motor.ACTIVE_ROTATE);

        rotateIntake.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        rotateIntake.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
    }

    public void runIntake(double x)
    {
        leftIntake.set(ControlMode.PercentOutput, x);
        rightIntake.set(ControlMode.PercentOutput, x);
    }

    public void stop()
    { runIntake(0); }

    public void rotateIntake(double x)
    { rotateIntake.set(ControlMode.PercentOutput, x); }

    @Override
    protected void initDefaultCommand()
    { setDefaultCommand(new ActiveCommand()); }
}
