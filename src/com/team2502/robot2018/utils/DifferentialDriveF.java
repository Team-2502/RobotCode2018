package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.hal.FRCNetComm;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@Deprecated
@SuppressWarnings("unused")
public class DifferentialDriveF extends RobotDriveBase
{
    public static final float kDefaultQuickStopThreshold = 0.2F;
    public static final float kDefaultQuickStopAlpha = 0.1F;

    private static int instances = 0;

    private ISpeedControllerF m_leftMotor;
    private ISpeedControllerF m_rightMotor;

    private float m_quickStopThreshold = kDefaultQuickStopThreshold;
    private float m_quickStopAlpha = kDefaultQuickStopAlpha;
    private float m_quickStopAccumulator = 0.0F;

    protected float m_deadbandF = (float) kDefaultDeadband;
    protected float m_maxOutputF = (float) kDefaultMaxOutput;
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
     * Arcade drive method for differential drive platform.
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
     * Arcade drive method for differential drive platform.
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
     * Curvature drive method for differential drive platform.
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
     * Tank drive method for differential drive platform.
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
     * Tank drive method for differential drive platform.
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

        m_leftMotor.set(leftSpeed * m_maxOutputF);
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
     * @param threshold X speed below which quick stop accumulator will receive rotation rate values
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
