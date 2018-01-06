package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.ClimberSubsystem;
import edu.wpi.first.wpilibj.command.Command;

public class RunClimberCommand extends Command
{
    public final ClimberSubsystem climber;

    public RunClimberCommand()
    {
        requires(Robot.CLIMBER);
        climber = Robot.CLIMBER;
    }

    @Override
    protected void execute()
    { climber.climb(); }

    @Override
    protected boolean isFinished()
    { return false; }

    @Override
    protected void end()
    { climber.stop(); }
}
