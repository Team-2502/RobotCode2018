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
    private final TankRobot tankRobot;
    public float lookAheadDistance;
    public static final float TAU = 2*3.1415F;
    private DriveTrainSubsystem driveTrain;
    private AHRS navx;
    private PurePursuitMovementStrategy purePursuitMovementStrategy;
    private long lastTime = -1;
    private float initAngleDegrees;
    private float initAngleRadians;

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
        navx = Robot.NAVX;
        navx.resetDisplacement();
        initAngleDegrees = (float) navx.getAngle();
        initAngleRadians = initAngleDegrees / 180F * TAU;
        this.lookAheadDistance = lookAheadDistance;
        requires(Robot.DRIVE_TRAIN);
        driveTrain = Robot.DRIVE_TRAIN;

        tankRobot = new TankRobot()
        {
            @Override
            public float getHeading()
            {
                double radians = (navx.getAngle() - initAngleDegrees) / 180F * TAU;
                return (float) - (radians % TAU);
            }

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

        LocationEstimator locationEstimator = () ->
        {
            // How many 100 ms intervals occured
            double dTime = getDTime()/10F;

            // talon inversed
            float leftVel = -Robot.DRIVE_TRAIN.leftRearTalonEnc.getSelectedSensorVelocity(0);

            float rightVel = Robot.DRIVE_TRAIN.rightRearTalonEnc.getSelectedSensorVelocity(0);

            float rotVelocity = (leftVel-rightVel)/tankRobot.getLateralWheelDistance();

            Function<Float,Vector> estimatePositionFromDTheta = dTheta -> {
                float dxRelative = -purePursuitMovementStrategy.getPathRadius() * (1- MathUtils.cos(-dTheta));
                float dyRelative = -purePursuitMovementStrategy.getPathRadius() * MathUtils.sin(-dTheta);

                Vector dRelativeVector = new Vector(dxRelative, dyRelative);
                Vector rotated = MathUtils.LinearAlgebra.rotate2D(dRelativeVector, purePursuitMovementStrategy.getUsedHeading());
                Vector toReturn = rotated.add(purePursuitMovementStrategy.getUsedEstimatedLocation()); //
                return toReturn;


            };

            Function<Double,Vector> dTimeToPosition = dTime1 -> {
                double dTheta =  dTime1 * rotVelocity; // purePursuitMovementStrategy.getRotVelocity();
                return estimatePositionFromDTheta.apply( (float) dTheta); //
            };
            return dTimeToPosition.apply(dTime);
        };


        purePursuitMovementStrategy = new PurePursuitMovementStrategy(tankRobot, locationEstimator, waypoints, lookAheadDistance);
    }

    @Override
    protected void initialize()
    {
        System.out.println("NavX initial angle"+navx.getAngle());
//        driveTrain.setAutonSettings();
    }

    @Override
    protected void execute()
    {
        purePursuitMovementStrategy.update();
        Vector wheelVelocities = purePursuitMovementStrategy.getWheelVelocities();
        float x = wheelVelocities.get(0);
        float y = wheelVelocities.get(1);
        System.out.println("Wheel velocities: "+x+" ::: "+y+" ::: Wheel velocities");
        System.out.println("Unmoded Loc: "+navx.getDisplacementX()+","+navx.getDisplacementY());
        System.out.println("Heading: "+tankRobot.getHeading());
        driveTrain.runMotors(x, y);
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
