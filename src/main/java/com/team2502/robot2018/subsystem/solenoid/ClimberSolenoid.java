package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import logger.Log;

public class ClimberSolenoid extends NonDefaultSubsystem
{
    /**
     * This solenoid, when enabled (set to true), locks the climber
     */
    private final Solenoid climberSolenoid;

    /**
     * When true, it means the elevator is locked and can only go down
     */
    private boolean climberEnabled;

    public ClimberSolenoid()
    {
        climberSolenoid = new Solenoid(RobotMap.Solenoid.CLIMBER_SOLENOID);
        climberEnabled = true;
    }


    /**
     * Stop the elevator from slamming down by engaging the climber
     */
    public void lockElevator()
    {
        Log.info("Locking elevator");
        climberSolenoid.set(climberEnabled = false);
    }

    /**
     * Let the elevator move freely by disengaging the climber
     */
    public void unlockElevator()
    {
        Log.info("Unlocking elevator");
        climberSolenoid.set(climberEnabled = true);
    }

    /**
     * Toggle whether the climber is engaged
     */
    public void toggleLock()
    { climberSolenoid.set(climberEnabled = !climberEnabled); }

    /**
     * @return Whether or not the climber is enabled
     */
    public boolean isLocked()
    { return !climberEnabled; }

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
}
