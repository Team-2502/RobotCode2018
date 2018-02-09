package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.DriveCommand;
import com.team2502.robot2018.command.teleop.ElevatorCommand;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem
{
    public WPI_TalonSRXF elevatorTop;
    public WPI_TalonSRXF elevatorBottom;

    public ElevatorSubsystem()
    {
        elevatorTop = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_TOP);
        elevatorBottom = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_BOTTOM);
    }

    public void stop()
    {
        elevatorTop.set(ControlMode.PercentOutput, 0.0F);
        elevatorBottom.set(ControlMode.PercentOutput, 0.0F);
    }

    @Override
    protected void initDefaultCommand() { }

    public void drive(double x)
    {
        elevatorTop.set(ControlMode.PercentOutput, x);

// //         because of how the motors are physically wired, even though they should spin in the same direction
// //         this is how it should be
        elevatorBottom.set(ControlMode.PercentOutput, x);
    }
}
