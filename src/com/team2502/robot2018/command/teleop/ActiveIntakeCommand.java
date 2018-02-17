package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class ActiveIntakeCommand extends Command
{
    private double _speed;

    public ActiveIntakeCommand(double speed)
    {
        requires(Robot.ACTIVE_INTAKE);
        _speed = speed;
    }

    @Override
    protected void execute()
    {
        // Continue to watch the rotation axis on joystick...
        Robot.ACTIVE_INTAKE.rotateIntake(OI.JOYSTICK_FUNCTION.getY());

        // ...but also rotate the intake
        Robot.ACTIVE_INTAKE.runIntake(_speed);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.ACTIVE_INTAKE.stopIntake();
    }
}
