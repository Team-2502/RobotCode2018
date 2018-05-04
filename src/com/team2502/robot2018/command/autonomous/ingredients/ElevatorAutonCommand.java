package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.ElevatorSubsystem;
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
     * @param timeout
     * @param feet
     */
    public ElevatorAutonCommand(double timeout, float feet)
    {
        super(timeout);
        this.feet = feet;
    }

    @Override
    protected void initialize()
    {
        // DO NOT RESET THE DAMN THING, THIS WILL MAKE LOWERING IMPOSSIBLE
//        Robot.ELEVATOR.calibrateEncoder();
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
        Robot.ELEVATOR.stopElevator();
    }
}
