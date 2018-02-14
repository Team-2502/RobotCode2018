package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import logger.Log;

public class ClimberSolenoid extends Subsystem
{
    /**
     * This solenoid, when enabled (set to true), locks the climber
     */
    private final Solenoid climberSolenoid;
    /**
     * When true, it means the elevator is locked and can only go down
     */
    private boolean climberDisabled;

    public ClimberSolenoid()
    {
        climberSolenoid = new Solenoid(RobotMap.Solenoid.CLIMBER_SOLENOID);
        climberDisabled = true;
    }

    @Override
    protected void initDefaultCommand() { }

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
}
