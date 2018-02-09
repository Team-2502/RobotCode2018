package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem
{
    public WPI_TalonSRXF elevatorTop;
    public WPI_TalonSRXF elevatorBottom;
//    public WPI_TalonSRXF elevator3;
//    public WPI_TalonSRXF elevator4;

    public ElevatorSubsystem()
    {
        elevatorTop = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_TOP);
        elevatorBottom = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_BOTTOM);

    }

    public void run(double x)
    {
        elevatorBottom.set(ControlMode.PercentOutput, -x);
        elevatorTop.set(ControlMode.PercentOutput, x
                       );
    }

    @Override
    protected void initDefaultCommand()
    {

    }
}
