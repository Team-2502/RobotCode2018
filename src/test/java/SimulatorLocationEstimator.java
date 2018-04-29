import com.team2502.robot2018.pathplanning.localization.IRotationalLocationEstimator;
import com.team2502.robot2018.pathplanning.localization.ITranslationalLocationEstimator;
import com.team2502.robot2018.pathplanning.localization.ITranslationalVelocityEstimator;
import com.team2502.robot2018.utils.MathUtils;
import com.team2502.robot2018.utils.Stopwatch;
import org.joml.ImmutableVector2f;

//TODO: WIP
public class SimulatorLocationEstimator implements ITranslationalLocationEstimator, ITranslationalVelocityEstimator, IRotationalLocationEstimator
{

    private final SimulatedRobot simulatedRobot;

    private float heading = 0;
    private ImmutableVector2f location = new ImmutableVector2f();
    private ImmutableVector2f velocity = new ImmutableVector2f();
    private Stopwatch stopwatch;

    public SimulatorLocationEstimator(SimulatedRobot simulatedRobot)
    {
        this.simulatedRobot = simulatedRobot;
        stopwatch = new Stopwatch();
    }

    public void update()
    {
        float time = stopwatch.pop();
        float leftVel = simulatedRobot.getLeftVel();
        float rightVel = simulatedRobot.getRightVel();
        float angularVel = MathUtils.Kinematics.getAngularVel(leftVel, rightVel, SimulatedRobot.LATERAL_WHEEL_DIST);
        heading+=angularVel*time;
        MathUtils.Kinematics.getAbsoluteDPosCurve(leftVel,rightVel,simulatedRobot.getLateralWheelDistance(),time,heading);
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
        return 0;
    }

    @Override
    public float getRightWheelSpeed()
    {
        return 0;
    }

    @Override
    public float estimateSpeed()
    {
        return 0;
    }
}
