package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.hal.HAL;

/**
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
public abstract class GenericHIDF
{
    /**
     * Represents a rumble output on the JoyStick.
     */
    public enum RumbleType
    {
        kLeftRumble, kRightRumble
    }

    public enum HIDType
    {
        kUnknown(-1),
        kXInputUnknown(0),
        kXInputGamepad(1),
        kXInputWheel(2),
        kXInputArcadeStick(3),
        kXInputFlightStick(4),
        kXInputDancePad(5),
        kXInputGuitar(6),
        kXInputGuitar2(7),
        kXInputDrumKit(8),
        kXInputGuitar3(11),
        kXInputArcadePad(19),
        kHIDJoystick(20),
        kHIDGamepad(21),
        kHIDDriving(22),
        kHIDFlight(23),
        kHID1stPerson(24);

        public static final HIDType[] VALUES = values();

        public final int value;

        HIDType(int value)
        { this.value = value; }
    }

    /**
     * Which hand the Human Interface Device is associated with.
     */
    public enum Hand
    {
        kLeft(0), kRight(1);

        public final int value;

        Hand(int value)
        { this.value = value; }
    }

    private DriverStationF m_ds;
    private final int m_port;
    private int m_outputs;
    private short m_leftRumble;
    private short m_rightRumble;

    public GenericHIDF(int port)
    {
        m_ds = DriverStationF.INSTANCE;
        m_port = port;
    }

    /**
     * Get the x position of the HID.
     *
     * @return the x position of the HID
     */
    public final float getX()
    { return getX(Hand.kRight); }

    /**
     * Get the x position of HID.
     *
     * @param hand which hand, left or right
     * @return the x position
     */
    public abstract float getX(Hand hand);

    /**
     * Get the y position of the HID.
     *
     * @return the y position
     */
    public final float getY()
    { return getY(Hand.kRight); }

    /**
     * Get the y position of the HID.
     *
     * @param hand which hand, left or right
     * @return the y position
     */
    public abstract float getY(Hand hand);

    /**
     * Get the button value (starting at button 1).
     * <p>
     * <p>The buttons are returned in a single 16 bit value with one bit representing the state of
     * each button. The appropriate button is returned as a boolean value.
     *
     * @param button The button number to be read (starting at 1)
     * @return The state of the button.
     */
    public boolean getRawButton(int button)
    { return m_ds.getStickButton(m_port, (byte) button); }

    /**
     * Whether the button was pressed since the last check. Button indexes begin at
     * 1.
     *
     * @param button The button index, beginning at 1.
     * @return Whether the button was pressed since the last check.
     */
    public boolean getRawButtonPressed(int button)
    { return m_ds.getStickButtonPressed(m_port, (byte) button); }

    /**
     * Whether the button was released since the last check. Button indexes begin at
     * 1.
     *
     * @param button The button index, beginning at 1.
     * @return Whether the button was released since the last check.
     */
    public boolean getRawButtonReleased(int button)
    { return m_ds.getStickButtonReleased(m_port, button); }

    /**
     * Get the value of the axis.
     *
     * @param axis The axis to read, starting at 0.
     * @return The value of the axis.
     */
    public float getRawAxis(int axis)
    { return m_ds.getStickAxis(m_port, axis); }

    /**
     * Get the angle in degrees of a POV on the HID.
     * <p>
     * <p>The POV angles start at 0 in the up direction, and increase clockwise (eg right is 90,
     * upper-left is 315).
     *
     * @param pov The index of the POV to read (starting at 0)
     * @return the angle of the POV in degrees, or -1 if the POV is not pressed.
     */
    public int getPOV(int pov)
    { return m_ds.getStickPOV(m_port, pov); }

    public int getPOV()
    { return getPOV(0); }

    /**
     * Get the number of axes for the HID.
     *
     * @return the number of axis for the current HID
     */
    public int getAxisCount()
    { return m_ds.getStickAxisCount(m_port); }

    /**
     * For the current HID, return the number of POVs.
     */
    public int getPOVCount()
    { return m_ds.getStickPOVCount(m_port); }

    /**
     * For the current HID, return the number of buttons.
     */
    public int getButtonCount()
    { return m_ds.getStickButtonCount(m_port); }

    /**
     * Get the type of the HID.
     *
     * @return the type of the HID.
     */
    public HIDType getType()
    { return HIDType.VALUES[m_ds.getJoystickType(m_port)]; }

    /**
     * Get the name of the HID.
     *
     * @return the name of the HID.
     */
    public String getName()
    { return m_ds.getJoystickName(m_port); }

    /**
     * Get the axis type of a joystick axis.
     *
     * @return the axis type of a joystick axis.
     */
    public int getAxisType(int axis)
    { return m_ds.getJoystickAxisType(m_port, axis); }

    /**
     * Get the port number of the HID.
     *
     * @return The port number of the HID.
     */
    public int getPort()
    { return m_port; }

    /**
     * Set a single HID output value for the HID.
     *
     * @param outputNumber The index of the output to set (1-32)
     * @param value        The value to set the output to
     */
    public void setOutput(int outputNumber, boolean value)
    {
        m_outputs = (m_outputs & ~(1 << (outputNumber - 1))) | ((value ? 1 : 0) << (outputNumber - 1));
        HAL.setJoystickOutputs((byte) m_port, m_outputs, m_leftRumble, m_rightRumble);
    }

    /**
     * Set all HID output values for the HID.
     *
     * @param value The 32 bit output value (1 bit for each output)
     */
    public void setOutputs(int value)
    {
        m_outputs = value;
        HAL.setJoystickOutputs((byte) m_port, m_outputs, m_leftRumble, m_rightRumble);
    }

    /**
     * Set the rumble output for the HID. The DS currently supports 2 rumble values, left rumble and
     * right rumble.
     *
     * @param type  Which rumble value to set
     * @param value The normalized value (0 to 1) to set the rumble to
     */
    public void setRumble(RumbleType type, float value)
    {
        if(value < 0.0F) { value = 0.0F; }
        else if(value > 1.0F) { value = 1.0F; }

        if(type == RumbleType.kLeftRumble) { m_leftRumble = (short) (value * 65535); }
        else { m_rightRumble = (short) (value * 65535); }
        HAL.setJoystickOutputs((byte) m_port, m_outputs, m_leftRumble, m_rightRumble);
    }
}
