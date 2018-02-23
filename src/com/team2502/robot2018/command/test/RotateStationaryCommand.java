package com.team2502.robot2018.command.test;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class


RotateStationaryCommand extends TimedCommand implements TestResult
{
    private final static int DEGREE_THRESHOLD = 10;
    private final static int ENCODER_THRESHOLD = 8000;
    private final static int ENCODER_DIF_THRESHOLD = 1000;
    private int encoderLeftInit;
    private int encoderRightInit;
    private double degreesRotated;
    private int encoderLeft;
    private int encoderRight;
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
        encoderLeftInit = Robot.DRIVE_TRAIN.leftRearTalon.getSelectedSensorPosition(0);
        encoderRightInit = Robot.DRIVE_TRAIN.rightRearTalon.getSelectedSensorPosition(0);
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

        // should be negative
        degreesRotated = Robot.NAVX.getAngle();

        // should be negative
        encoderLeft = Robot.DRIVE_TRAIN.leftRearTalon.getSelectedSensorPosition(0) - encoderLeftInit;

        // should be positive
        encoderRight = Robot.DRIVE_TRAIN.rightRearTalon.getSelectedSensorPosition(0) - encoderRightInit;


        System.out.println("");
    }

    @Override
    public String getResultsString()
    {
        return results;
    }

    private void generateResults()
    {
        if(!(degreesRotated < DEGREE_THRESHOLD))
        {
            success = false;
            results += "\nDegrees rotated (navX Gyro) was supposed to be < " + DEGREE_THRESHOLD + ", but it actually was " + degreesRotated + ". " +
                       "1) Did the robot not turn CCW? 2) Is the navX working?";
        }
        else if(encoderLeft >= -ENCODER_THRESHOLD)
        {
            success = false;
            results += "\nLeft encoder position detected was supposed to be < " + -ENCODER_THRESHOLD + ", but it actually was " + encoderLeft + ". " +
                       "1) Is the left encoder backwards? 2) Is the left encoder working?";
        }
        else if(encoderRight <= ENCODER_THRESHOLD)
        {
            success = false;
            results += "\nRight encoder position detected was supposed to be > " + ENCODER_THRESHOLD + ", but it actually was " + encoderLeft + ". " +
                       "Is the right encoder backwards? 2) Is the right encoder working?";
        }
        else
        {
            int encDif = encoderRight + encoderLeft;
            if(Math.abs(encDif) >= ENCODER_THRESHOLD)
            {
                success = false;
                results += "\nWhen adding encoder positions the result should be near 0. The threshold was (-1000,1000), " +
                           "but encoderRight-encoderLeft was actually " + encDif + ". 1) Are the encoders different? (one quadrature one mag)";
            }
        }
    }

    @Override
    public boolean getSuccess()
    {
        return success;
    }
}
