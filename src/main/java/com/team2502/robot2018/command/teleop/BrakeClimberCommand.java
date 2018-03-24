package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.solenoid.ClimberCarriageBrakeSubsystem;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 * @deprecated Not a mechanical feature yet
 */
public class BrakeClimberCommand extends InstantCommand
{
    public final ClimberCarriageBrakeSubsystem carriageBreak;

    public BrakeClimberCommand()
    {
        requires(Robot.CLIMBER_CARRIAGE_BRAKE);
        carriageBreak = Robot.CLIMBER_CARRIAGE_BRAKE;
    }

    @Override
    protected void execute()
    {
        carriageBreak.toggle();
    }
}
