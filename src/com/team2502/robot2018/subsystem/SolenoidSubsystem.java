package com.team2502.robot2018.subsystem;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import logger.Log;

/**
 * Serves as a global place for all solenoids.
 * If you have any questions ask @ikeaj - it was
 * his idea.
 */
public class SolenoidSubsystem extends NonDefaultSubsystem
{
    /**
     * This solenoid, when enabled (set to true), locks the climber
     */
    private final Solenoid climberSolenoid;
    /**
     * When true, it means the elevator is locked and can only go down
     */
    private boolean climberDisabled;

    private final Solenoid switcher;
    public boolean disabledAutoShifting = true;
    public boolean highGear;

    private final Solenoid grabber;
    private boolean grabberEnabled;

    public SolenoidSubsystem()
    {
        climberSolenoid = new Solenoid(RobotMap.Solenoid.CLIMBER_SOLENOID);
        climberDisabled = true;
        climberSolenoid.set(climberDisabled);

        switcher = new Solenoid(RobotMap.Solenoid.TRANSMISSION_SWITCH);
        highGear = false;

        grabber = new Solenoid(RobotMap.Solenoid.ACTIVE_GRABBER);
        grabberEnabled = false;
        grabber.set(grabberEnabled);
    }

    //region Climber
    /**
     * Stop the elevator from slamming down by engaging the climber
     */
    public void lockElevator()
    {
        Log.info("Locking elevator");
        climberSolenoid.set(climberDisabled = false);
    }

    /**
     * Let the elevator move freely by disengaging the climber
     */
    public void unlockElevator()
    {
        Log.info("Unlocking elevator");
        climberSolenoid.set(climberDisabled = true);
    }

    /**
     * Toggle whether the climber is engaged
     */
    public void toggleLock()
    { climberSolenoid.set(climberDisabled = !climberDisabled); }

    /**
     * @return Whether or not the climber is enabled
     */
    public boolean isLocked()
    { return climberDisabled; }

    /**
     * Engage the climber
     */
    public void engageClimber()
    { lockElevator(); }

    /**
     * Disengage the climber
     */
    public void disengageClimber()
    { unlockElevator(); }
    //endregion

    //region Transmission
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
    //endregion

    public void toggleIntake()
    {
        Log.info("Toggling intake");
        grabber.set(grabberEnabled = !grabberEnabled);
    }
}
