package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class DriveTime extends Command
{
    private float voltage;

    /**
     * @deprecated
     * Veer off to the side inconsistently
     * <p>
     * <br>
     * <p>
     * Play Russian Roulette with the robot
     * <p>
     * <br>
     * <p>
     * Invest all of your money into the Lehman Brothers
     *
     * @param timeout How long we should put you in the timeout corner for using this class
     * @param voltage How much voltage we should give the robot to restart the robot's heart, as it got a heart attack because you used this class.
     */
    public DriveTime(float timeout, float voltage)
    {
        requires(Robot.DRIVE_TRAIN);
        setTimeout(timeout);
        this.voltage = voltage;
    }

    @Override
    protected void execute() { Robot.DRIVE_TRAIN.runMotorsVoltage(voltage, voltage);}

    @Override
    protected boolean isFinished()
    {
        return isTimedOut();
    }
}
