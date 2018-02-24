package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

//TODO: Replace with encoder
public class ElevatorAutonCommand extends TimedCommand
{

    private float speed;

    public ElevatorAutonCommand(double timeout)
    {
        this(timeout,1);
    }
    /**
     * Positive voltage is up by default is up
     * @param timeout
     * @param speed
     */
    public ElevatorAutonCommand(double timeout, float speed)
    {
        super(timeout);
        this.speed = speed;
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveElevator(speed);
    }

    @Override
    protected void end()
    {
        Robot.ELEVATOR.stopElevator();
    }
}
