package com.team2502.robot2018.command.test.group;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.command.test.RotateStationaryCommand;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

public class FullSystemsCommandTestGroup extends CommandGroup
{
    public FullSystemsCommandTestGroup()
    {
        Robot.write("::: Performing Full System Test :::");
        Robot.write("There are 3 second delays between each test");

        // Test 1
        Robot.write("1) Performing CCW Stationary Rotation. Tests navX, left encoder, right encoder (3 seconds)");

        RotateStationaryCommand rotateStationaryCommand = new RotateStationaryCommand(3);
        addSequential(rotateStationaryCommand);
        Robot.write("Success: "+rotateStationaryCommand.getSuccess());
        Robot.write("Results: "+rotateStationaryCommand.getResultsString());

        Robot.write("~waiting~");
        addSequential(new WaitCommand(3));

        // Test 2 (not implemented)

        // Finish
        Robot.write("Finished!");
    }
}
