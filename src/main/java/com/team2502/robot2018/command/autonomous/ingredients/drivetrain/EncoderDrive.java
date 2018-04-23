package com.team2502.robot2018.command.autonomous.ingredients.drivetrain;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

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
        Robot.DRIVE_TRAIN.runMotorsPosition((float) feetLeft, (float) feetRight);
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut() || Math.abs(Robot.DRIVE_TRAIN.getAvgEncLoopError()) < Constants.Physical.DriveTrain.ALLOWABLE_POS_ERROR_EPOS;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setTeleopSettings();
        Robot.DRIVE_TRAIN.stop();
    }


}
