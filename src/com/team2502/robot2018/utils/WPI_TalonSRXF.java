package com.team2502.robot2018.utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.MotControllerJNI;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@Deprecated
public class WPI_TalonSRXF extends TalonSRX implements ISpeedControllerF, Sendable, MotorSafety
{
    protected String m_description;
    protected float m_speed;
    protected MotorSafetyHelper m_safetyHelper;
    protected ControlMode m_controlMode = ControlMode.PercentOutput;

    /**
     * Constructor
     */
    public WPI_TalonSRXF(int deviceNumber)
    {
        super(deviceNumber);
        HAL.report(66, deviceNumber + 1);
        m_description = "Talon SRX " + deviceNumber;
        /* prep motor safety */
        m_safetyHelper = new MotorSafetyHelper(this);
        m_safetyHelper.setExpiration(0.0);
        m_safetyHelper.setSafetyEnabled(false);

        LiveWindow.add(this);
        setName("Talon SRX ", deviceNumber);
    }

    // ------ set/get routines for WPILIB interfaces ------//
    @Override
    public void set(double speed)
    {
        m_speed = (float) speed;
        set(ControlMode.PercentOutput, m_speed);
        m_safetyHelper.feed();
    }

    public void set(float speed)
    {
        m_speed = speed;
        set(ControlMode.PercentOutput, m_speed);
        m_safetyHelper.feed();
    }

    @Override
    public void pidWrite(double output)
    { set(output); }

    public void pidWrite(float output)
    { set(output); }

    /**
     * Common interface for getting the current set speed of a speed controller.
     *
     * @return The current set speed. Value is between -1.0 and 1.0.
     */
    @Override
    public double get()
    { return m_speed; }

    /**
     * Common interface for getting the current set speed of a speed controller.
     *
     * @return The current set speed. Value is between -1.0 and 1.0.
     */
    public float getF()
    { return m_speed; }

    // ---------Intercept CTRE calls for motor safety ---------//
    @Override
    public void set(ControlMode mode, double value)
    {
        /* intercept the advanced Set and feed motor-safety */
        super.set(mode, value);
        m_safetyHelper.feed();
    }

    public void set(ControlMode mode, float value)
    {
        /* intercept the advanced Set and feed motor-safety */
        set(mode, value, 0.0F);
        m_safetyHelper.feed();
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void set(ControlMode mode, double demand0, double demand1)
    {
        m_controlMode = mode;
        int work;

        switch(m_controlMode)
        {
            case PercentOutput:
                // case TimedPercentOutput:
                MotControllerJNI.SetDemand(m_handle, mode.value, (int) (1023 * demand0), 0);
                break;
            case Follower:
                /* did caller specify device ID */
                if((0 <= demand0) && (demand0 <= 62))
                { // [0,62]
                    work = getBaseID();
                    work >>= 16;
                    work <<= 8;
                    work |= ((int) demand0) & 0xFF;
                }
                else
                {
                    work = (int) demand0;
                }
                MotControllerJNI.SetDemand(m_handle, mode.value, work, 0);
                break;
            case Velocity:
            case Position:
            case MotionMagic:
            case MotionMagicArc:
            case MotionProfile:
                MotControllerJNI.SetDemand(m_handle, mode.value, (int) demand0, 0);
                break;
            case Current:
                MotControllerJNI.SetDemand(m_handle, mode.value, (int) (1000. * demand0), 0); /* milliamps */
                break;
            case Disabled:
                /* fall thru... */
            default:
                MotControllerJNI.SetDemand(m_handle, mode.value, 0, 0);
                break;
        }
        m_safetyHelper.feed();

    }

    @SuppressWarnings("Duplicates")
    public void set(ControlMode mode, float demand0, float demand1)
    {
        m_controlMode = mode;
        int work;

        switch(mode)
        {
            case PercentOutput:
                // case TimedPercentOutput:
                MotControllerJNI.SetDemand(m_handle, mode.value, (int) (1023 * demand0), 0);
                break;
            case Follower:
                /* did caller specify device ID */
                if((0 <= demand0) && (demand0 <= 62))
                { // [0,62]
                    work = getBaseID();
                    work >>= 16;
                    work <<= 8;
                    work |= ((int) demand0) & 0xFF;
                }
                else
                {
                    work = (int) demand0;
                }
                MotControllerJNI.SetDemand(m_handle, mode.value, work, 0);
                break;
            case Velocity:
            case Position:
            case MotionMagic:
            case MotionMagicArc:
            case MotionProfile:
                MotControllerJNI.SetDemand(m_handle, mode.value, (int) demand0, 0);
                break;
            case Current:
                MotControllerJNI.SetDemand(m_handle, mode.value, (int) (1000. * demand0), 0); /* milliamps */
                break;
            case Disabled:
                /* fall thru... */
            default:
                MotControllerJNI.SetDemand(m_handle, mode.value, 0, 0);
                break;
        }
        m_safetyHelper.feed();
    }

    // ----------------------- Invert routines -------------------//
    @Override
    public void setInverted(boolean isInverted)
    {
        super.setInverted(isInverted);
    }

    @Override
    public boolean getInverted()
    {
        return super.getInverted();
    }

    // ----------------------- turn-motor-off routines-------------------//
    @Override
    public void disable()
    {
        neutralOutput();
    }

    /**
     * Common interface to stop the motor until Set is called again.
     */
    @Override
    public void stopMotor()
    {
        neutralOutput();
    }
    // -------- Motor Safety--------//

    /**
     * Set the safety expiration time.
     *
     * @param timeout The timeout (in seconds) for this motor object
     */
    @Override
    public void setExpiration(double timeout)
    {
        m_safetyHelper.setExpiration(timeout);
    }

    /**
     * Return the safety expiration time.
     *
     * @return The expiration time value.
     */
    @Override
    public double getExpiration()
    {
        return m_safetyHelper.getExpiration();
    }

    /**
     * Check if the motor is currently alive or stopped due to a timeout.
     *
     * @return a bool value that is true if the motor has NOT timed out and
     * should still be running.
     */
    @Override
    public boolean isAlive()
    {
        return m_safetyHelper.isAlive();
    }

    /**
     * Check if motor safety is enabled.
     *
     * @return True if motor safety is enforced for this object
     */
    @Override
    public boolean isSafetyEnabled()
    {
        return m_safetyHelper.isSafetyEnabled();
    }

    @Override
    public void setSafetyEnabled(boolean enabled)
    {
        m_safetyHelper.setSafetyEnabled(enabled);
    }

    // ---- essentially a copy of SendableBase -------//
    private String m_name = "";
    private String m_subsystem = "Ungrouped";

    /**
     * Free the resources used by this object.
     */
    public void free()
    {
        LiveWindow.remove(this);
    }

    @Override
    public final synchronized String getName()
    {
        return m_name;
    }

    @Override
    public final synchronized void setName(String name)
    {
        m_name = name;
    }

    /**
     * Sets the name of the sensor with a channel number.
     *
     * @param moduleType A string that defines the module name in the label for the
     *                   value
     * @param channel    The channel number the device is plugged into
     */
    protected final void setName(String moduleType, int channel)
    {
        setName(moduleType + "[" + channel + "]");
    }

    /**
     * Sets the name of the sensor with a module and channel number.
     *
     * @param moduleType   A string that defines the module name in the label for the
     *                     value
     * @param moduleNumber The number of the particular module type
     * @param channel      The channel number the device is plugged into (usually PWM)
     */
    protected final void setName(String moduleType, int moduleNumber, int channel)
    {
        setName(moduleType + "[" + moduleNumber + "," + channel + "]");
    }

    @Override
    public final synchronized String getSubsystem()
    {
        return m_subsystem;
    }

    @Override
    public final synchronized void setSubsystem(String subsystem)
    {
        m_subsystem = subsystem;
    }

    /**
     * Add a child component.
     *
     * @param child child component
     */
    protected final void addChild(Object child)
    {
        LiveWindow.addChild(this, child);
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.setSmartDashboardType("Speed Controller");
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Value", this::get, this::set);
    }

    @Override
    public String getDescription()
    {
        return m_description;
    }

    @Override
    public ControlMode getControlMode()
    {
        return m_controlMode;
    }
}
