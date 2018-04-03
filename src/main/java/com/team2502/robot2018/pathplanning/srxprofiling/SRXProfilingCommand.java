package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;

import java.security.InvalidParameterException;
import java.util.List;

public class SRXProfilingCommand extends Command
{

    private final Trajectory leftTraj;
    private final Trajectory rightTraj;

    private final Notifier pointLoader;
    private final MotionProfileStatus status;

    public SRXProfilingCommand(List<Waypoint> waypointList) throws Exception
    {
        this((Waypoint[]) waypointList.toArray());
    }

    public SRXProfilingCommand(Waypoint... waypoints) throws Exception
    {
        this(Pathfinder.generate(waypoints, Constants.SRXProfiling.CONFIG_SETTINGS));
    }

    public SRXProfilingCommand(Trajectory traj) throws Exception
    {
        TankModifier modifier = new TankModifier(traj);
        modifier.modify(Constants.SRXProfiling.WHEELBASE_WIDTH);

        leftTraj = modifier.getLeftTrajectory();
        rightTraj = modifier.getRightTrajectory();

        if(leftTraj.length() != rightTraj.length())
        {
            throw new Exception("Somehow, the left trajectory does not have the same number of points as the right trajectory (SRXProfilingCommand)");
        }

        pointLoader = new Notifier(Robot.DRIVE_TRAIN::processMotionProfileBuffer);
        pointLoader.startPeriodic(Constants.SRXProfiling.PERIOD_SEC);
        status = new MotionProfileStatus();
    }

    @Override
    protected void initialize()
    {
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Disable);
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(leftTraj, rightTraj);
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
        return status.hasUnderrun;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Hold);
        Robot.DRIVE_TRAIN.resetTalonControlFramePeriod();
    }
}
