import org.joml.ImmutableVector2f;

//TODO: WIP
public class SimulatedRobot
{

    public static final float MAX_VEL = 16F;
    public static final float VOLTAGE_CHANGE_MAX = .1F;
    public static final float LATERAL_WHEEL_DIST = 2F;

    ImmutableVector2f position;
    float heading;

    private float leftMotorPercentVoltage = 0;
    private float rightMotorPercentVoltage = 0;

    public SimulatedRobot()
    {

    }

    public void update()
    {

    }
    /**
     * Run motors at a certain velocity
     * @param leftVel
     * @param rightVel
     */
    public void runMotorsVel(float leftVel, float rightVel)
    {
        leftMotorPercentVoltage = runMotorVel(leftVel, leftMotorPercentVoltage);
        rightMotorPercentVoltage = runMotorVel(rightVel, rightMotorPercentVoltage);
    }

    /**
     *
     * @return Get velocity given current status
     */
    public float getLeftVel()
    {
        return 0; //TODO
    }

    /**
     *
     * @return Get velocity given current status
     */
    public float getRightVel()
    {
        return 0; //TODO
    }

    private float runMotorVel(float velocity, float currentVoltage)
    {
        float percentVoltage = velocity / MAX_VEL;
        if(percentVoltage > 1)
        {
            percentVoltage = 1;
        }
        else if (percentVoltage < -1)
        {
            percentVoltage = -1;
        }
        return runMotorVoltage(percentVoltage, currentVoltage);
    }

    private float runMotorVoltage(float percentVoltage, float currentVoltage)
    {
        if(currentVoltage - percentVoltage < -VOLTAGE_CHANGE_MAX)
        {
            percentVoltage=currentVoltage-VOLTAGE_CHANGE_MAX;
        }
        else if(currentVoltage - percentVoltage > VOLTAGE_CHANGE_MAX)
        {
            percentVoltage=currentVoltage+VOLTAGE_CHANGE_MAX;
        }
        return percentVoltage;

    }
}
