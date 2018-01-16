package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import logger.Log;
import org.joml.Vector2f;

import static com.team2502.robot2018.command.autonomous.PurePursuitCommand.RAW_UNIT_PER_ROT;

public class EncoderDifferentialDriveLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator
{
    Vector2f location;
    float heading = 0;
    float angularVel = 0;

    public EncoderDifferentialDriveLocationEstimator()
    {
        location = new Vector2f(0, 0);
    }

    private long lastTime = -1;

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

        float leftVel = leftRevPerS * Robot.Physical.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        float rightVel = rightRevPerS * Robot.Physical.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE);


        Log.info("Left: {0,number,0.00} Right: {1,number,0.00}", leftVel, rightVel);

//        Log.debug("wheel vels: L: {0,number,#.###} \t\t R: {1,number,#.###}", leftVel, rightVel);

        Vector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE, dTime
                , heading);
        // Log.debug("adpp: " + absoluteDPos);
        Vector2f absLoc = location.add(absoluteDPos);
        // float angle = (float) Robot.NAVX.getYaw();
        heading += angularVel * dTime;
        Log.debug("absLoc: '{' {0,number,0.00}, {1,number,0.00} '}'", absLoc.x, absLoc.y);
        Log.debug("heading: {0,number,0.00}\n", heading);
        return absLoc;
    }

    @Override
    public float estimateHeading()
    {
        return heading;
    }
}
