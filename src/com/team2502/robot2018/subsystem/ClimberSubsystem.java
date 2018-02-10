package com.team2502.robot2018.subsystem;

import com.team2502.robot2018.OI;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.RunClimberCommand;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Subsystem;

@Deprecated
public class ClimberSubsystem extends Subsystem
{
    public final WPI_TalonSRXF climbMotorLeft;
    public final WPI_TalonSRXF climbMotorRight;

    public ClimberSubsystem()
    {
        climbMotorLeft = new WPI_TalonSRXF(RobotMap.Motor.CLIMBER_TOP);
        climbMotorRight = new WPI_TalonSRXF(RobotMap.Motor.CLIMBER_BOTTOM);
    }

    @Override
    protected void initDefaultCommand()
    { setDefaultCommand(new RunClimberCommand()); }

    public void climb()
    {
        float speed = Math.abs((float) OI.JOYSTICK_FUNCTION.getY());

        /*
         * Prevent motors from going backwards and
         * stopElevator if speed is below a certain threshold.
         */
        if(speed <= 0.01F) { speed = 0.0F; }

        runMotors(speed);
    }

    public void runMotors(float speed)
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
        climbMotorLeft.set(0.0F);
        climbMotorRight.set(0.0F);
    }
}
