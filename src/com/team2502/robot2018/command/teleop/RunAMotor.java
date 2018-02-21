package com.team2502.robot2018.command.teleop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This class has been deprecated in order to discourage use of it.
 * <p>
 * Only use this class when doing quick fixes at tournaments.
 */
@Deprecated
public class RunAMotor extends Command
{
    WPI_TalonSRX talon;
    double speed;

    public RunAMotor(WPI_TalonSRX talon, double speed)
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

    protected void end()
    {
        talon.set(ControlMode.PercentOutput, 0.0F);
    }
}
