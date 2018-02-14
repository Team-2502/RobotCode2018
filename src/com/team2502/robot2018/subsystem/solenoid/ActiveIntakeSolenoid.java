package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import logger.Log;

public class ActiveIntakeSolenoid extends NonDefaultSubsystem
{
    private final Solenoid grabber;
    private boolean grabberEnabled = false;

    public ActiveIntakeSolenoid()
    {
        grabber = new Solenoid(RobotMap.Solenoid.ACTIVE_GRABBER);

        // starting state
        grabber.set(false);
    }

    public void toggleIntake()
    {
        Log.info("Toggling intake");
        grabber.set(grabberEnabled = !grabberEnabled);
    }
}
