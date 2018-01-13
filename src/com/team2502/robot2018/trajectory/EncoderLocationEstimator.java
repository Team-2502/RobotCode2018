package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.Vector2f;

import static com.team2502.robot2018.command.autonomous.PurePursuitCommand.RAW_UNIT_PER_ROT;

public class EncoderLocationEstimator implements ILocationEstimator
{
    float headingStartDegrees;
    Vector2f location;
    float heading = 0;

    public void initialize(){
        headingStartDegrees = ((float) Robot.NAVX.getAngle());
        location = new Vector2f(0, 0);
    }

    private long lastTime = -1;

    private float getDTime()
    {
        long nanoTime = System.nanoTime();
        double dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return (float) (dTime / 1E6);
    }

    public EncoderLocationEstimator()
    {

    }

    @Override
    public Vector2f estimateLocation()
    {
        // How many 100 ms intervals occured
        float dTime = getDTime() / 10F;

        // talon inversed
        float leftRevPer100ms = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0) / RAW_UNIT_PER_ROT;

        float rightRevPer100ms = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0) / RAW_UNIT_PER_ROT;

        float leftVel = leftRevPer100ms * Robot.Physical.WHEEL_DIAMETER_FT;

        float rightVel = rightRevPer100ms * Robot.Physical.WHEEL_DIAMETER_FT;

//        Log.debug("wheel vels: L: {0,number,#.###} \t\t R: {1,number,#.###}", leftVel, rightVel);

        Vector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE, dTime
                , heading);
        // Log.debug("adpp: " + absoluteDPos);
        Vector2f absLoc = location.add(absoluteDPos);
        heading = MathUtils.Geometry.getDTheta(headingStartDegrees, (float) Robot.NAVX.getAngle());
        return absLoc;
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
