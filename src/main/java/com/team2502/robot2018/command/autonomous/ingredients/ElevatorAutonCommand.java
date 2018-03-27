package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * @deprecated Not reliable since the encoder is unreliable (often turns red)
 */
public class ElevatorAutonCommand extends TimedCommand
{

    private static final int TOLERANCE = 7000;
    private float feet;
    private double eposFinal;
    private double eposInit;
    private boolean down;

    public ElevatorAutonCommand(double timeout)
    {
        this(timeout, 1);
    }

    /**
     * Positive voltage is up by default is down
     *
     * @param timeout How long to try to evaluateY the elvator to the right height
     * @param feet    How high the elevator should be
     */
    public ElevatorAutonCommand(double timeout, float feet)
    {
        super(timeout);
        this.feet = feet;
        eposFinal = feet * Constants.Physical.Elevator.FEET_TO_EPOS_ELEV;
    }

    @Override
    protected void initialize()
    {
        eposInit = Robot.ELEVATOR.getPos();
        if(eposFinal < eposInit)
        {
            down = true;
        }
        Robot.writeLog("ElevatorAutonCommand init", 10);
        Robot.writeLog("eposInit %.2f eposFinal %.2f", 200, (float) eposInit, (float) eposFinal);
    }

    @Override
    protected boolean isFinished()
    {
        if(super.isFinished())
        {
            return true;
        }

        if(down)
        {
            return Robot.ELEVATOR.getPos() < eposFinal + TOLERANCE;
        }
        else
        {
            return Robot.ELEVATOR.getPos() > eposFinal - TOLERANCE;
        }
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.setElevatorPos(feet);
    }

    @Override
    protected void end()
    {
        Robot.ELEVATOR.stopElevator();
    }
}
