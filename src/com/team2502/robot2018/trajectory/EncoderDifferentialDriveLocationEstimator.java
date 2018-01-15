package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import org.joml.Vector2f;

import static com.team2502.robot2018.command.autonomous.PurePursuitCommand.RAW_UNIT_PER_ROT;

public class EncoderDifferentialDriveLocationEstimator implements ITranslationalLocationEstimator, IRotationalLocationEstimator
{
    Vector2f location;
    float heading = 0;
    float angularVel = 0;

    public void initialize(){
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

    public EncoderDifferentialDriveLocationEstimator()
    {

    }

    @Override
    public Vector2f estimateLocation()
    {
        // How many time passed
        float dTime = getDTime();

//        System.out.printf("dTime: %.2f\n",dTime);

        // talon inversed
        float leftRevPerS = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0) *10F / RAW_UNIT_PER_ROT;

        float rightRevPerS = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0)*10F / RAW_UNIT_PER_ROT;

        float leftVel = leftRevPerS * Robot.Physical.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        float rightVel =  rightRevPerS * Robot.Physical.WHEEL_DIAMETER_FT * MathUtils.PI_F;

        angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE);



        System.out.printf("Left: %.2f Right: %.2f\n",leftVel,rightVel);

//        Log.debug("wheel vels: L: {0,number,#.###} \t\t R: {1,number,#.###}", leftVel, rightVel);

        Vector2f absoluteDPos = MathUtils.Kinematics.getAbsoluteDPos(
                leftVel, rightVel, Robot.LATERAL_WHEEL_DISTANCE, dTime
                , heading);
        // Log.debug("adpp: " + absoluteDPos);
        Vector2f absLoc = location.add(absoluteDPos);
        // float angle = (float) Robot.NAVX.getYaw();
        heading+= angularVel*dTime;
        System.out.printf("absLoc: %.2f, %.2f\n",absLoc.x,absLoc.y);
        System.out.printf("heading: %.2f\n",heading);
        System.out.println();
        return absLoc;
    }

    @Override
    public float estimateHeading()
    {
        return heading;
    }
}
