package com.team2502.robot2018;

import com.team2502.robot2018.trajectory.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalVelocityEstimator;
import edu.wpi.first.wpilibj.command.Command;
import org.joml.ImmutableVector2f;


/**
 * A command that runs 24/7 to calculate and cache the state of the robot (x,y,heading,dx,dv...).
 *
 * @deprecated Should be made into a Thread running at a period of <i>x</i> ms. However, this needs to be tested.
 */
public class RobotLocalizationCommand extends Command implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator, IRotationalLocationEstimator
{
    private final IRotationalLocationEstimator rotEstimator;
    private final ITranslationalVelocityEstimator velocityEstimator;
    private final ITranslationalLocationEstimator locationEstimator;
    //    private final long msPeriod;
    private float heading, leftWheelSpeed, rightWheelSpeed, speed;
    private ImmutableVector2f location, velocity;

    public RobotLocalizationCommand(IRotationalLocationEstimator rotEstimator,
                                    ITranslationalVelocityEstimator velocityEstimator, ITranslationalLocationEstimator locationEstimator)
    {
        this.rotEstimator = rotEstimator;
        this.velocityEstimator = velocityEstimator;
        this.locationEstimator = locationEstimator;
    }

    @Override
    protected void execute()
    {
        heading = rotEstimator.estimateHeading();
        leftWheelSpeed = velocityEstimator.getLeftWheelSpeed();
        rightWheelSpeed = velocityEstimator.getRightWheelSpeed();
        location = locationEstimator.estimateLocation();
        System.out.printf("locationX %.2f, locationY %.2f\n", location.x, location.y);
        velocity = velocityEstimator.estimateAbsoluteVelocity();
        speed = velocityEstimator.estimateSpeed();
    }

    //    @Override
//    public void run()
//    {
//        System.out.println("running!!!! ");
//        while(!Thread.currentThread().isInterrupted())
//        {
//            heading = rotEstimator.estimateHeading();
//            leftWheelSpeed = velocityEstimator.getLeftWheelSpeed();
//            rightWheelSpeed = velocityEstimator.getRightWheelSpeed();
//            location = locationEstimator.estimateLocation();
//            System.out.printf("locationX %.2f, locationY %.2f", location.x, location.y);
//            velocity = velocityEstimator.estimateAbsoluteVelocity();
//            speed = velocityEstimator.estimateSpeed();
//            try
//            {
//                sleep(msPeriod);
//            }
//            catch(InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//
//        }
//    }

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
        return location;
    }

    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        return velocity;
    }

    @Override
    public float getLeftWheelSpeed()
    {
        return leftWheelSpeed;
    }

    @Override
    public float getRightWheelSpeed()
    {
        return rightWheelSpeed;
    }

    @Override
    public float estimateSpeed()
    {
        return speed;
    }
}
