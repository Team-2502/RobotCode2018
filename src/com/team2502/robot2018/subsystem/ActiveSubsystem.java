package com.team2502.robot2018.subsystem;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ActiveSubsystem extends Subsystem
{
    public WPI_TalonSRXF leftIntake;
    public WPI_TalonSRXF rightIntake;
    public WPI_TalonSRXF rotateIntake;

    public ActiveSubsystem()
    {
        leftIntake = new WPI_TalonSRXF(RobotMap.Motor.ACTIVE_LEFT);
        rightIntake = new WPI_TalonSRXF(RobotMap.Motor.ACTIVE_RIGHT);
        rotateIntake = new WPI_TalonSRXF(RobotMap.Motor.ACTIVE_ROTATE);
    }

    @Override
    protected void initDefaultCommand()
    {

    }
}
