package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

//TODO: Replace with encoder
public class ElevatorAutonCommand extends TimedCommand
{

    private float feet;

    public ElevatorAutonCommand(double timeout)
    {
        this(timeout, 1);
    }

    /**
     * Positive voltage is up by default is up
     *
     * @param timeout How long to try to get the elvator to the right height
     * @param feet    How high the elevator should be
     */
    public ElevatorAutonCommand(double timeout, float feet)
    {
        super(timeout);
        this.feet = feet;
    }

    @Override
    protected void initialize()
    {
        Robot.CLIMBER_SOLENOID.unlockElevator();
        Robot.ELEVATOR.calibrateEncoder();
        Robot.writeLog("ElevatorAutonCommand init", 10);
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.setElevatorPos(feet);
    }

    @Override
    protected void end()
    {
        Robot.CLIMBER_SOLENOID.lockElevator();
        Robot.ELEVATOR.stopElevator();
    }
}
