package com.team2502.robot2018.pathplanning.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.Stopwatch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

/**
 *  Localization using encoders which can is primarily used for estimating the speed of the robot.
 *  If an {@link IRotationalLocationEstimator} such as {@link NavXLocationEstimator} is added, it can do much more,
 *  estimating the absolute position of the robot.
 */
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
        float dTime = stopwatch.poll();
        float leftVel = Robot.DRIVE_TRAIN.getLeftVel();
        float rightVel = Robot.DRIVE_TRAIN.getRightVel();

        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosLine(leftVel, rightVel, dTime, rotEstimator.estimateHeading());

        location = location.add(dPos);

        SmartDashboard.putNumber("posX", location.x);
        SmartDashboard.putNumber("posY", location.y);

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
        return MathUtils.Kinematics.getTangentialSpeed(getLeftWheelSpeed(), getRightWheelSpeed());
    }
}

