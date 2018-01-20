package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.Vector2f;

import static com.team2502.robot2018.command.autonomous.PurePursuitCommand.RAW_UNIT_PER_ROT;

/**
 * WIP, do not use yet!!
 */
@Deprecated
public class EncoderSkidSteerLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator
{
    Vector2f location;
    float heading = 0;
    float angularVel = 0;
    private long lastTime = -1;

    public EncoderSkidSteerLocationEstimator()
    {
        location = new Vector2f(0, 0);
    }

    private float getDTime()
    {
        long nanoTime = System.nanoTime();
        double dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return (float) (dTime / 1E9);
    }

    @Override
    public Vector2f estimateLocation()
    {
        // How many time passed
        float dTime = getDTime();

        // talon inversed
        float leftRevPerS = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0) * 10F / RAW_UNIT_PER_ROT;

        float rightRevPerS = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0) * 10F / RAW_UNIT_PER_ROT;

        float leftVelNoSlide = leftRevPerS * Constants.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        float rightVelNoSlide = rightRevPerS * Constants.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        float vTan = (leftVelNoSlide + rightVelNoSlide) / 2;

        // longitudinal slip proportion
        float i = 1 - vTan / (Constants.WHEEL_ROLLING_RADIUS_FT * angularVel);

//        float iL = leftRevPerS/a;
//        float iR = rightRevPerS/a;


        angularVel = MathUtils.Kinematics.getAngularVel(leftVelNoSlide, rightVelNoSlide, Robot.LATERAL_WHEEL_DISTANCE);


        System.out.printf("Left: %.2f Right: %.2f\n", leftVelNoSlide, rightVelNoSlide);

//        Log.debug("wheel vels: L: {0,number,#.###} \t\t R: {1,number,#.###}", leftVel, rightVel);

        Vector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVelNoSlide, rightVelNoSlide, Robot.LATERAL_WHEEL_DISTANCE, dTime
                , heading);
        // Log.debug("adpp: " + absoluteDPos);
        Vector2f absLoc = location.add(absoluteDPos);
        // float angle = (float) Robot.NAVX.getYaw();
        heading += angularVel * dTime;
        System.out.printf("absLoc: %.2f, %.2f\n", absLoc.x, absLoc.y);
        System.out.printf("heading: %.2f\n", heading);
        System.out.println();
        return absLoc;
    }

    @Override
    public float estimateHeading()
    {
        return heading;
    }
}
