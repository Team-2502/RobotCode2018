package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.pathplanning.purepursuit.Path;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.io.File;
import java.util.List;

public class SRXProfilingCommand extends Command
{

    private final Trajectory leftTraj;
    private final Trajectory rightTraj;

    private final Notifier pointLoader;
    private final MotionProfileStatus status;
    private final ScheduledCommand[] commands;

    private final double dir;

    public SRXProfilingCommand(ScheduledCommand[] commands, double dir, List<Waypoint> waypointList)
    {
        this(commands, dir, (Waypoint[]) waypointList.toArray());
    }

    public SRXProfilingCommand(ScheduledCommand[] commands, double dir, Waypoint... waypoints)
    {
        this(commands, dir, Pathfinder.generate(waypoints, Constants.SRXProfiling.CONFIG_SETTINGS));
    }

    private SRXProfilingCommand(ScheduledCommand[] commands, double dir, Trajectory traj)
    {
        TankModifier modifier = new TankModifier(traj);
        modifier.modify(Constants.SRXProfiling.WHEELBASE_WIDTH);

        leftTraj = modifier.getLeftTrajectory();
        rightTraj = modifier.getRightTrajectory();

        Pathfinder.writeToCSV(new File("/home/lvuser/LEFT_TRAJ.csv"), leftTraj);
        Pathfinder.writeToCSV(new File("/home/lvuser/RIGHT_TRAJ.csv"), rightTraj);

//        if(leftTraj.length() != rightTraj.length())
//        {
//            throw new Exception("Somehow, the left purepursuit does not have the same number of points as the right purepursuit (SRXProfilingCommand)");
//        }

        this.dir = dir;

        pointLoader = new Notifier(() -> {
            Robot.DRIVE_TRAIN.processMotionProfileBuffer();
        });
        pointLoader.startPeriodic(Constants.SRXProfiling.PERIOD_SEC);
        status = new MotionProfileStatus();
        this.commands = commands;
    }

    @Override
    protected void initialize()
    {
        DriverStation.getInstance().reportWarning("Disabling the robot", false);
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Disable);

        DriverStation.getInstance().reportWarning("Loading purepursuit points", false);
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(leftTraj, rightTraj, dir);
        for(ScheduledCommand command : commands)
        {
            Scheduler.getInstance().add(command);
        }

        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Enable);
    }

    @Override
    protected void execute()
    {
        DriverStation.getInstance().reportWarning("thinking . . .", false);

        // Update status
        Robot.DRIVE_TRAIN.updateStatus(status);



        // If we have run out of points to send to the lower-level
        if(status.hasUnderrun)
        {
            DriverStation.getInstance().reportWarning("Ran out of points", false);
            // stop loading points
            pointLoader.stop();

            // clear the flag
            Robot.DRIVE_TRAIN.clearMotionProfileHasUnderrun();
        }

    }

    @Override
    protected boolean isFinished()
    {
        // If this is the last point
        return status.isLast;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Hold);
        Robot.DRIVE_TRAIN.resetTalonControlFramePeriod();
    }
}