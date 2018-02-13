package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.solenoid.ActiveIntakeSolenoid;
import edu.wpi.first.wpilibj.command.Command;

public class ActiveIntakeCommand extends Command
{
    double x;
    public ActiveIntakeCommand(double speed)
    {
        requires(Robot.ACTIVE_INTAKE);
        x = speed;
    }

    protected void execute()
    {
        Robot.ACTIVE_INTAKE.runIntake(x);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
