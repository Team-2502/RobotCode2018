package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.util.List;

public class SRXProfilingCommand extends Command
{

    private final Trajectory leftTraj;
    private final Trajectory rightTraj;

    private final Notifier pointLoader;
    private final MotionProfileStatus status;
    private final ScheduledCommand[] commands;

    public SRXProfilingCommand(ScheduledCommand[] commands, List<Waypoint> waypointList)
    {
        this(commands, (Waypoint[]) waypointList.toArray());

    }

    public SRXProfilingCommand(ScheduledCommand[] commands, Waypoint... waypoints)
    {
        this(commands, Pathfinder.generate(waypoints, Constants.SRXProfiling.CONFIG_SETTINGS));
    }

    private SRXProfilingCommand(ScheduledCommand[] commands, Trajectory traj)
    {
        TankModifier modifier = new TankModifier(traj);
        modifier.modify(Constants.SRXProfiling.WHEELBASE_WIDTH);

        leftTraj = modifier.getLeftTrajectory();
        rightTraj = modifier.getRightTrajectory();

//        if(leftTraj.length() != rightTraj.length())
//        {
//            throw new Exception("Somehow, the left trajectory does not have the same number of points as the right trajectory (SRXProfilingCommand)");
//        }

        pointLoader = new Notifier(Robot.DRIVE_TRAIN::processMotionProfileBuffer);
        pointLoader.startPeriodic(Constants.SRXProfiling.PERIOD_SEC);
        status = new MotionProfileStatus();
        this.commands = commands;
    }

    @Override
    protected void initialize()
    {
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Disable);
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(leftTraj, rightTraj);
        for(ScheduledCommand command : commands)
        {
            Scheduler.getInstance().add(command);
        }
    }

    @Override
    protected void execute()
    {
        // Update status
        Robot.DRIVE_TRAIN.updateStatus(status);

        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Enable);

        // If we have run out of points to send to the lower-level
        if(status.hasUnderrun)
        {
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