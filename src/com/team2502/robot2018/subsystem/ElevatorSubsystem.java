package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem
{
    private final WPI_TalonSRX elevatorTop;
    private final WPI_TalonSRX elevatorBottom;

    // The difference between the climber motors and the elevator motors is that
    // the climber motors are the slower CIMS while the the elevator motors are the faster VEX motors
    private final WPI_TalonSRX climberTop;
    private final WPI_TalonSRX climberBottom;

    private int timer;

    public ElevatorSubsystem()
    {
        elevatorTop = new WPI_TalonSRX(RobotMap.Motor.ELEVATOR_TOP);
        elevatorBottom = new WPI_TalonSRX(RobotMap.Motor.ELEVATOR_BOTTOM);

        climberTop = new WPI_TalonSRX(RobotMap.Motor.CLIMBER_TOP);
        climberBottom = new WPI_TalonSRX(RobotMap.Motor.CLIMBER_BOTTOM);

        elevatorTop.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        elevatorBottom.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Motor.ELEVATOR_TOP, Constants.INIT_TIMEOUT);

        elevatorTop.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        elevatorBottom.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Motor.ELEVATOR_TOP, Constants.INIT_TIMEOUT);
    }

    /**
     * Run the elevator at given speed. This method automatically
     * disengages the climbing solenoid for the duration that the elevator runs.
     * @param speed the speed (-1.0 to 1.0) that the elevator motors will run at.
     */
    public void moveElevator(double speed)
    {
        /* Artificial delay between unlocking the elevator solenoid
           and starting the elevator motors.

           This allows the shifting gearbox enough time to physically
           change position before the elevator motors begin to try and
           lift the elevator against the climbing ratchet.

           The climbing solenoid controls the shifting gearbox.

           We re-engage the shifting gearbox because the drag from the
           CIM motors (climber motors) is enough to prevent the elevator
           from falling back into the ground.

           This method gets called once per 20ms while the elevator
           buttons are held.
         */
        if(Robot.CLIMBER_SOLENOID.isLocked())
        {
            timer = 0;
            Robot.CLIMBER_SOLENOID.unlockElevator();
        }
        if(timer <= 15)
        {
            timer++;
        }

        else
        {
            elevatorTop.set(speed);
            elevatorBottom.set(speed);
        }
    }

    /**
     * Stop the elevator by setting voltage output to 0
     */
    public void stopElevator()
    {
        if(!Robot.CLIMBER_SOLENOID.isLocked())
        {
            Robot.CLIMBER_SOLENOID.lockElevator();
        }

        elevatorTop.set(0.0F);
        elevatorBottom.set(0.0F);
    }

    /**
     * Move the elevator down for climbing purpose
     *
     * @param x How fast to move it. Make it positive to life elevator
     */
    public void moveClimber(double x)
    {
        x = Math.abs(x);
        climberTop.set(ControlMode.PercentOutput, x);
        climberBottom.set(ControlMode.PercentOutput, x);
    }

    /**
     * Stop the climber by setting voltage output to 0
     */
    public void stopClimber()
    {
        climberTop.set(ControlMode.PercentOutput, 0);
        climberBottom.set(ControlMode.PercentOutput, 0);
    }

    /**
     * Stop both the elevator and the climber
     */
    public void stopAll()
    {
        stopElevator();
        stopClimber();
    }

    @Override
    protected void initDefaultCommand() { }
}
