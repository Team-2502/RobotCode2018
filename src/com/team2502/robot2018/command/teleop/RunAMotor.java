package com.team2502.robot2018.command.teleop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Command;

public class RunAMotor extends Command

{

    WPI_TalonSRXF talon;
    double speed;
    public RunAMotor(WPI_TalonSRXF talon, double speed)
    {
        this.talon = talon;
        this.speed = speed;
    }

    protected void execute()
    {
        talon.set(ControlMode.PercentOutput, speed);
    }
    @Override
    protected boolean isFinished()
    {
        return true;
    }
}
