package com.team2502.robot2018.command.test;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;
import logger.ShuffleboardLog;

public class RotateStationaryCommand extends TimedCommand implements TestResult
{
    private final static int DEGREE_THRESHOLD = 10;
    private final static int ENCODER_THRESHOLD = 8000;
    private final static int ENCODER_DIF_THRESHOLD = 1000;
    private float encoderLeftInit;
    private float encoderRightInit;
    private double degreesRotated;
    private float encoderLeft;
    private float encoderRight;
    private boolean success;
    private String results;

    public RotateStationaryCommand(int seconds)
    {
        super(seconds);
        requires(Robot.DRIVE_TRAIN);
    }

    @Override
    protected void initialize()
    {
        Robot.NAVX.reset();
        // TODO: see if resets position when called
        encoderLeftInit = Robot.DRIVE_TRAIN.getLeftPos();
        encoderRightInit = Robot.DRIVE_TRAIN.getRightPos();
    }

    @Override
    protected void execute()
    {
        // rotate counterclockwise
        Robot.DRIVE_TRAIN.runMotors(-0.5F, 0.5F);
    }

    @Override
    protected void end()
    {

        // should be negative (counterclockwise)
        degreesRotated = Robot.NAVX.getAngle();

        // should be negative
        encoderLeft = Robot.DRIVE_TRAIN.getLeftPos() - encoderLeftInit;

        // should be positive
        encoderRight = Robot.DRIVE_TRAIN.getRightPos() - encoderRightInit;

        logResults();
    }

    @Override
    public String getResultsString()
    {
        return results;
    }

    private void logResults()
    {
        if(!(degreesRotated > DEGREE_THRESHOLD))
        {
            success = false;
            ShuffleboardLog.getInstance().log("Degrees rotated (navX Gyro) was supposed to be greater than " + DEGREE_THRESHOLD + ", but it actually was " + degreesRotated + ". " +
                                              "1) Did the robot not turn counterclockwise? 2) Is the navX working?");
        }
        else if(encoderLeft >= -ENCODER_THRESHOLD)
        {
            success = false;
            ShuffleboardLog.getInstance().log("Left encoder position detected was supposed to be < " + -ENCODER_THRESHOLD + ", but it actually was " + encoderLeft + ". " +
                                              "1) Is the left encoder backwards? 2) Is the left encoder working?");
        }
        else if(encoderRight <= ENCODER_THRESHOLD)
        {
            success = false;
            ShuffleboardLog.getInstance().log("Right encoder position detected was supposed to be > " + ENCODER_THRESHOLD + ", but it actually was " + encoderLeft + ". " +
                                              "Is the right encoder backwards? 2) Is the right encoder working?");
        }
        else
        {
            float encDif = encoderRight + encoderLeft;
            if(Math.abs(encDif) >= ENCODER_THRESHOLD)
            {
                success = false;
                ShuffleboardLog.getInstance().log("When adding encoder positions the result should be near 0. The threshold was (-1000,1000), " +
                                                  "but encoderRight-encoderLeft was actually " + encDif + ". 1) Are the encoders different? (one quadrature one mag)");
            }
        }
    }

    @Override
    public boolean getSuccess()
    {
        return success;
    }
}
