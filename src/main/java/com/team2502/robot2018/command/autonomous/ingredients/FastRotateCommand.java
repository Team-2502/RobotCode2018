package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;

public class FastRotateCommand extends Command
{

    public static final float TOLERANCE_DEG = 5;
    private final float angleFinal;
    private final float maxWheelVel;
    //    private final float maxAccel;
    private final float maxDeccel;
    private float angleInit;
    private boolean ccw;
    private float dAngle;

    /**
     * @param angleFinal
     * @param maxWheelVel
     * @param maxDeccel   Really weird units... in wheel deceleration / degrees.
     */
    public FastRotateCommand(float angleFinal, float maxWheelVel, float maxDeccel)
    {
        requires(Robot.DRIVE_TRAIN);
        this.angleFinal = MathUtils.Kinematics.navXBound(angleFinal);
        this.maxWheelVel = maxWheelVel;
        this.maxDeccel = maxDeccel;
    }

    @Override
    protected void initialize()
    {
        angleInit = MathUtils.Kinematics.navXBound((float) Robot.NAVX.getAngle());

        ccw = MathUtils.Geometry.isCCWQuickest(angleInit, angleFinal);
    }

    @Override
    protected void execute()
    {
        float currentAngle = MathUtils.Kinematics.navXBound((float) Robot.NAVX.getYaw());
        // v_f^2 = v_i^2 + 2ad
        // -2ad = v_i^2
        // sqrt(-2ad) = v_i
        dAngle = MathUtils.Geometry.getDAngle(currentAngle, angleFinal);
        float maxVelocity = (float) Math.min(Math.sqrt(-2 * maxDeccel * dAngle), maxWheelVel);
        if(ccw)
        {
            Robot.DRIVE_TRAIN.runMotorsVelocity(-maxVelocity, maxVelocity);
        }
        else
        {
            Robot.DRIVE_TRAIN.runMotorsVelocity(maxVelocity, -maxVelocity);
        }
    }

    @Override
    protected boolean isFinished()
    {
        System.out.println("dAngle: " + dAngle);
        return dAngle <= TOLERANCE_DEG;
    }

}
