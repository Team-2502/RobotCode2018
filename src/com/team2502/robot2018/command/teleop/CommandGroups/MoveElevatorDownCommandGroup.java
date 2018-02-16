package com.team2502.robot2018.command.teleop.CommandGroups;

import com.team2502.robot2018.command.teleop.ElevatorCommand;
import com.team2502.robot2018.command.teleop.LockElevatorCommand;
import com.team2502.robot2018.command.teleop.UnlockElevatorCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class MoveElevatorDownCommandGroup extends CommandGroup
{
    public MoveElevatorDownCommandGroup()
    {
        addSequential(new UnlockElevatorCommand());
        addSequential(new WaitCommand(.1));
        addSequential(new ElevatorCommand(-0.6));
        addSequential(new LockElevatorCommand());
    }

}
