package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.ActiveIntakeSubsystem;
import edu.wpi.first.wpilibj.command.Command;

/**
 * During teleop, simultaneously rotate the intake and run it at a speed
 *
 */
public class ActiveIntakeCommand extends Command
{
    private double _speed;


    /**
     * @param speed Percent voltage to run intake at
     */
    public ActiveIntakeCommand(double speed)
    {
        requires(Robot.ACTIVE_INTAKE);
        _speed = speed;
    }

    @Override
    protected void execute()
    {
        // Continue to watch the rotation axis on joystick...
        // Must be negative joystick value to be correct on Comp Bot
        // Workaround for requires()
        Robot.ACTIVE_INTAKE.rotateIntake(-OI.JOYSTICK_FUNCTION.getY());

        // ...but also run the intake at a speed
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
