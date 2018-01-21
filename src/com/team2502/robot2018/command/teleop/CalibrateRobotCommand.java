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
    private boolean finished = false;

    @Override
    protected void initialize()
    {
//        velocity = Robot.CAL_VELOCITY;

        // We are blocking the right wheels
        Robot.DRIVE_TRAIN.leftRearTalonEnc.set(ControlMode.Disabled, 0.0F);
        Robot.DRIVE_TRAIN.leftFrontTalon.set(ControlMode.Disabled, 0.0F);

        initAngle = (float) Robot.NAVX.getAngle();

        // One of the left motors lacks an encoder
        Robot.DRIVE_TRAIN.rightFrontTalon.follow(Robot.DRIVE_TRAIN.rightRearTalonEnc);

//        Robot.DRIVE_TRAIN.rightFrontTalon.setInverted(true);
//        Robot.DRIVE_TRAIN.leftFrontTalon.setInverted(true); //
//        Robot.DRIVE_TRAIN.leftRearTalonEnc.setInverted(true); //
    }

    @Override
    protected void execute()
    {
        System.out.println("executing");
        if(SmartDashboard.getBoolean("calibration_enabled",false))
        {
            velocity = Robot.CAL_VELOCITY;
            SmartDashboard.putNumber("enc_velocity", Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0)/*Constants.EVEL_TO_FPS*/);
//        System.out.println("kP: " + Robot.DRIVE_TRAIN.getkP());
//        Robot.DRIVE_TRAIN.leftRearTalonEnc.f
//        System.out.println("Should go to: "+ velocity);

            Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Velocity, velocity); // this
        }
    }

    @Override
    protected boolean isFinished()
    {
        return false;
//        return Robot.NAVX.getYaw() - initAngle >= ROT_UNTIL_STOP;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setTeleopSettings();
    }

}
