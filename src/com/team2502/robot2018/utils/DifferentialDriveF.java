package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.hal.FRCNetComm;
import edu.wpi.first.wpilibj.hal.HAL;
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
@SuppressWarnings("unused")
public class DifferentialDriveF extends RobotDriveBase
{
    public static final float kDefaultQuickStopThreshold = 0.2F;
    public static final float kDefaultQuickStopAlpha = 0.1F;

    private static int instances = 0;
    protected float m_deadbandF = (float) kDefaultDeadband;
    protected float m_maxOutputF = (float) kDefaultMaxOutput;
    private ISpeedControllerF m_leftMotor;
    private ISpeedControllerF m_rightMotor;
    private float m_quickStopThreshold = kDefaultQuickStopThreshold;
    private float m_quickStopAlpha = kDefaultQuickStopAlpha;
    private float m_quickStopAccumulator = 0.0F;
    private boolean m_reported = false;

    /**
     * Construct a DifferentialDrive.
     * <p>
     * <p>To pass multiple motors per side, use a {@link edu.wpi.first.wpilibj.SpeedControllerGroup}. If a motor needs to be
     * inverted, do so before passing it in.
     */
    public DifferentialDriveF(ISpeedControllerF leftMotor, ISpeedControllerF rightMotor)
    {
        m_leftMotor = leftMotor;
        m_rightMotor = rightMotor;
        addChild(m_leftMotor);
        addChild(m_rightMotor);
        instances++;
        setName("DifferentialDrive", instances);
    }

    /**
     * Arcade moveElevator method for differential moveElevator platform.
     * The calculated values will be squared to decrease sensitivity at low speeds.
     *
     * @param xSpeed    The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                  positive.
     */
    public void arcadeDrive(float xSpeed, float zRotation)
    { arcadeDrive(xSpeed, zRotation, true); }

    protected float limit(float value)
    {
        if(value > 1.0F) { return 1.0F; }
        if(value < -1.0F) { return -1.0F; }
        return value;
    }

    protected float applyDeadband(float value, float deadband)
    {
        if(Math.abs(value) > deadband)
        {
            if(value > 0.0F) { return (value - deadband) / (1.0F - deadband); }
            else { return (value + deadband) / (1.0F - deadband); }
        }
        return 0.0F;
    }

    /**
     * Arcade moveElevator method for differential moveElevator platform.
     *
     * @param xSpeed        The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation     The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                      positive.
     * @param squaredInputs If set, decreases the input sensitivity at low speeds.
     */
    public void arcadeDrive(float xSpeed, float zRotation, boolean squaredInputs)
    {
        if(!m_reported)
        {
            HAL.report(FRCNetComm.tResourceType.kResourceType_RobotDrive, 2, FRCNetComm.tInstances.kRobotDrive_ArcadeStandard);
            m_reported = true;
        }

        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadbandF);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadbandF);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if(squaredInputs)
        {
            xSpeed = Math.copySign(xSpeed * xSpeed, xSpeed);
            zRotation = Math.copySign(zRotation * zRotation, zRotation);
        }

        float leftMotorOutput;
        float rightMotorOutput;

        float maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

        if(xSpeed >= 0.0)
        {
            // First quadrant, else second quadrant
            if(zRotation >= 0.0)
            {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
            else
            {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
        }
        else
        {
            // Third quadrant, else fourth quadrant
            if(zRotation >= 0.0)
            {
                leftMotorOutput = xSpeed + zRotation;
                rightMotorOutput = maxInput;
            }
            else
            {
                leftMotorOutput = maxInput;
                rightMotorOutput = xSpeed - zRotation;
            }
        }

        m_leftMotor.set(limit(leftMotorOutput) * m_maxOutputF);
        m_rightMotor.set(-limit(rightMotorOutput) * m_maxOutputF);

        m_safetyHelper.feed();
    }

    /**
     * Curvature moveElevator method for differential moveElevator platform.
     * <p>
     * <p>The rotation argument controls the curvature of the robot's path rather than its rate of
     * heading change. This makes the robot more controllable at high speeds. Also handles the
     * robot's quick turn functionality - "quick turn" overrides constant-curvature turning for
     * turn-in-place maneuvers.
     *
     * @param xSpeed      The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param zRotation   The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is
     *                    positive.
     * @param isQuickTurn If set, overrides constant-curvature turning for
     *                    turn-in-place maneuvers.
     */
    public void curvatureDrive(float xSpeed, float zRotation, boolean isQuickTurn)
    {
        if(!m_reported)
        {
            // HAL.report(tResourceType.kResourceType_RobotDrive, 2, tInstances.kRobotDrive_Curvature);
            m_reported = true;
        }

        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadbandF);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadbandF);

        float angularPower;
        boolean overPower;

        if(isQuickTurn)
        {
            if(Math.abs(xSpeed) < m_quickStopThreshold)
            {
                m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
                                         + m_quickStopAlpha * limit(zRotation) * 2;
            }
            overPower = true;
            angularPower = zRotation;
        }
        else
        {
            overPower = false;
            angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

            if(m_quickStopAccumulator > 1.0F) { m_quickStopAccumulator -= 1.0F; }
            else if(m_quickStopAccumulator < -1.0F) { m_quickStopAccumulator += 1.0F; }
            else { m_quickStopAccumulator = 0.0F; }
        }

        float leftMotorOutput = xSpeed + angularPower;
        float rightMotorOutput = xSpeed - angularPower;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if(overPower)
        {
            if(leftMotorOutput > 1.0F)
            {
                rightMotorOutput -= leftMotorOutput - 1.0F;
                leftMotorOutput = 1.0F;
            }
            else if(rightMotorOutput > 1.0F)
            {
                leftMotorOutput -= rightMotorOutput - 1.0F;
                rightMotorOutput = 1.0F;
            }
            else if(leftMotorOutput < -1.0F)
            {
                rightMotorOutput -= leftMotorOutput + 1.0F;
                leftMotorOutput = -1.0F;
            }
            else if(rightMotorOutput < -1.0F)
            {
                leftMotorOutput -= rightMotorOutput + 1.0F;
                rightMotorOutput = -1.0F;
            }
        }

        m_leftMotor.set(leftMotorOutput * m_maxOutputF);
        m_rightMotor.set(-rightMotorOutput * m_maxOutputF);

        m_safetyHelper.feed();
    }

    /**
     * Tank moveElevator method for differential moveElevator platform.
     * The calculated values will be squared to decrease sensitivity at low speeds.
     *
     * @param leftSpeed  The robot's left side speed along the X axis [-1.0..1.0]. Forward is
     *                   positive.
     * @param rightSpeed The robot's right side speed along the X axis [-1.0..1.0]. Forward is
     *                   positive.
     */
    public void tankDrive(float leftSpeed, float rightSpeed)
    {
        tankDrive(leftSpeed, rightSpeed, true);
    }

    /**
     * Tank moveElevator method for differential moveElevator platform.
     *
     * @param leftSpeed     The robot left side's speed along the X axis [-1.0..1.0]. Forward is
     *                      positive.
     * @param rightSpeed    The robot right side's speed along the X axis [-1.0..1.0]. Forward is
     *                      positive.
     * @param squaredInputs If set, decreases the input sensitivity at low speeds.
     */
    public void tankDrive(float leftSpeed, float rightSpeed, boolean squaredInputs)
    {
        if(!m_reported)
        {
            HAL.report(FRCNetComm.tResourceType.kResourceType_RobotDrive, 2, FRCNetComm.tInstances.kRobotDrive_Tank);
            m_reported = true;
        }

        leftSpeed = limit(leftSpeed);
        leftSpeed = applyDeadband(leftSpeed, m_deadbandF);

        rightSpeed = limit(rightSpeed);
        rightSpeed = applyDeadband(rightSpeed, m_deadbandF);

        // Square the inputs (while preserving the sign) to increase fine control
        // while permitting full power.
        if(squaredInputs)
        {
            leftSpeed = Math.copySign(leftSpeed * leftSpeed, leftSpeed);
            rightSpeed = Math.copySign(rightSpeed * rightSpeed, rightSpeed);
        }

        m_leftMotor.set(-leftSpeed * m_maxOutputF);
        m_rightMotor.set(-rightSpeed * m_maxOutputF);

        m_safetyHelper.feed();
    }

    /**
     * Sets the QuickStop speed threshold in curvature drive.
     * <p>
     * <p>QuickStop compensates for the robot's moment of inertia when stopping after a QuickTurn.
     * <p>
     * <p>While QuickTurn is enabled, the QuickStop accumulator takes on the rotation rate value
     * outputted by the low-pass filter when the robot's speed along the X axis is below the
     * threshold. When QuickTurn is disabled, the accumulator's value is applied against the computed
     * angular power request to slow the robot's rotation.
     *
     * @param threshold X speed below which quick stopElevator accumulator will receive rotation rate values
     *                  [0..1.0].
     */
    public void setQuickStopThreshold(float threshold)
    { m_quickStopThreshold = threshold; }

    /**
     * Sets the low-pass filter gain for QuickStop in curvature drive.
     * <p>
     * <p>The low-pass filter filters incoming rotation rate commands to smooth out high frequency
     * changes.
     *
     * @param alpha Low-pass filter gain [0.0..2.0]. Smaller values result in slower output changes.
     *              Values between 1.0 and 2.0 result in output oscillation. Values below 0.0 and
     *              above 2.0 are unstable.
     */
    public void setQuickStopAlpha(float alpha)
    { m_quickStopAlpha = alpha; }

    @Override
    public void stopMotor()
    {
        m_leftMotor.stopMotor();
        m_rightMotor.stopMotor();
        m_safetyHelper.feed();
    }

    @Override
    public String getDescription()
    { return "DifferentialDrive"; }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        builder.setSmartDashboardType("DifferentialDrive");
        builder.addDoubleProperty("Left Motor Speed", m_leftMotor::get, m_leftMotor::set);
        builder.addDoubleProperty(
                "Right Motor Speed",
                () -> -m_rightMotor.get(),
                x -> m_rightMotor.set(-x));
    }
}
