package com.team2502.robot2018.utils.baseoverloads;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

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
 * The Talon's also don't take in doubles when setting the speed.
 * CTRE PhoenixLib converts the floating point numbers to integral
 * types using specific operations depending on what the control
 * mode is. This mean we can very easily write up some code to
 * use single precision instead of double precision floating
 * point numbers.
 */
@Deprecated
public class SpeedControllerGroupF extends SendableBase implements ISpeedControllerF
{
    private static int instances = 0;
    private final ISpeedControllerF[] m_speedControllers;
    private boolean m_isInverted = false;

    /**
     * Create a new SpeedControllerGroup with the provided SpeedControllers.
     *
     * @param speedControllers The SpeedControllers to add
     */
    public SpeedControllerGroupF(ISpeedControllerF speedController, ISpeedControllerF... speedControllers)
    {
        m_speedControllers = new ISpeedControllerF[speedControllers.length + 1];
        m_speedControllers[0] = speedController;
        addChild(speedController);
        for(int i = 0; i < speedControllers.length; i++)
        {
            m_speedControllers[i + 1] = speedControllers[i];
            addChild(speedControllers[i]);
        }
        instances++;
        setName("SpeedControllerGroup", instances);
    }

    @Override
    public void set(double speed)
    {
        for(SpeedController speedController : m_speedControllers) { speedController.set(m_isInverted ? -speed : speed); }
    }

    @Override
    public double get()
    {
        if(m_speedControllers.length > 0) { return m_speedControllers[0].get(); }
        return 0.0;
    }

    @Override
    public boolean getInverted()
    { return m_isInverted; }

    @Override
    public void setInverted(boolean isInverted)
    { m_isInverted = isInverted; }

    @Override
    public void disable()
    {
        for(SpeedController speedController : m_speedControllers) { speedController.disable(); }
    }

    @Override
    public void stopMotor()
    {
        for(SpeedController speedController : m_speedControllers) { speedController.stopMotor(); }
    }

    @Override
    public void pidWrite(double output)
    {
        for(SpeedController speedController : m_speedControllers) { speedController.pidWrite(output); }
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.setSmartDashboardType("Speed Controller");
        builder.setSafeState(this::stopMotor);
        builder.addDoubleProperty("Value", this::get, this::set);
    }

    @Override
    public void set(float speed)
    {
        for(SpeedController speedController : m_speedControllers) { speedController.set(m_isInverted ? -speed : speed); }
    }

    @Override
    public float getF()
    {
        if(m_speedControllers.length > 0) { return m_speedControllers[0].getF(); }
        return 0.0F;
    }
}
