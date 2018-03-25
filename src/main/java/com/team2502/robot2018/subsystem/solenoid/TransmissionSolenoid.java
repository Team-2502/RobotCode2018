package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The solenoid that controls the transmission
 */
public class TransmissionSolenoid extends NonDefaultSubsystem
{
    private final Solenoid transmission;
    public boolean disabledAutoShifting = true;
    private boolean lowGear;

    public TransmissionSolenoid()
    {
        transmission = new Solenoid(RobotMap.Solenoid.TRANSMISSION_SWITCH);
        lowGear = false;
        transmission.set(false);
    }

    /**
     * Switch the gear from its current state
     */
    public void toggleGear()
    { transmission.set(lowGear = !lowGear); }

    /**
     * @return if we are in high gear
     */
    public boolean isHigh()
    { return !lowGear; }

    /**
     * Set the transmission to a specific high gear or low gear
     *
     * @param highGear Boolean saying "do you want to be in high gear?"
     */
    public void setHighGear(boolean highGear)
    { transmission.set(this.lowGear = !highGear); }
}