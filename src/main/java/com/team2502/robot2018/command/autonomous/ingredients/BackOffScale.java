package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
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
        addSequential(new DeadreckoningDrive(0.4F, -4F));
        addSequential(new ElevatorAutonCommand(2.5, 0));
        addSequential(new ToggleIntakeCommand());
    }
}
