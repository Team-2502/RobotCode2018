package com.team2502.robot2018.command.autonomous.ingredients;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import org.joml.ImmutableVector2f;

/**
 * Turn to a certain angle with the NavX. Is also a good example of using custom PID control
 *
 * @author Ritik Mishr, 2017
 */
public class NavXRotateCommand extends Command implements PIDOutput
{
    private float targetYaw;
    private DriveTrainSubsystem driveTrain;
    private AHRS navx;
    private float turnRate = 0;
    private PIDController turnController;
    private long alignedTime;
    private boolean onTarget = false;
    private boolean reset = true;

    /**
     * Drive in a straight line for 5 seconds according to the navx.
     */
    private NavXRotateCommand(double time)
    {
        super(time);
        requires(Robot.DRIVE_TRAIN);
        driveTrain = Robot.DRIVE_TRAIN;
        navx = Robot.NAVX;

        turnController = new PIDController(0.02, 0.000007, 0, 0, navx, this);
        //.0225 , .0002, 0
        turnController.setInputRange(-180.0f, 180.0f);
        turnController.setOutputRange(-1.0, 1.0);
        turnController.setAbsoluteTolerance(1);
        turnController.setContinuous(true);
        turnController.disable();
    }

    /**
     * Turn to an angle, and immediately end once turned.
     *
     * @param angle the angle to turn to.
     */
    public NavXRotateCommand(float angle, float maxtime)
    {
        this(maxtime);
        targetYaw = angle;
    }

    /**
     * Turn to an angle, and immediately end once turned.
     *
     * @param angle the angle to turn to.
     */
    public NavXRotateCommand(float angle, float maxtime, boolean reset)
    {
        this(maxtime);
        targetYaw = angle;
        this.reset = reset;
    }

    public static NavXRotateCommand fromLocation(ImmutableVector2f now, ImmutableVector2f later, float maxTime)
    {
        // TODO: test if this is the right direction
        float theta = MathUtils.Geometry.getThetaFromPoints(now, later);
        float degrees = (float) Math.toDegrees(theta);
        return new NavXRotateCommand(degrees, maxTime);
    }


    @Override
    protected void initialize()
    {
        if(reset)
        {
            navx.reset();
        }


        if(!turnController.isEnabled())
        {
            turnController.setSetpoint(targetYaw);
            turnController.enable();
        }

        if(turnController.onTarget() && !onTarget)
        {
            onTarget = true;
            alignedTime = System.currentTimeMillis();

        }
    }

    @Override
    protected void execute()
    {
        turnRate = (float) turnController.get();
        System.out.println("turnRate: " + turnRate);
        driveTrain.runMotorsVoltage(turnRate, -turnRate);
    }

    @Override
    protected boolean isFinished()
    {
        return isTimedOut() || (onTarget && (System.currentTimeMillis() - alignedTime) >= 1000);
    }

    @Override
    protected void end() { driveTrain.stop(); }

    @Override
    protected void interrupted() { end(); }

    @Override
    public void pidWrite(double output) { turnRate = (float) output; }
}