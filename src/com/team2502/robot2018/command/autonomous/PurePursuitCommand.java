package com.team2502.robot2018.command.autonomous;

import com.kauailabs.navx.frc.AHRS;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.data.Vector;
import com.team2502.robot2018.subsystem.DriveTrainSubsystem;
import com.team2502.robot2018.trajectory.LocationEstimator;
import com.team2502.robot2018.trajectory.PurePursuitMovementStrategy;
import com.team2502.robot2018.trajectory.TankRobot;
import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;

import java.util.List;
import java.util.function.Function;

public class PurePursuitCommand extends Command
{
    public float lookAheadDistance;
    private DriveTrainSubsystem driveTrain;
    private AHRS navx;
    private PurePursuitMovementStrategy purePursuitMovementStrategy;
    private long lastTime = -1;

    /**
     * @return difference in seconds since last time the method was called
     */
    double getDTime()
    {
        long nanoTime = System.nanoTime();
        double dTime;
        dTime = lastTime == -1 ? 0 : nanoTime - lastTime;
        lastTime = nanoTime;
        return (dTime / 1E6);
    }

    public PurePursuitCommand(List<Vector> waypoints, float lookAheadDistance)
    {
        this.lookAheadDistance = lookAheadDistance;
        requires(Robot.DRIVE_TRAIN);
        driveTrain = Robot.DRIVE_TRAIN;
        navx = Robot.NAVX;

        TankRobot tankRobot = new TankRobot()
        {
            @Override
            public float getHeading()
            { return (float) navx.getAngle(); }

            @Override
            public float getV_rMax()
            { return Robot.VR_MAX; }

            @Override
            public float getV_lMax()
            { return Robot.VL_MAX; }

            @Override
            public float getV_lMin()
            { return Robot.VL_MIN; }

            @Override
            public float getV_rMin()
            { return Robot.VR_MIN; }

            @Override
            public float getLateralWheelDistance()
            { return Robot.LATERAL_WHEEL_DISTANCE; }
        };

//        LocationEstimator locationEstimator = () -> new Vector(navx.getDisplacementX(), navx.getDisplacementY());

        // Not used: reason encoders do not yet setup.


        LocationEstimator locationEstimator = () ->
        {
            double dTime = getDTime();
            Function<Float,Vector> estimatePositionFromDTheta = dTheta -> {
                float dxRelative = -purePursuitMovementStrategy.getPathRadius() * (1- (float) Math.cos(-dTheta));
                float dyRelative = -purePursuitMovementStrategy.getPathRadius() * (float) Math.sin(-dTheta);

                Vector dRelativeVector = new Vector(dxRelative, dyRelative);
                Vector rotated = MathUtils.LinearAlgebra.rotate2D(dRelativeVector, purePursuitMovementStrategy.getUsedHeading());
                Vector toReturn = rotated.add(purePursuitMovementStrategy.getUsedEstimatedLocation()); //
                return toReturn;


            };

            Function<Double,Vector> dTimeToPosition = dTime1 -> {
                double dTheta =  dTime1 * purePursuitMovementStrategy.getRotVelocity();
                return estimatePositionFromDTheta.apply( (float) dTheta); //
            };
            return dTimeToPosition.apply(dTime); //
        };


        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, locationEstimator, waypoints, lookAheadDistance);
    }

    @Override
    protected void initialize()
    {
        navx.resetDisplacement();
        driveTrain.setAutonSettings();
    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();
        Vector wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();
        driveTrain.runMotors(-wheelVelocities.get(0), -wheelVelocities.get(1));
    }

    @Override
    protected boolean isFinished()
    {
        return purePursuitMovementStrategy.isFinishedPath();

    }

    @Override
    protected void end()
    {
        driveTrain.setTeleopSettings();
    }
}
