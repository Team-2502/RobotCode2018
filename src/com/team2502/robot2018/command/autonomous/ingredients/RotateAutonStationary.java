package com.team2502.robot2018.command.autonomous.ingredients;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import javafx.scene.transform.Rotate;

public class RotateAutonStationary extends Command
{

    private final float degrees;

    private final float wheelSpeed;
    private final AHRS navx;
    private final DriveTrainSubsystem driveTrain;
    private final boolean cw;

    public RotateAutonStationary(float degrees)
    {
        this(degrees, 2);
    }

    public RotateAutonStationary(float degrees, float fps)
    {
        driveTrain = Robot.DRIVE_TRAIN;
        requires(driveTrain);
        this.degrees = degrees;
        navx = Robot.NAVX;
        cw = degrees > 0;
        wheelSpeed = fps * Constants.FPS_TO_EVEL_DT;


    }

    @Override
    protected void initialize()
    {
        navx.reset();
    }

    @Override
    protected void execute()
    {
        if(cw)
        {
            rotateCW();
            System.out.println("rotate CW");
        }
        else
        {
            rotateCCW();
        }
    }

    @Override
    protected boolean isFinished()
    {
        if(MathUtils.epsilonEquals(degrees, 0F))
        {
            return true;
        }
        if(cw)
        {
            if(navx.getAngle() - degrees >= 0)
            {
                return true;
            }
        }
        else
        {
            if(navx.getAngle() - degrees <= 0)
            {
                return true;
            }
        }
        return false;
    }

    private void rotateCCW()
    {
        driveTrain.runMotors(ControlMode.Velocity, -wheelSpeed, wheelSpeed);
    }

    private void rotateCW()
    {
        driveTrain.runMotors(ControlMode.Velocity, wheelSpeed, -wheelSpeed);
    }
}
