package com.team2502.robot2018;

import com.team2502.robot2018.trajectory.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.trajectory.localization.ITranslationalVelocityEstimator;
import edu.wpi.first.wpilibj.command.Command;
import org.joml.ImmutableVector2f;


/**
<<<<<<< HEAD
 * A command that runs 24/7 to calculate and cache the state of the robot (x,y,heading,dx,dv...).
=======
 * An uninterruptable command that runs 24/7 (even when disabled) to calculate and cache the state of the robot (x,y,heading,dx,dv...).
>>>>>>> develop
 *
 * @deprecated Should be made into a Thread running at a period of <i>x</i> ms. However, this needs to be tested.
 */
public class RobotLocalizationCommand extends Command implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator, IRotationalLocationEstimator
{
    /**
     * A lambda that estimates the robot's rotation
     */
    private final IRotationalLocationEstimator rotEstimator;

    /**
     * A lambda that estimates the robot's velocity
     */
    private final ITranslationalVelocityEstimator velocityEstimator;

    /**
     * A lambda that estimates the robot's location
     */
    private final ITranslationalLocationEstimator locationEstimator;

//    /**
//     * How often to run (in ms)
//     */
    //    private final long msPeriod;

    // self-explanatory.
    // These variables store info about the robot's velocity, position, and heading
    private float heading, leftWheelSpeed, rightWheelSpeed, speed;
    private ImmutableVector2f location, velocity;

    /**
     * Begin Localization
     *
     * @param rotEstimator      Heading estimator - Grabs the heading from the NavX
     * @param velocityEstimator Velocity estimator - Grabs the velocity from the encoders
     * @param locationEstimator Location estimator - Grabs the estimated location from some kinematics equations
     */
    public RobotLocalizationCommand(IRotationalLocationEstimator rotEstimator,
                                    ITranslationalVelocityEstimator velocityEstimator, ITranslationalLocationEstimator locationEstimator)
    {
        setRunWhenDisabled(true);
        setInterruptible(false);
        this.rotEstimator = rotEstimator;
        this.velocityEstimator = velocityEstimator;
        this.locationEstimator = locationEstimator;
    }

    /**
     * Runs continually during autonomous.
     */
    @Override
    protected void execute()
    {
        // Update estimated heading
        heading = rotEstimator.estimateHeading();

        // Update wheel speeds
        leftWheelSpeed = velocityEstimator.getLeftWheelSpeed();
        rightWheelSpeed = velocityEstimator.getRightWheelSpeed();

        // Estimate location
        location = locationEstimator.estimateLocation();
        System.out.printf("locationX %.2f, locationY %.2f\n", location.x, location.y);

        // Find our heading in vector form
        velocity = velocityEstimator.estimateAbsoluteVelocity();

        // Find our speed
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

    /**
     * Never finished - runs continuously
     *
     * @return false
     */
    @Override
    protected boolean isFinished()
    {
        return false;
    }

    /**
     * @return estimated heading
     */
    @Override
    public float estimateHeading()
    {
        return heading;
    }

    /**
     * @return absolute location
     */
    @Override
    public ImmutableVector2f estimateLocation()
    {
        return location;
    }

    /**
     * @return Unit vector pointing in the direction of our heading
     */
    @Override
    public ImmutableVector2f estimateAbsoluteVelocity()
    {
        return velocity;
    }

    /**
     * @return speed of left wheel (ft/s)
     */
    @Override
    public float getLeftWheelSpeed()
    {
        return leftWheelSpeed;
    }

    /**
     * @return speed of right wheel (ft/s)
     */
    @Override
    public float getRightWheelSpeed()
    {
        return rightWheelSpeed;
    }

    /**
     * More often than not, our robot is moving along a curve. <br>
     * At any given time, the robot's velocity is tangent to the curve on which it is driving.<br>
     * (Imagine a dot moving in a circular path - if you freed it from the circle, it would continue in a path tangent to the circle)<br>
     * The robot's tangential speed is the robot's speed tangent to the curve.<br>
     *
     * @return tangential speed
     */
    @Override
    public float estimateSpeed()
    {
        return speed;
    }
}
