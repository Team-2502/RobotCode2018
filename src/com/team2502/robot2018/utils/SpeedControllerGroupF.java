package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.SendableBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

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
