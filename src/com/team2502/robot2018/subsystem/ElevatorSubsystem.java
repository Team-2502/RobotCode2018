package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import logger.Log;

public class ElevatorSubsystem extends Subsystem
{
    public final WPI_TalonSRXF elevatorTop;
    public final WPI_TalonSRXF elevatorBottom;

    /**
     * This solenoid, when enabled (set to true), locks the climber
     */
    private final Solenoid climberSolenoid;

    // The difference between the climber motors and the elevator motors is that
    // the climber motors are the slower CIMS while the the elevator motors are the faster miniCIMS
    public final WPI_TalonSRXF climberTop;
    public final WPI_TalonSRXF climberBottom;


    /**
     * When true, it means the elevator is locked and can only go down
     */
    private boolean climberDisabled = false;

    public ElevatorSubsystem()
    {
        elevatorTop = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_TOP);
        elevatorBottom = new WPI_TalonSRXF(RobotMap.Motor.ELEVATOR_BOTTOM);

        climberSolenoid = new Solenoid(RobotMap.Solenoid.CLIMBER_SOLENOID);

        climberTop = new WPI_TalonSRXF(RobotMap.Motor.CLIMBER_TOP);
        climberBottom = new WPI_TalonSRXF(RobotMap.Motor.CLIMBER_BOTTOM);
    }

    /**
     * Move the elevator up or down
     * @param x Speed to move elevator (in percent output)
     */
    public void moveElevator(double x)
    {
        elevatorTop.set(ControlMode.PercentOutput, x);
        elevatorBottom.set(ControlMode.PercentOutput, x);
    }

    /**
     * Stop the elevator by setting voltage output to 0
     */
    public void stopElevator()
    {
        elevatorTop.set(ControlMode.PercentOutput, 0.0F);
        elevatorBottom.set(ControlMode.PercentOutput, 0.0F);
    }

    /**
     * Move the elevator down for climbing purposes
     * @param x How fast to move it
     */
    public void moveClimber(double x)
    {
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
     * Stop the elevator from slamming down by engaging the climber
     */
    public void lockElevator()
    {
        //TODO: Once solenoid is installed, make functional
        Log.info("Locking elevator");
        climberSolenoid.set(climberDisabled = false);
    }

    /**
     * Engage the climber
     */
    public void engageClimber() { lockElevator(); }

    /**
     * Disengage the climber
     */
    public void disengageClimber() { unlockElevator(); }

    /**
     * Let the elevator move freely by disengaging the climber
     */
    public void unlockElevator()
    {
        //TODO: Once solenoid is installed, make functional
        Log.info("Unlocking elevator");

        climberSolenoid.set(climberDisabled = true);
    }

    /**
     * Toggle whether the climber is engaged
     */
    public void toggleLock()
    {
        climberSolenoid.set(climberDisabled = !climberDisabled);
    }

    /**
     * @return Whether or not the climber is enabled
     */
    public boolean isLocked() { return climberDisabled; }

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
