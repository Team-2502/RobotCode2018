package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.ActiveIntakeSubsystem;
import edu.wpi.first.wpilibj.command.Command;

/**
 * During teleop, rotate intake based on function joystick y-axis
 * @see ActiveIntakeSubsystem#initDefaultCommand()
 */
public class ActiveRotationCommand extends Command
{
    public ActiveRotationCommand()
    {
        requires(Robot.ACTIVE_INTAKE);
    }

    @Override
    protected void execute()
    {
        // Must be negative
        Robot.ACTIVE_INTAKE.rotateIntake(-OI.JOYSTICK_FUNCTION.getY());
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.ACTIVE_INTAKE.stopRotate();
    }
}
