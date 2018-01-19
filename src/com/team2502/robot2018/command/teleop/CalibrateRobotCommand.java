package com.team2502.robot2018.command.teleop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.data.CalibrationSendable;
import edu.wpi.first.wpilibj.command.Command;

public class CalibrateRobotCommand extends Command
{

    double velocity = 0;
    float initYaw = 0;
    private boolean finished = false;
    public static final float ROT_UNTIL_STOP = 1080F;

    @Override
    protected void initialize()
    {
        velocity = CalibrationSendable.getInstance().getVelocity();

        // We are blocking the right wheels
        Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Disabled, 0.0F);
        Robot.DRIVE_TRAIN.rightFrontTalon.set(ControlMode.Disabled, 0.0F);
        initYaw = Robot.NAVX.getYaw();
    }

    @Override
    protected void execute()
    {
        if(Robot.NAVX.getYaw() - initYaw < ROT_UNTIL_STOP)
        {
            Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Velocity, 4096F * velocity);
            Robot.DRIVE_TRAIN.rightFrontTalon.set(ControlMode.Velocity, 4096F * velocity);
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
