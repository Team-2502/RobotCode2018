package com.team2502.robot2018.command.teleop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class CalibrateRobotCommand extends Command
{

    public static final float ROT_UNTIL_STOP = 1080F;
    double velocity = 0;
    float initAngle = 0;
    private boolean finished = false;

    @Override
    protected void initialize()
    {
        velocity = Robot.CAL_VELOCITY;

        // We are blocking the right wheels
        Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Disabled, 0.0F);
        Robot.DRIVE_TRAIN.rightFrontTalon.set(ControlMode.Disabled, 0.0F);
        initAngle = (float) Robot.NAVX.getAngle();
    }

    @Override
    protected void execute()
    {
        if(Robot.NAVX.getAngle() - initAngle < ROT_UNTIL_STOP)
        {

            // We want to run robot wheels at x ft/s.
            // We have x ft /s = 12 in /s =
            Robot.DRIVE_TRAIN.leftRearTalonEnc.set(ControlMode.Velocity, -Constants.FPS_TO_EVEL*velocity);
            Robot.DRIVE_TRAIN.leftFrontTalon.set(ControlMode.Velocity, -Constants.FPS_TO_EVEL*velocity);
        }
        else
        {
            finished = true;
        }
    }

    @Override
    protected boolean isFinished()
    {
        return finished;
    }

}
