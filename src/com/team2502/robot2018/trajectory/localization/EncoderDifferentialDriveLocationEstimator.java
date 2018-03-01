package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.Stopwatch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

public class EncoderDifferentialDriveLocationEstimator implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator
{
    private ImmutableVector2f location;
    private IRotationalLocationEstimator rotEstimator;
    private Stopwatch stopwatch;

    public EncoderDifferentialDriveLocationEstimator(IRotationalLocationEstimator rotEstimator)
    {
        location = new ImmutableVector2f(0, 0);
        stopwatch = new Stopwatch();
        this.rotEstimator = rotEstimator;
    }

    @Override
    public ImmutableVector2f estimateLocation()
    {
        float dTime = stopwatch.dTime();
        float leftVel = Robot.DRIVE_TRAIN.getLeftVel();
        float rightVel = Robot.DRIVE_TRAIN.getRightVel();

        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosLine(leftVel, rightVel, dTime, rotEstimator.estimateHeading());
        location = location.add(dPos);

        return location;
    }

    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        ImmutableVector2f vector = MathUtils.Geometry.getVector(estimateSpeed(), rotEstimator.estimateHeading());
        return vector;
    }

    @Override
    public float getLeftWheelSpeed()
    {
        return Robot.DRIVE_TRAIN.getLeftVel();
    }

    @Override
    public float getRightWheelSpeed()
    {
        return Robot.DRIVE_TRAIN.getRightVel();
    }

    @Override
    public float estimateSpeed()
    {
        return MathUtils.Kinematics.getTangentialSpeed(getLeftWheelSpeed(),getRightWheelSpeed());
    }
}

