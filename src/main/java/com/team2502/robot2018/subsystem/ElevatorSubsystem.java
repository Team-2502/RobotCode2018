package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.Constants;
import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.sendables.PIDTunable;
import com.team2502.robot2018.sendables.SendablePIDTuner;
import com.team2502.robot2018.utils.NonDefaultSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2502.robot2018.Constants.Physical.Elevator;

public class ElevatorSubsystem extends NonDefaultSubsystem implements PIDTunable, DashboardData.DashboardUpdater
{
    /**
     * In (enc units / 100ms) / sec
     *
     * @see Elevator#MAX_ACCEL_FPS2
     */
    private static int MAX_ACCEL_EACCEL = 1500;

    private static int CRUISE_VELOCITY_EVEL = 600;

    private final WPI_TalonSRX elevatorTop;
    private final WPI_TalonSRX elevatorBottom;

    // The difference between the climber motors and the elevator motors is that
    // the climber motors are the slower CIMS while the the elevator motors are the faster VEX motors
    private final WPI_TalonSRX climberTop;
    private final WPI_TalonSRX climberBottom;
    private final SendablePIDTuner pidTuner;

    private double kF = 0.2D;
    private double kP = 0.2D;
    private double kI = 0D;
    private double kD = 0D;

    private int timer;

    public ElevatorSubsystem()
    {
        SmartDashboard.putNumber("Elevator: Max Accel (enc units)", MAX_ACCEL_EACCEL);
        SmartDashboard.putNumber("Elevator: Cruise vel (enc units)", CRUISE_VELOCITY_EVEL);


        elevatorTop = new WPI_TalonSRX(RobotMap.Motor.ELEVATOR_TOP);
        elevatorBottom = new WPI_TalonSRX(RobotMap.Motor.ELEVATOR_BOTTOM);


        climberTop = new WPI_TalonSRX(RobotMap.Motor.CLIMBER_TOP);
        climberBottom = new WPI_TalonSRX(RobotMap.Motor.CLIMBER_BOTTOM);

        elevatorTop.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        elevatorBottom.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Motor.ELEVATOR_TOP, Constants.INIT_TIMEOUT);

        elevatorTop.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, Constants.INIT_TIMEOUT);
        elevatorBottom.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX, LimitSwitchNormal.NormallyOpen, RobotMap.Motor.ELEVATOR_TOP, Constants.INIT_TIMEOUT);

        elevatorTop.follow(elevatorBottom);
        elevatorBottom.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.INIT_TIMEOUT);
        elevatorBottom.setSensorPhase(true);

        // Set trapezoid details for motion profiling
        elevatorBottom.configMotionCruiseVelocity(CRUISE_VELOCITY_EVEL, Constants.INIT_TIMEOUT);
        elevatorBottom.configMotionAcceleration(MAX_ACCEL_EACCEL, Constants.INIT_TIMEOUT);


        elevatorBottom.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 5, Constants.INIT_TIMEOUT);
        elevatorBottom.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 5, Constants.INIT_TIMEOUT);

        pidTuner = new SendablePIDTuner(this, this);

        DashboardData.addUpdater(this);
        calibrateEncoder();

    }

    /**
     * Run the elevator at given speed. This method automatically
     * disengages the climbing solenoid for the duration that the elevator runs.
     *
     * @param speed the speed (-1.0 to 1.0) that the elevator motors will run at.
     */
    public void moveElevator(ControlMode controlMode, double speed)
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
        // 20ms per 1 loop
        if(timer <= 6)
        {
            timer++;
        }

        else
        {
            elevatorBottom.set(controlMode, speed);
        }
    }

    /**
     * Move the elevator
     *
     * @param speed Percent voltage
     */
    public void moveElevator(double speed)
    {
        moveElevator(ControlMode.PercentOutput, speed);
    }


    /**
     * Move the elevator to a certain position
     *
     * @param feet Height of elevator in feet
     */
    public void setElevatorPos(float feet)
    {
        double epos = feet * Elevator.FEET_TO_EPOS_ELEV;

        moveElevator(ControlMode.MotionMagic, epos);

        System.out.println("Closed Loop Error (evelator): " + elevatorBottom.getClosedLoopError(0));

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
    public double getkP()
    {
        return kP;
    }

    @Override
    public void setkP(double kP)
    {
        this.kP = kP;
        setPID();
    }

    @Override
    public double getkI()
    {
        return kI;
    }

    @Override
    public void setkI(double kI)
    {
        this.kI = kI;
        setPID();
    }

    @Override
    public double getkD()
    {
        return kD;
    }

    @Override
    public void setkD(double kD)
    {
        this.kD = kD;
        setPID();
    }

    @Override
    public double getkF()
    {
        return kF;
    }

    @Override
    public void setkF(double kF)
    {
        this.kF = kF;
        setPID();
    }

    @Override
    public void setPID()
    {
        elevatorBottom.selectProfileSlot(0, 0);
        elevatorBottom.config_kP(0, kP, Constants.INIT_TIMEOUT);
        elevatorBottom.config_kI(0, kI, Constants.INIT_TIMEOUT);
        elevatorBottom.config_kD(0, kD, Constants.INIT_TIMEOUT);
        elevatorBottom.config_kF(0, kF, Constants.INIT_TIMEOUT);
    }

    /**
     * @return Velocity of elevator in enc units / 100 ms
     * <p>
     * TODO: Change to real units
     */
    public double getVel()
    {
        return elevatorBottom.getSelectedSensorVelocity(0);
    }

    /**
     * @return Position of elevator in enc units
     * <p>
     * TODO: Change to real units
     */
    public double getPos()
    {
        int selectedSensorPosition = elevatorBottom.getSelectedSensorPosition(0);
        return selectedSensorPosition;
    }

    /**
     * Reset the encoder reading to 0
     */
    public void calibrateEncoder()
    {
        elevatorBottom.setSelectedSensorPosition(0, 0, Constants.INIT_TIMEOUT);
    }


    @Override
    public void updateDashboard()
    {
        SmartDashboard.putNumber("Elevator: Velocity (fps)", getVel() * Elevator.EVEL_TO_FPS_ELEV);
        SmartDashboard.putNumber("Elevator: Position (ft)", getPos() * Elevator.EPOS_TO_FEET_ELEV);
        SmartDashboard.putNumber("Elevator: Max Accel (enc units) (read)", MAX_ACCEL_EACCEL);
        SmartDashboard.putNumber("Elevator: Cruise vel (enc units) (read)", CRUISE_VELOCITY_EVEL);

        MAX_ACCEL_EACCEL = (int) SmartDashboard.getNumber("Elevator: Max Accel (enc units)", MAX_ACCEL_EACCEL);
        CRUISE_VELOCITY_EVEL = (int) SmartDashboard.getNumber("Elevator: Cruise vel (enc units)", CRUISE_VELOCITY_EVEL);


        pidTuner.updateDashboard();
    }
}
