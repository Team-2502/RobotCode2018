package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.TimedCommand;

public class ElevatorAutonDeadreckoningCommand extends TimedCommand
{

    private final double percentVoltage;

    public ElevatorAutonDeadreckoningCommand(double timeout, double percentVoltage)
    {
        super(timeout);
        requires(Robot.ELEVATOR);
        this.percentVoltage = percentVoltage;
    }

    @Override
    protected void execute()
    {
        Robot.ELEVATOR.moveElevator(percentVoltage);
    }
}
