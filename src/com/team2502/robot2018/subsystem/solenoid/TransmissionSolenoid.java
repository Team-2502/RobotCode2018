package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class TransmissionSolenoid extends Subsystem
{
    private final Solenoid switcher;
    public boolean disabledAutoShifting = true;
    public boolean highGear;

    public TransmissionSolenoid()
    {
        switcher = new Solenoid(RobotMap.Solenoid.TRANSMISSION_SWITCH);
        highGear = false;
    }

    @Override
    protected void initDefaultCommand()
    {

    }

    /**
     * Switch the gear from its current state
     */
    public void switchGear()
    {
        setGear(highGear = !highGear);
        Robot.SHIFTED = System.currentTimeMillis();
    }

    /**
     * @return if we are in high gear
     */
    public boolean getGear()
    { return highGear; }

    /**
     * Set the transmission to a specific high gear or low gear
     *
     * @param highGear Boolean saying "do you want to be in high gear?"
     */
    public void setGear(boolean highGear)
    {
        if(this.highGear != highGear)
        {
            Robot.SHIFTED = System.currentTimeMillis();
            switcher.set(this.highGear = highGear);
        }
    }

    /**
     * Autoshifting can be disabled during robot demonstrations. This method toggles it.
     */
    public void toggleAutoShifting()
    { disabledAutoShifting = !disabledAutoShifting; }
}
