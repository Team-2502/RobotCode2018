package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

public class SRXProfilingCommand extends Command
{

    private final StreamableProfile profile;

    private final Notifier pointLoader;
    private final MotionProfileStatus status;
    private final ScheduledCommand[] commands;

    public SRXProfilingCommand(StreamableProfile profile, ScheduledCommand... commands)
    {
        this.profile = profile;

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
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(profile);
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