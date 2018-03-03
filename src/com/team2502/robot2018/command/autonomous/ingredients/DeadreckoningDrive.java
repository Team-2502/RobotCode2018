package com.team2502.robot2018.command.autonomous.ingredients;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class DeadreckoningDrive extends TimedCommand
{

    private final float speed;

    public DeadreckoningDrive(double timeout, float speed)
    {
        super(timeout);
        this.speed = speed;
    }

    @Override
    protected void execute()
    {
        Robot.DRIVE_TRAIN.runMotorsVelocity(speed, speed);
    }
}
