package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.Stopwatch;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class CoolSRXProfilingCommand extends Command
{

    private final TrajectoryPoint[] leftTraj;
    private final TrajectoryPoint[] rightTraj;

    private final Notifier pointLoader;
    private final MotionProfileStatus status;
    private final ScheduledCommand[] commands;

    private final Stopwatch stopwatch = new Stopwatch();

    private final double timeToTakeSec;

    /**
     * Start a motion profiling command
     *
     * @param traj A 2d array containing 2 arrays of n {@link TrajectoryPoint}s.
     * @param commands Any commands to run whilst driving
     *
     * @see TrajConfig
     * @see TrajConfig#toTankDrive(Waypoint...)
     */
    public CoolSRXProfilingCommand(TrajectoryPoint[][] traj, ScheduledCommand[] commands ){

        requires(Robot.DRIVE_TRAIN);
        setInterruptible(true);

        leftTraj = traj[0];
        rightTraj = traj[1];

        pointLoader = new Notifier(() -> {
            Robot.DRIVE_TRAIN.processMotionProfileBuffer();
        });

        pointLoader.startPeriodic(Constants.SRXProfiling.PERIOD_SEC);
        status = new MotionProfileStatus();
        this.commands = commands;

        // each waypoint takes period seconds to execute
        // by having n waypoints that take period seconds to execute, n * period = time to execute
        // we will give it a small amount of extra time
        timeToTakeSec = (traj[0].length + 2) * Constants.SRXProfiling.PERIOD_SEC;
        setTimeout(timeToTakeSec);
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
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(leftTraj, rightTraj);
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
        return status.hasUnderrun || status.isLast || isTimedOut();
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