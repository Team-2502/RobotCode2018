package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import com.team2502.robot2018.trajectory.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.util.List;

public class GoScaleCrossCountry extends CommandGroup
{
    /**
     * @param path The correct path
     */
    public GoScaleCrossCountry(List<Waypoint> path)
    {
        addSequential(new PurePursuitCommand(Paths.Right.leftScale));

        addParallel(new ActiveIntakeRotate(1F, -0.5));

        emitCube();

        addSequential(new BackOffScale());
    }

    private void emitCube()
    {
        addSequential(new ShootCubeCommand(1, .5F));
    }
}
