package com.team2502.robot2018.pathplanning.localization;

import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.utils.IStopwatch;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.RealStopwatch;
import edu.wpi.first.wpilibj.command.Command;
import org.joml.ImmutableVector2f;

//TODO: WIP
public class PlsWorkLocalizer2 extends Command implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator, IRotationalLocationEstimator
{

    private float heading = 0F;
    private ImmutableVector2f estimatedLocation = new ImmutableVector2f();
    private ImmutableVector2f velocity = new ImmutableVector2f();
    private IStopwatch stopwatch;

    public PlsWorkLocalizer2()
    {
        this.stopwatch = new RealStopwatch(); // each 20ms
    }

    public void setEstimatedLocation(ImmutableVector2f estimatedLocation)
    {
        this.estimatedLocation = estimatedLocation;
    }

    @Override
    public void execute()
    {
        float time = stopwatch.pop();
        float leftVel = Robot.DRIVE_TRAIN.getLeftVel();
        float rightVel = Robot.DRIVE_TRAIN.getRightVel();
        velocity = MathUtils.LinearAlgebra.rotate2D(new ImmutableVector2f(leftVel, rightVel), heading);
        float angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, 1);
        this.heading = (float) MathUtils.Kinematics.navXToRad(Robot.NAVX.getAngle());
//        System.out.printf("heading: %.2f, estimatedLocation: (%.2f, %.2f) leftVel: %.2f, rightVel: %.2f\n",
//                          heading,estimatedLocation.x,estimatedLocation.y,leftVel,rightVel);
//        System.out.println("heading =" + heading);
//        System.out.println("estimatedLocation = " + estimatedLocation);
//        System.out.println("leftVel = " + leftVel);
//        System.out.println("rightVel = " + rightVel);

        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosCurve(leftVel, rightVel, 1, time, this.heading);
        estimatedLocation = estimatedLocation.add(dPos);
    }

    @Override
    protected boolean isFinished()
    {
        return false;
    }

    @Override
    public float estimateHeading()
    {
        return heading;
    }

    @Override
    public ImmutableVector2f estimateLocation()
    {
        return estimatedLocation;
    }

    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        return velocity;
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

