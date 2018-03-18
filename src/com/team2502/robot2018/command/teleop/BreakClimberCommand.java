package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.solenoid.ClimberCarriageBreakSubsystem;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class BreakClimberCommand extends InstantCommand
{
    public final ClimberCarriageBreakSubsystem carriageBreak;

    public BreakClimberCommand()
    {
        requires(Robot.CLIMBER_CARRIAGE_BREAK);
        carriageBreak = Robot.CLIMBER_CARRIAGE_BREAK;
    }

    @Override
    protected void execute()
    {
        carriageBreak.toggle();
    }
}
