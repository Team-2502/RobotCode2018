package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;

public class CarriageBrakeSolenoid extends NonDefaultSubsystem
{
    private Solenoid carriageBrake;
    private boolean brakeState = false;

    public CarriageBrakeSolenoid()
    {
        carriageBrake = new Solenoid(RobotMap.Solenoid.CARRIAGE_BRAKE);
        carriageBrake.set(brakeState);
    }

    public void toggle()
    {

        carriageBrake.set(brakeState = !brakeState);
    }

    public void set(boolean state)
    {

        carriageBrake.set(brakeState = state);
    }
}
