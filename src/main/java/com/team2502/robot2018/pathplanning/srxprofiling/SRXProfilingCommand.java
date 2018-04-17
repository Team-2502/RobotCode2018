package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import jaci.pathfinder.Trajectory;

public class SRXProfilingCommand extends Command
{


    private final Notifier pointLoader;
    private final MotionProfileStatus status;
    private final ScheduledCommand[] commands;

    private final Trajectory leftTraj;
    private final Trajectory rightTraj;
    private final double dir;

    public SRXProfilingCommand(ScheduledCommand[] commands, double dir, Trajectory[] traj)
    {

        requires(Robot.DRIVE_TRAIN);
        setInterruptible(true);

        leftTraj = traj[0];
        rightTraj = traj[1];

        this.dir = dir;

//        Pathfinder.writeToCSV(new File("/home/lvuser/LEFT_TRAJ.csv"), leftTraj);
//        Pathfinder.writeToCSV(new File("/home/lvuser/RIGHT_TRAJ.csv"), rightTraj);

//        if(leftTraj.length() != rightTraj.length())
//        {
//            throw new Exception("Somehow, the left purepursuit does not have the same number of points as the right purepursuit (SRXProfilingCommand)");
//        }

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

        DriverStation.getInstance().reportWarning("Loading motion profile points", false);
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(leftTraj, rightTraj, dir);
        for(ScheduledCommand command : commands)
        {
            Scheduler.getInstance().add(command);
        }

        DriverStation.getInstance().reportWarning("Disabling the robot", false);

        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Enable);
    }

    @Override
    protected void execute()
    {
        DriverStation.getInstance().reportWarning("thinking . . .", false);

        // Update status
        Robot.DRIVE_TRAIN.updateStatus(status);

        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Enable);

        // If we have run out of points to send to the lower-level
//        if(status.hasUnderrun)
//        {
//            DriverStation.getInstance().reportWarning("Ran out of points", false);
//            // stop loading points
//            pointLoader.stop();
//
//            // clear the flag
//            Robot.DRIVE_TRAIN.clearMotionProfileHasUnderrun();
//        }

    }

    @Override
    protected boolean isFinished()
    {
        // If this is the last point
        return status.hasUnderrun || status.isLast || super.isTimedOut();
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Hold);
        Robot.DRIVE_TRAIN.resetTalonControlFramePeriod();
        pointLoader.stop();
        Robot.DRIVE_TRAIN.clearMotionProfileHasUnderrun();
        DriverStation.getInstance().reportError("ENDING MP", false);
    }
}