package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.OI;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.RunClimberCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimberSubsystem extends Subsystem
{
    public final WPI_TalonSRX climbMotorLeft;
    public final WPI_TalonSRX climbMotorRight;

    public ClimberSubsystem()
    {
        climbMotorLeft = new WPI_TalonSRX(RobotMap.Motor.CLIMBER_LEFT);
        climbMotorRight = new WPI_TalonSRX(RobotMap.Motor.CLIMBER_RIGHT);
    }

    @Override
    protected void initDefaultCommand()
    { setDefaultCommand(new RunClimberCommand()); }

    public void climb()
    {
        double speed = Math.abs(OI.JOYSTICK_FUNCTION.getY());

        /*
         * Prevent motors from going backwards and
         * stop if speed is below a certain threshold.
         */
        if(speed <= 0.01D) { speed = 0.0D; }

        runMotors(speed);
    }

    public void runMotors(double speed)
    {
        // TODO: Verify that these settings are correct.
        /*
         * Potentially needs to be changed to match
         * how the climber is setup on the robot.
         */
        climbMotorLeft.set(-speed);
        climbMotorRight.set(speed);
    }

    public void stop()
    {
        climbMotorLeft.set(0.0D);
        climbMotorRight.set(0.0D);
    }
}
