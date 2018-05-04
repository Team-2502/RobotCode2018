import com.team2502.robot2018.pathplanning.purepursuit.ITankRobotBounds;
import org.joml.ImmutableVector2f;

public class SimulatedRobot implements ITankRobotBounds
{

    public static final float MAX_VEL = 16F;
    public static final float VOLTAGE_CHANGE_MAX = .02F;
    public static final float LATERAL_WHEEL_DIST = 2F;

    private final float lateralWheelDistance;

    private float leftMotorPercentVoltage = 0;
    private float rightMotorPercentVoltage = 0;

    public SimulatedRobot(float lateralWheelDistance)
    {
        this.lateralWheelDistance = lateralWheelDistance;
    }

    public float getLateralWheelDistance()
    {
        return lateralWheelDistance;
    }

    /**
     * Run motors at a certain velocity
     *
     * @param leftVel
     * @param rightVel
     */
    public void runMotorsVel(float leftVel, float rightVel)
    {
        leftMotorPercentVoltage = runMotorVel(leftVel, leftMotorPercentVoltage);
        rightMotorPercentVoltage = runMotorVel(rightVel, rightMotorPercentVoltage);
    }

    /**
     * @return Get velocity given current status
     */
    public float getLeftVel()
    {
        return leftMotorPercentVoltage * MAX_VEL; //TODO
    }

    /**
     * @return Get velocity given current status
     */
    public float getRightVel()
    {
        return rightMotorPercentVoltage * MAX_VEL; //TODO
    }

    private float runMotorVel(float velocity, float currentVoltage)
    {
        float percentVoltage = velocity / MAX_VEL;
        if(percentVoltage > 1)
        {
            percentVoltage = 1;
        }
        else if(percentVoltage < -1)
        {
            percentVoltage = -1;
        }
        return runMotorVoltage(percentVoltage, currentVoltage);
    }

    private float runMotorVoltage(float percentVoltage, float currentVoltage)
    {
        float dif = percentVoltage - currentVoltage;
        if(dif < -VOLTAGE_CHANGE_MAX)
        {
            percentVoltage = currentVoltage - VOLTAGE_CHANGE_MAX;
        }
        else if(dif > VOLTAGE_CHANGE_MAX)
        {
            percentVoltage = currentVoltage + VOLTAGE_CHANGE_MAX;
        }
        return percentVoltage;
    }
}
