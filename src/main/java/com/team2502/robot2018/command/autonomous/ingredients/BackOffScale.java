package com.team2502.robot2018.command.autonomous.ingredients;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * After putting a cube in the scale, back off
 */
public class BackOffScale extends CommandGroup
{
    /**
     * After putting a cube in the scale, back off
     */
    public BackOffScale()
    {

        addSequential(new DeadreckoningDrive(0.5F, -2F));
        addSequential(new ElevatorAutonCommand(2.5, 0));
    }
}