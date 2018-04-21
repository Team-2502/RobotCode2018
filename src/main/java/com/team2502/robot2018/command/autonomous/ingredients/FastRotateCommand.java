package com.team2502.robot2018.command.autonomous.ingredients;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FastRotateCommand extends Command
{

    public static final float TOLERANCE_DEG = 3;
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
        // between 0 and 360
        float currentAngle = MathUtils.Kinematics.navXBound(Robot.NAVX.getYaw());

        // v_f^2 = v_i^2 + 2ad
        // -2ad = v_i^2
        // sqrt(-2ad) = v_i
        dAngle = MathUtils.Geometry.getDAngle(currentAngle, angleFinal);

        float dAnglePercent = dAngle / 180;
//        float calcVel = (float) Math.sqrt(-2 * maxDeccel * dAnglePercent);
        float calcVel = maxWheelVel * dAnglePercent;
//        float maxVelocity = (float) Math.min(calcVel, maxWheelVel);
        float maxVelocity = (float) Math.sqrt(calcVel);
        if(ccw)
        {
            Robot.DRIVE_TRAIN.runMotorsVelocity(-maxVelocity, maxVelocity);
        }
        else
        {
            Robot.DRIVE_TRAIN.runMotorsVelocity(maxVelocity, -maxVelocity);
        }
        SmartDashboard.putNumber("FastRotate: calcVel", calcVel);
        SmartDashboard.putNumber("FastRotate: dAnglePercent", dAngle);
        SmartDashboard.putNumber("FastRotate: maxVelocity", maxVelocity);
    }

    @Override
    protected boolean isFinished()
    {
        System.out.println("dAngle: " + dAngle);
        return dAngle <= TOLERANCE_DEG;
    }

}
