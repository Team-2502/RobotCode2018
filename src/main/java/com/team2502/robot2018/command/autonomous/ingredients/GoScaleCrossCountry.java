package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.util.List;

public class GoScaleCrossCountry extends CommandGroup
{
    /**
     * Go to the side of the scale that is across the field
     *
     * @param path The correct path
     */
    public GoScaleCrossCountry(List<Waypoint> path)
    {
        System.out.println("GOING CROSS COUNRY SCALE!!!");
        addSequential(new PurePursuitCommand(path, false));

        addParallel(new ActiveIntakeRotate(1F, -0.5));

        emitCube();

        addSequential(new BackOffCommand());
    }

    private void emitCube()
    {
        addSequential(new RunIntakeCommand(1, .5F));
    }
}
