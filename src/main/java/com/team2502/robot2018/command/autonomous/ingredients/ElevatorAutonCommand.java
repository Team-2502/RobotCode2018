package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

/**
 * @deprecated Not reliable since the encoder is unreliable (often turns red)
 */
public class ElevatorAutonCommand extends TimedCommand
{

    private float feet;
    private double eposFinal;
    private double eposInit;
    private boolean down;
    private static final int TOLERANCE = 25;

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
        eposInit = Robot.ELEVATOR.getPos() * Constants.Physical.Elevator.FEET_TO_EPOS_ELEV;
        eposFinal = feet * Constants.Physical.Elevator.FEET_TO_EPOS_ELEV;
        if(eposFinal < eposInit)
        {
            down = true;
        }
    }

    @Override
    protected void initialize()
    {
        Robot.writeLog("ElevatorAutonCommand init", 10);
    }

    @Override
    protected boolean isFinished()
    {
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
