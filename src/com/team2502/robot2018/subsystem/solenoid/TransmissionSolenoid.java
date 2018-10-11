package com.team2502.robot2018.subsystem.solenoid;

import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TransmissionSolenoid extends NonDefaultSubsystem implements DashboardData.DashboardUpdater
{
    private final Solenoid transmission;
    public boolean disabledAutoShifting = true;
    private boolean lowGear;

    public TransmissionSolenoid()
    {
        transmission = new Solenoid(RobotMap.Solenoid.TRANSMISSION_SWITCH);
        lowGear = false;
        transmission.set(false);
        DashboardData.addUpdater(this);
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
     * @param lowGear Boolean saying "do you want to be in high gear?"
     */
    public void setLowGear(boolean lowGear)
    { transmission.set(this.lowGear = !lowGear); }

    @Override
    public void updateDashboard()
    {
        SmartDashboard.putBoolean("High Gear", isHigh());
    }
}
