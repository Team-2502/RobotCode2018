package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import logger.Log;

public class ButterflySolenoid extends NonDefaultSubsystem
{
    private final Solenoid butterfly;
    private boolean butterflyState = false;

    public ButterflySolenoid()
    {
        butterfly = new Solenoid(RobotMap.Solenoid.BUTTERFLY_SOLENOID);

        // starting state
        butterfly.set(false);
    }

    public void toggle()
    {
        Log.info("Toggling intake");
        butterfly.set(butterflyState = !butterflyState);
    }

    public void set(boolean state)
    {
        Log.info("Setting butterfly");
        butterfly.set(butterflyState = state);
    }
}
