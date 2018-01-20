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

        // One of the left motors lacks an enocder
        Robot.DRIVE_TRAIN.leftFrontTalon.follow(Robot.DRIVE_TRAIN.rightFrontTalon);

    }

    @Override
    protected void execute()
    {
            Robot.DRIVE_TRAIN.leftRearTalonEnc.set(ControlMode.Velocity, velocity / Constants.VEL_TO_RPM);

    }

    @Override
    protected boolean isFinished()
    {
        return Robot.NAVX.getYaw() - initAngle >= ROT_UNTIL_STOP;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setTeleopSettings();
    }

}
