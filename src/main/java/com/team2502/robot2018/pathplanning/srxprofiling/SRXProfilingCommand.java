package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.Stopwatch;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class SRXProfilingCommand extends Command
{

    private final Trajectory leftTraj;
    private final Trajectory rightTraj;

    private final Notifier pointLoader;
    private final MotionProfileStatus status;
    private final ScheduledCommand[] commands;

    private final Stopwatch stopwatch = new Stopwatch();

    private final double dir;

    /**
     * Start a motion profiling command
     *
     * @param commands Any commands to run whilst driving
     * @param dir Drive forwards or backwards?
     * @param traj An array of length 2. Item 0 contains trajectory for left side, Item 1 contains trajectory for right side
     *
     * @see TrajConfig
     * @see TrajConfig#toTankDrive(Waypoint...)
     */
    public SRXProfilingCommand(ScheduledCommand[] commands, double dir, Trajectory[] traj){

        requires(Robot.DRIVE_TRAIN);
        setInterruptible(true);

        leftTraj = traj[0];
        rightTraj = traj[1];

//        Pathfinder.writeToCSV(new File("/home/lvuser/LEFT_TRAJ.csv"), leftTraj);
//        Pathfinder.writeToCSV(new File("/home/lvuser/RIGHT_TRAJ.csv"), rightTraj);

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
        float timeSpent;

        // start stopwatch
        stopwatch.poll();
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Disable);
        timeSpent = stopwatch.poll();

        DriverStation.getInstance().reportWarning("Disabling the robot (took " + timeSpent + "ms)", false);

        DriverStation.getInstance().reportWarning("Loading motion profiling points", false);

        stopwatch.poll(); //reset the stopwatch
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(leftTraj, rightTraj, dir);
        timeSpent = stopwatch.pollMs(); // check stopwatch

        DriverStation.getInstance().reportWarning("Loaded MP points (took " + timeSpent + "ms)", false);


        stopwatch.poll(); // reset stopwatch
        for(ScheduledCommand command : commands)
        {
            Scheduler.getInstance().add(command);
        }
        timeSpent = stopwatch.pollMs(); // record time

        DriverStation.getInstance().reportWarning("Adding scheduled commands to scheduler (took " + timeSpent + "ms)", false);


        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Enable);
    }

    @Override
    protected void execute()
    {
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