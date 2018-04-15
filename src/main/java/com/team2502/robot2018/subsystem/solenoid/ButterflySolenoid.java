package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;


/**
 * Controls the butterfly dropping
 */
public class ButterflySolenoid extends NonDefaultSubsystem
{
    // The physical solenoid
    private final Solenoid butterfly;

    // Whether the butterfly is down
    private boolean butterflyState = false;

    public ButterflySolenoid()
    {
        butterfly = new Solenoid(RobotMap.Solenoid.BUTTERFLY_SOLENOID);

        // starting state
        butterfly.set(false);
    }

    /**
     * Toggle whether or not the butterfly is down
     */
    public void toggle()
    {
//        Log.info("Toggling intake");
        butterfly.set(butterflyState = !butterflyState);
    }

    /**
     * Set whether the butterfly is released
     *
     * @param state released?
     */
    public void set(boolean state)
    {
//        Log.info("Setting butterfly");
        butterfly.set(butterflyState = state);
    }
}
