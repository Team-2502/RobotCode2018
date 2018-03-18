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

    /**
     * Autoshifting does not exist on Daedalus
     * <br>
     * For an autoshifting example, see {@link com.team2502.robot2017.command.teleop.DriveCommand#execute} found in UpdatedRobotCode2017 (which is a repo)
     */
    public boolean disabledAutoShifting = true;

    /**
     * If we have shifted into low gear
     */
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
    public boolean isHigh()
    { return highGear; }

    /**
     * Set the transmission to a specific high gear or low gear
     *
     * @param highGear Boolean saying "do you want to be in high gear?"
     */
    public void setLowGear(boolean highGear)
    { transmission.set(this.highGear = !highGear); }
}
