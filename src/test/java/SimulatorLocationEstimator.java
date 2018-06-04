import com.team2502.robot2018.pathplanning.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.pathplanning.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.pathplanning.localization.ITranslationalVelocityEstimator;
import com.team2502.robot2018.utils.IStopwatch;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.SimulatedStopwatch;
import org.joml.ImmutableVector2f;

//TODO: WIP
public class SimulatorLocationEstimator implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator, IRotationalLocationEstimator
{

    private final SimulatedRobot simulatedRobot;

    private float heading = 0F;
    private ImmutableVector2f estimatedLocation = new ImmutableVector2f();
    private ImmutableVector2f velocity = new ImmutableVector2f();
    private IStopwatch stopwatch;

    public SimulatorLocationEstimator(SimulatedRobot simulatedRobot)
    {
        this.simulatedRobot = simulatedRobot;
        this.stopwatch = new SimulatedStopwatch(0.02F); // each 20ms
    }

    public SimulatorLocationEstimator(SimulatedRobot simulatedRobot, float dt)
    {
        this.simulatedRobot = simulatedRobot;
        this.stopwatch = new SimulatedStopwatch(dt); // each 20ms
    }

    public void setEstimatedLocation(ImmutableVector2f estimatedLocation)
    {
        this.estimatedLocation = estimatedLocation;
    }

    public void update()
    {
        float time = stopwatch.pop();
        float leftVel = simulatedRobot.getLeftVel();
        float rightVel = simulatedRobot.getRightVel();
        velocity = MathUtils.LinearAlgebra.rotate2D(new ImmutableVector2f(leftVel, rightVel), heading);
        float angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, SimulatedRobot.LATERAL_WHEEL_DIST);
        heading += angularVel * time;
        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosCurve(leftVel, rightVel, simulatedRobot.getLateralWheelDistance(), time, heading);
        if(Double.isNaN(dPos.x) || Double.isNaN(dPos.y))
        {
            throw new NullPointerException("dPos is NaN!");
        }
        estimatedLocation = estimatedLocation.add(dPos);
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
        return simulatedRobot.getLeftVel();
    }

    @Override
    public float getRightWheelSpeed()
    {
        return simulatedRobot.getRightVel();
    }

    @Override
    public float estimateSpeed()
    {
        return (simulatedRobot.getLeftVel() + simulatedRobot.getRightVel()) / 2F;
    }
}
