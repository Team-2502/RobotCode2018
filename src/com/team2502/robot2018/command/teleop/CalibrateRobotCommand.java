package com.team2502.robot2018.command.teleop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalibrateRobotCommand extends Command
{

    public static final float ROT_UNTIL_STOP = 1080F;
    double velocity = 0;
    float initAngle = 0;

    @Override
    protected void initialize()
    {
        System.out.println("initialized");
        // We are blocking the right wheels
//        Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Disabled, 0.0F);
//        Robot.DRIVE_TRAIN.rightFrontTalon.set(ControlMode.Disabled, 0.0F);

        initAngle = (float) Robot.NAVX.getAngle();

        // One of the left motors lacks an encoder
        Robot.DRIVE_TRAIN.leftFrontTalon.follow(Robot.DRIVE_TRAIN.leftRearTalonEnc);
    }

    @Override
    protected void execute()
    {
        if(SmartDashboard.getBoolean("calibration_enabled", false))
        {
            System.out.println("enabled!");
            velocity = Robot.CAL_VELOCITY;
            SmartDashboard.putNumber("enc_velocity", Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0));
            Robot.DRIVE_TRAIN.leftRearTalonEnc.set(ControlMode.Velocity, velocity); // this
            Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Velocity, -velocity); // this
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setTeleopSettings();
    }

}
