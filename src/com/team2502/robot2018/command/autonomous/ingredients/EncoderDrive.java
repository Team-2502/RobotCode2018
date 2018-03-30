package com.team2502.robot2018.command.autonomous.ingredients;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.Constants;
import edu.wpi.first.wpilibj.command.Command;
import com.team2502.robot2018.Robot;

public class EncoderDrive extends Command
{

    private double feetLeft = 3D;
    private double feetRight = 3D;


    /**
     * @param timeout the timeout in seconds for the command
     */
    private EncoderDrive(double timeout)
    {
        super(timeout);
        requires(Robot.DRIVE_TRAIN);
    }

    public EncoderDrive(double feet, double maxtime) { this(feet, feet, maxtime); }

    public EncoderDrive(double feetLeft, double feetRight, double time)
    {
        this(time);

        this.feetLeft = feetLeft;
        this.feetRight = feetRight;
    }

    @Override
    protected void initialize()
    {
        Robot.DRIVE_TRAIN.setAutonSettings();
    }

    @Override
    protected void execute()
    {
        feetLeft = Robot.DRIVE_TRAIN.fakeToRealEncUnits((float) feetLeft);
        feetRight = Robot.DRIVE_TRAIN.fakeToRealEncUnits((float) feetRight);
        Robot.DRIVE_TRAIN.runMotors(ControlMode.Position, (float) feetLeft, (float) feetRight);
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut();// || Math.abs(Robot.DRIVE_TRAIN.getAvgEncLoopError()) < 100;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setTeleopSettings();
        Robot.DRIVE_TRAIN.stop();
    }


}
