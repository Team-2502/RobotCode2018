package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.modifiers.TankModifier;

public class SRXProfilingCommand extends Command
{
    private final Trajectory trajectoryLeft;
    private final Trajectory trajectoryRight;

    private final MotionProfileStatus statusLeft;
    private final MotionProfileStatus statusRight;

    public SRXProfilingCommand(Trajectory trajectory)
    {
        requires(Robot.DRIVE_TRAIN);

        TankModifier tankModifier = new TankModifier(trajectory);
        tankModifier.modify(Constants.SRXProfiling.WHEELBASE_WIDTH);
        trajectoryLeft = tankModifier.getLeftTrajectory();
        trajectoryRight = tankModifier.getRightTrajectory();

        statusLeft = new MotionProfileStatus();
        statusRight = new MotionProfileStatus();

        updateStatus();
    }

    @Override
    protected void initialize()
    {
        Robot.DRIVE_TRAIN.loadTrajectoryPoints(trajectoryLeft, trajectoryRight);

        while(statusLeft.btmBufferCnt < Constants.SRXProfiling.MIN_PTS_BUFFER_CNT)
        {
            Robot.DRIVE_TRAIN.leftFrontTalonEnc.
        }

    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    protected void end()
    {
        Robot.DRIVE_TRAIN.setMotionProfilingState(SetValueMotionProfile.Hold);
    }

    private void updateStatus()
    {
        Robot.DRIVE_TRAIN.leftFrontTalonEnc.getMotionProfileStatus(statusLeft);
        Robot.DRIVE_TRAIN.rightFrontTalonEnc.getMotionProfileStatus(statusRight);
    }
}
