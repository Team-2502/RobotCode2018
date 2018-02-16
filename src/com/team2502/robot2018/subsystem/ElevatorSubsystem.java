package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.OI;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.baseoverloads.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem
{
    public final WPI_TalonSRXF elevatorTop;
    public final WPI_TalonSRXF elevatorBottom;

    // The difference between the climber motors and the elevator motors is that
    // the climber motors are the slower CIMS while the the elevator motors are the faster miniCIMS
    public final WPI_TalonSRXF climberTop;
    public final WPI_TalonSRXF climberBottom;
    /**
     * Move the elevator up or down
     *
     * @param speed Speed to move elevator (in percent output)
     */
    int timer;
    boolean elevUp = false;
    boolean elevDown = false;

    public ElevatorSubsystem()
    {
        elevatorTop = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_TOP);
        elevatorBottom = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_BOTTOM);

        climberTop = new WPI_TalonSRXF(RobotMap.Motor.CLIMBER_TOP);
        climberBottom = new WPI_TalonSRXF(RobotMap.Motor.CLIMBER_BOTTOM);

        elevatorTop.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        elevatorBottom.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Motor.ELEVATOR_TOP, Constants.INIT_TIMEOUT);

        elevatorTop.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        elevatorBottom.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Motor.ELEVATOR_TOP, Constants.INIT_TIMEOUT);
    }

    public void moveElevator(double speed)
    {
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
        // TESTING THESE LINES
        if(!Robot.CLIMBER_SOLENOID.isLocked())
        {
            Robot.CLIMBER_SOLENOID.lockElevator();
            timer = 0;
        }
        // TESTING THESE LINES

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
    public void periodic()
    {
        // Uses debouncing since there is a wrapper
        if(OI.ELEV_UP.get())
        {
            System.out.println("UP");
            moveElevator(1);
        }
        else if(OI.ELEV_DOWN.get())
        {
            System.out.println("DOWN");
            moveElevator(-.3);
        }
        else
        {
            System.out.println("STOP");
            Robot.ELEVATOR.stopElevator();
        }
    }

    @Override
    protected void initDefaultCommand() { }


}
