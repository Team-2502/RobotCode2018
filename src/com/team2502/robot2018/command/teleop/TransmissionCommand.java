package com.team2502.robot2018.command.teleop;

import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Created by 64009334 on 2/18/18.
 */
public class TransmissionCommand extends InstantCommand
{
    public TransmissionCommand()
    { requires(Robot.TRANSMISSION_SOLENOID); }

    @Override
    protected void execute()
    {
        Robot.TRANSMISSION_SOLENOID.toggleGear();
    }


}
