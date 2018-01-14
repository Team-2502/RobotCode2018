package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.Vector2f;

import java.util.Random;

import static com.team2502.robot2018.command.autonomous.PurePursuitCommand.RAW_UNIT_PER_ROT;

public class EncoderLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator
{
    private final Random random;
    // float headingStartDegrees;
    Vector2f location;
    float heading = 0;

    public void initialize(){
        // headingStartDegrees = Robot.NAVX.getCompassHeading();
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

    public EncoderLocationEstimator()
    {
        random = new Random();
    }

    @Override
    public Vector2f estimateLocation()
    {
        // How many time passed
        float dTime = getDTime();

//        System.out.printf("dTime: %.2f\n",dTime);

        // talon inversed
        float leftRevPerS = Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0) *10F / RAW_UNIT_PER_ROT;

        float rightRevPerS = -Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0)*10F / RAW_UNIT_PER_ROT;

        float leftVel = leftRevPerS * Robot.Physical.WHEEL_DIAMETER_FT;

        float rightVel = rightRevPerS * Robot.Physical.WHEEL_DIAMETER_FT;

        System.out.printf("Left: %.2f Right: %.2f\n",leftVel,rightVel);

        float angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE);

//        Log.debug("wheel vels: L: {0,number,#.###} \t\t R: {1,number,#.###}", leftVel, rightVel);

        Vector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE, dTime
                , heading);
        // Log.debug("adpp: " + absoluteDPos);
        Vector2f absLoc = location.add(absoluteDPos);
        // float angle = (float) Robot.NAVX.getYaw();
        heading+= angularVel*dTime;
        return absLoc;
    }

    @Override
    public float estimateHeading()
    {
        return 0;
    }

    interface IRobotState {
        /**
         * @return Heading in radians from (0,2pi)
         */
        default float getHeading()
        {
            double radians = MathUtils.deg2Rad(Robot.NAVX.getAngle());
            return (float) -(radians % MathUtils.TAU);
        }

        float getEncoderLeft();
        float getEncoderRight();
    }
}
