package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.command.autonomous.ingredients.ElevatorAutonCommand;
import com.team2502.robot2018.command.autonomous.ingredients.Paths;
import com.team2502.robot2018.command.autonomous.ingredients.PurePursuitCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class TestCommandGroup extends CommandGroup
{
    public TestCommandGroup()
    {
        addSequential(new ElevatorAutonCommand(3, Constants.SWITCH_ELEV_HEIGHT_FT));
        addSequential(new ElevatorAutonCommand(3, 0));
        addSequential(new ElevatorAutonCommand(3, Constants.SCALE_ELEV_HEIGHT_FT));
        addSequential(new ElevatorAutonCommand(3, 0));
    }
}
