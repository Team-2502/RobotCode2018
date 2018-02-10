package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Created by 64009334 on 2/9/18.
 */
public class ActiveCommand extends Command
{
    public ActiveCommand()
    {
        requires(Robot.ACTIVE_INTAKE);
    }

    protected void execute()
    {
        Robot.ACTIVE_INTAKE.rotateIntake(OI.JOYSTICK_FUNCTION.getY());
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }
}
