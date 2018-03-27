import org.joml.ImmutableVector2f;

public class SimulatedRobot
{

    public static final float MAX_VEL = 16F;
    public static final float VOLTAGE_CHANGE_MAX = .1F;

    ImmutableVector2f position;
    float heading;

    private float leftMotorPercentVoltage = 0;
    private float rightMotorPercentVoltage = 0;

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
    public ImmutableVector2f getVelocity()
    {
        return new ImmutableVector2f(leftMotorPercentVoltage*MAX_VEL,rightMotorPercentVoltage*MAX_VEL);
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
