package com.team2502.robot2018.utils.baseoverloads;

import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.hal.FRCNetComm;
import edu.wpi.first.wpilibj.hal.HAL;

/**
 * Handle input from standard Joysticks connected to the Driver Station.
 * <p>
 * <p>This class handles standard input that comes from the Driver Station. Each time a value is
 * requested the most recent value is returned. There is a single class instance for each joystick
 * and the mapping of ports to hardware buttons depends on the code in the Driver Station.
 * <p>
 * Reason for this class: Single Precision Floating Point Numbers.
 * <p>
 * A lot of the WPILib libraries use double precision floating point
 * numbers. This is a problem because the Cortex-A9 (the CPU that
 * the roboRIO uses) does not support native 64 bit operations.
 * <p>
 * What does it mean to have "native" 64 bit operations? It means that
 * the FPU (<b>F</b>loating <B>P</B>oint <b>U</b>nit) in the CPU has
 * 64 bit register and can do full 64 bit operations. So what does it
 * mean if it is not native? It means that the FPU only supports 32
 * bit register. When doing 64 bit operations on a non-native FPU
 * it will do two 32 bit operations and store the 64 bit value in
 * memory.<br>
 * <p>
 * How do we know that Cortex-A9 FPU doesn't support native 64 bit
 * operations? If you know x86 assembly and its floating point
 * operations then you probably know of the <code>cvtss2sd</code> and
 * <code>cvt2sdss</code> opcodes. In the ARM assembly instruction
 * set we have the <code>FCVT</code> opcode, this supports converting
 * to and from 16, 32, and 64 bit floating point numbers. If you read
 * the floating point spec for the Cortex-A9 you will find in the
 * <code>Instruction throughput and latency</code> section that it
 * mentions the cycle count of the <code>FCVT</code> opcode.
 * Underneath the opcode it has says two things: <code>.F16</code>
 * and <code>.F32</code>. You will also notice that it only shows
 * up in the single precision column. This leaves us with the
 * impression that it not be able to convert to a 64 bit number
 * implying that it may not natively support 64 bit operations.
 * <p>
 * Can we find supplemental evidence supporting our hypothesis?
 * In fact we can, according to a
 * <a href="http://nicolas.limare.net/pro/notes/2014/12/12_arit_speed/#index3h2">benchmark posted online</a>
 * which includes benchmarks for the Cortex-A9 we can see that when doing
 * single and double precision floating point operations the single
 * precision operations perform at twice the speed of the double
 * precision operations. While not entirely conclusive this leaves us
 * impression that the FPU does not natively support 64 bit operations.
 * And even if it did this shows that single precision will out
 * perform double precision.
 * <p>
 * The default Joystick classes return doubles for the joystick
 * positions. But the value eventually delegates back to a float
 * array. So why not just make the functions return floats.
 */
@Deprecated
public class JoystickF extends GenericHIDF
{
    static final byte kDefaultXAxis = 0;
    static final byte kDefaultYAxis = 1;
    static final byte kDefaultZAxis = 2;
    static final byte kDefaultTwistAxis = 2;
    static final byte kDefaultThrottleAxis = 3;
    private final byte[] m_axes = new byte[Axis.kNumAxes.value];

    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port The port on the Driver Station that the joystick is plugged into.
     */
    public JoystickF(final int port)
    {
        super(port);

        m_axes[Axis.kX.value] = kDefaultXAxis;
        m_axes[Axis.kY.value] = kDefaultYAxis;
        m_axes[Axis.kZ.value] = kDefaultZAxis;
        m_axes[Axis.kTwist.value] = kDefaultTwistAxis;
        m_axes[Axis.kThrottle.value] = kDefaultThrottleAxis;

        HAL.report(FRCNetComm.tResourceType.kResourceType_Joystick, port);
    }

    /**
     * Set the channel associated with a specified axis.
     *
     * @param axis    The axis to set the channel for.
     * @param channel The channel to set the axis to.
     * @deprecated Use the more specific axis channel setter functions.
     */
    @Deprecated
    public void setAxisChannel(AxisType axis, int channel)
    { m_axes[axis.value] = (byte) channel; }

    /**
     * Get the channel currently associated with the X axis.
     *
     * @return The channel for the axis.
     */
    public int getXChannel()
    { return m_axes[Axis.kX.value]; }

    /**
     * Set the channel associated with the X axis.
     *
     * @param channel The channel to set the axis to.
     */
    public void setXChannel(int channel)
    { m_axes[Axis.kX.value] = (byte) channel; }

    /**
     * Get the channel currently associated with the Y axis.
     *
     * @return The channel for the axis.
     */
    public int getYChannel()
    { return m_axes[Axis.kY.value]; }

    /**
     * Set the channel associated with the Y axis.
     *
     * @param channel The channel to set the axis to.
     */
    public void setYChannel(int channel)
    { m_axes[Axis.kY.value] = (byte) channel; }

    /**
     * Get the channel currently associated with the Z axis.
     *
     * @return The channel for the axis.
     */
    public int getZChannel()
    { return m_axes[Axis.kZ.value]; }

    /**
     * Set the channel associated with the Z axis.
     *
     * @param channel The channel to set the axis to.
     */
    public void setZChannel(int channel)
    { m_axes[Axis.kZ.value] = (byte) channel; }

    /**
     * Get the channel currently associated with the twist axis.
     *
     * @return The channel for the axis.
     */
    public int getTwistChannel()
    { return m_axes[Axis.kTwist.value]; }

    /**
     * Set the channel associated with the twist axis.
     *
     * @param channel The channel to set the axis to.
     */
    public void setTwistChannel(int channel)
    { m_axes[Axis.kTwist.value] = (byte) channel; }

    /**
     * Get the channel currently associated with the throttle axis.
     *
     * @return The channel for the axis.
     */
    public int getThrottleChannel()
    { return m_axes[Axis.kThrottle.value]; }

    /**
     * Set the channel associated with the throttle axis.
     *
     * @param channel The channel to set the axis to.
     */
    public void setThrottleChannel(int channel)
    { m_axes[Axis.kThrottle.value] = (byte) channel; }

    /**
     * Get the channel currently associated with the specified axis.
     *
     * @param axis The axis to look up the channel for.
     * @return The channel for the axis.
     * @deprecated Use the more specific axis channel getter functions.
     */
    @Deprecated
    public int getAxisChannel(AxisType axis)
    { return m_axes[axis.value]; }

    /**
     * Get the X value of the joystick. This depends on the mapping of the joystick connected to the
     * current port.
     *
     * @param hand Unused
     * @return The X value of the joystick.
     */
    @Override
    public final float getX(Hand hand)
    { return getRawAxis(m_axes[Axis.kX.value]); }

    /**
     * Get the Y value of the joystick. This depends on the mapping of the joystick connected to the
     * current port.
     *
     * @param hand Unused
     * @return The Y value of the joystick.
     */
    @Override
    public final float getY(Hand hand)
    { return getRawAxis(m_axes[Axis.kY.value]); }

    /**
     * Get the z position of the HID.
     *
     * @return the z position
     */
    public float getZ()
    { return getRawAxis(m_axes[Axis.kZ.value]); }

    /**
     * Get the twist value of the current joystick. This depends on the mapping of the joystick
     * connected to the current port.
     *
     * @return The Twist value of the joystick.
     */
    public float getTwist()
    { return getRawAxis(m_axes[Axis.kTwist.value]); }

    /**
     * Get the throttle value of the current joystick. This depends on the mapping of the joystick
     * connected to the current port.
     *
     * @return The Throttle value of the joystick.
     */
    public float getThrottle()
    { return getRawAxis(m_axes[Axis.kThrottle.value]); }

    /**
     * For the current joystick, return the axis determined by the argument.
     * <p>
     * <p>This is for cases where the joystick axis is returned programmatically, otherwise one of the
     * previous functions would be preferable (for example getX()).
     *
     * @param axis The axis to read.
     * @return The value of the axis.
     * @deprecated Use the more specific axis getter functions.
     */
    @Deprecated
    public float getAxis(final AxisType axis)
    {
        switch(axis)
        {
            case kX:
                return getX();
            case kY:
                return getY();
            case kZ:
                return getZ();
            case kTwist:
                return getTwist();
            case kThrottle:
                return getThrottle();
            default:
                return 0.0F;
        }
    }

    /**
     * Read the state of the trigger on the joystick.
     *
     * @return The state of the trigger.
     */
    public boolean getTrigger()
    { return getRawButton(Button.kTrigger.value); }

    /**
     * Whether the trigger was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTriggerPressed()
    { return getRawButtonPressed(Button.kTrigger.value); }

    /**
     * Whether the trigger was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTriggerReleased()
    { return getRawButtonReleased(Button.kTrigger.value); }

    /**
     * Read the state of the top button on the joystick.
     *
     * @return The state of the top button.
     */
    public boolean getTop()
    { return getRawButton(Button.kTop.value); }

    /**
     * Whether the top button was pressed since the last check.
     *
     * @return Whether the button was pressed since the last check.
     */
    public boolean getTopPressed()
    { return getRawButtonPressed(Button.kTop.value); }

    /**
     * Whether the top button was released since the last check.
     *
     * @return Whether the button was released since the last check.
     */
    public boolean getTopReleased()
    { return getRawButtonReleased(Button.kTop.value); }

    /**
     * Get buttons based on an enumerated type.
     * <p>
     * <p>The button type will be looked up in the list of buttons and then read.
     *
     * @param button The type of button to read.
     * @return The state of the button.
     * @deprecated Use Button enum values instead of ButtonType.
     */
    @Deprecated
    public boolean getButton(ButtonType button)
    { return getRawButton(button.value); }

    /**
     * Get the magnitude of the direction vector formed by the joystick's current position relative to
     * its origin.
     *
     * @return The magnitude of the direction vector
     */
    public float getMagnitude()
    { return (float) Math.sqrt(MathUtils.pow2f(getX()) + MathUtils.pow2f(getY())); }

    /**
     * Get the direction of the vector formed by the joystick and its origin in radians.
     *
     * @return The direction of the vector in radians
     */
    public double getDirectionRadians()
    { return Math.atan2(getX(), -getY()); }

    /**
     * Get the direction of the vector formed by the joystick and its origin in degrees.
     *
     * @return The direction of the vector in degrees
     */
    public double getDirectionDegrees()
    { return MathUtils.rad2Deg(getDirectionRadians()); }

    /**
     * Get the direction of the vector formed by the joystick and its origin in radians.
     *
     * @return The direction of the vector in radians
     */
    public float getDirectionRadiansF()
    { return (float) Math.atan2(getX(), -getY()); }

    /**
     * Get the direction of the vector formed by the joystick and its origin in degrees.
     *
     * @return The direction of the vector in degrees
     */
    public float getDirectionDegreesF()
    { return MathUtils.rad2Deg(getDirectionRadiansF()); }

    /**
     * Represents an analog axis on a joystick.
     */
    public enum AxisType
    {
        kX(0), kY(1), kZ(2), kTwist(3), kThrottle(4);

        public final int value;

        AxisType(int value)
        { this.value = value; }
    }

    /**
     * Represents a digital button on a joystick.
     */
    public enum ButtonType
    {
        kTrigger(1), kTop(2);

        public final int value;

        ButtonType(int value)
        { this.value = value; }
    }

    /**
     * Represents a digital button on a joystick.
     */
    private enum Button
    {
        kTrigger(1), kTop(2);

        public final int value;

        Button(int value)
        { this.value = value; }
    }

    /**
     * Represents an analog axis on a joystick.
     */
    private enum Axis
    {
        kX(0), kY(1), kZ(2), kTwist(3), kThrottle(4), kNumAxes(5);

        public final int value;

        Axis(int value)
        { this.value = value; }
    }
}
