package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.joml.ImmutableVector2f;

public class EncoderDifferentialDriveLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator, ITranslationalVelocityEstimator
{
    ImmutableVector2f location;
    float encHeading = 0;
    private long lastTime = -1;
    private IRotationalLocationEstimator rotationalLocationEstimator;

    public EncoderDifferentialDriveLocationEstimator()
    {
        location = new ImmutableVector2f(0, 0);
        this.rotationalLocationEstimator = () -> encHeading;
    }

    public EncoderDifferentialDriveLocationEstimator(IRotationalLocationEstimator rotationalLocationEstimator)
    {
        location = new ImmutableVector2f(0, 0);
        this.rotationalLocationEstimator = rotationalLocationEstimator;
    }

    private float getDTime()
    {
        long nanoTime = System.nanoTime();
        double dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return (float) (dTime / 1E9);
    }

    @Override
    public ImmutableVector2f estimateLocation()
    {
        // How many time passed
        float dTime = getDTime();

        // talon inversed
        float leftVel = Robot.DRIVE_TRAIN.getLeftVel();

        float rightVel = Robot.DRIVE_TRAIN.getRightVel();

        SmartDashboard.putNumber("encL", leftVel);
        SmartDashboard.putNumber("encR", rightVel);

        ImmutableVector2f immutableVector2f = MathUtils.LinearAlgebra.rotate2D(new ImmutableVector2f(0, (leftVel + rightVel) * dTime / 2), estimateHeading());

        location = location.add(immutableVector2f);

        return location;
    }

    @Override
    public float estimateHeading()
    {
        return rotationalLocationEstimator.estimateHeading();
    }

    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        ImmutableVector2f vector = MathUtils.Geometry.getVector(estimateSpeed(), estimateHeading());
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
        return (getLeftWheelSpeed() + getRightWheelSpeed()) / 2F;
    }
}

