package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.teleop.ToggleIntakeCommand;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import edu.wpi.first.wpilibj.command.CommandGroup;

import java.util.List;

/**
 * If the scale is on the same side as our starting position, go to it
 */
public class GoScaleSameSide extends CommandGroup
{
    /**
     * Go to the side of the scale that is on the same side of the field
     *
     * @param path The correct path
     */
    public GoScaleSameSide(List<Waypoint> path)
    {
        Robot.writeLog("Go Scale Same Side",200);
        addSequential(new PurePursuitCommand(path, false));

        addSequential(new ToggleIntakeCommand());

        emitCube();

        addParallel(new BackOffCommand());
    }

    private void emitCube()
    {
        addParallel(new RunIntakeCommand(0.3F, .5F));
    }
}
