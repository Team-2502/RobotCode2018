package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;

public class TransmissionSolenoid extends NonDefaultSubsystem
{
    private final Solenoid transmission;
    public boolean disabledAutoShifting = true;
    private boolean highGear;

    public TransmissionSolenoid()
    {
        transmission = new Solenoid(RobotMap.Solenoid.TRANSMISSION_SWITCH);
        highGear = false;
        transmission.set(false);
    }

    /**
     * Switch the gear from its current state
     */
    public void toggleGear()
    { transmission.set(highGear = !highGear); }

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
    { transmission.set(this.highGear = highGear); }
}
