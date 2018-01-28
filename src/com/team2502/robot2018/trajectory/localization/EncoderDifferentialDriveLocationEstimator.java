package com.team2502.robot2018.trajectory.localization;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;

public class EncoderDifferentialDriveLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator
{
    ImmutableVector2f location;
    float angularVel = 0;
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
        float leftVel = Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0) * Constants.EVEL_TO_FPS;

        float rightVel = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0) * Constants.EVEL_TO_FPS;

//        System.out.printf("leftVel: %.2f, rightVel: %.2f\n",leftVel,rightVel);

        angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, Constants.LATERAL_WHEEL_DISTANCE_FT);

        ImmutableVector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVel, rightVel, Constants.LATERAL_WHEEL_DISTANCE_FT, dTime
                , rotationalLocationEstimator.estimateHeading());

//        System.out.printf("absDPos: %.2f,%.2f\n",absoluteDPos.x,absoluteDPos.y);
        location = location.add(absoluteDPos);
        ImmutableVector2f absLoc = location.add(absoluteDPos);

        encHeading += angularVel * dTime;
        return absLoc;
    }

    @Override
    public float estimateHeading()
    {
        return rotationalLocationEstimator.estimateHeading();
    }
}
