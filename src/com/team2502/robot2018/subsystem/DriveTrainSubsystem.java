package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.*;
import com.team2502.robot2018.command.teleop.DriveCommand;
import com.team2502.robot2018.sendables.Nameable;
import com.team2502.robot2018.sendables.PIDTunable;
import com.team2502.robot2018.sendables.SendableDriveStrategyType;
import com.team2502.robot2018.sendables.SendablePIDTuner;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Example Implementation, Many changes needed.
 */
public class DriveTrainSubsystem extends Subsystem implements DashboardData.DashboardUpdater, PIDTunable
{
    private static final FloatPair SPEED_CONTAINER = new FloatPair();

    private final WPI_TalonSRX leftFrontTalonEnc;
    private final WPI_TalonSRX leftRearTalon;
    private final WPI_TalonSRX rightFrontTalonEnc;
    private final WPI_TalonSRX rightRearTalon;

    private final DifferentialDrive drive;
    private final SpeedControllerGroup spgLeft;
    private final SpeedControllerGroup spgRight;

    private final SendablePIDTuner pidTuner;

    double kP = 0.2;
    double kI = 0.0;
    double kD = 0;
    double kF = 0.03;

    private float lastLeft;
    private float lastRight;
    private boolean isNegativePressed;
    private boolean negative;

    public DriveTrainSubsystem()
    {

        setName("DriveTrainSubsystem");

        lastLeft = 0.0F;
        lastRight = 0.0F;

        leftFrontTalonEnc = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_FRONT_LEFT);
        leftRearTalon = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_BACK_LEFT);

        rightFrontTalonEnc = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_FRONT_RIGHT);
        rightRearTalon = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_BACK_RIGHT);

        // Add encoders (ask nicely for encoders on drivetrain)
        leftRearTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.INIT_TIMEOUT);
        rightRearTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.INIT_TIMEOUT);


        spgLeft = new SpeedControllerGroup(leftFrontTalonEnc, leftRearTalon);
        spgRight = new SpeedControllerGroup(rightFrontTalonEnc, rightRearTalon);

        spgLeft.setInverted(false);
        spgRight.setInverted(true);

        drive = new DifferentialDrive(spgLeft, spgRight);

        pidTuner = new SendablePIDTuner(this, this);

        drive.setSafetyEnabled(true);
        setTeleopSettings();
        DashboardData.addUpdater(this);
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new DriveCommand());
    }

    public void stop() { drive.stopMotor(); }


    private void setTeleopSettings(WPI_TalonSRX talon)
    {
        talon.set(ControlMode.PercentOutput, 0.0F);
        talon.configNominalOutputForward(0.0D, Constants.INIT_TIMEOUT);
        talon.configNominalOutputReverse(0.0D, Constants.INIT_TIMEOUT);

        talon.configPeakOutputForward(1.0D, Constants.INIT_TIMEOUT);
        talon.configPeakOutputReverse(-1.0D, Constants.INIT_TIMEOUT);

        talon.setInverted(true);
    }

    /**
     * This class sets the correct nominal/peak values for the talon and also sets the correct inversion settings on the encoders.
     */
    public void setTeleopSettings()
    {

        setTeleopSettings(leftFrontTalonEnc);
        setTeleopSettings(rightFrontTalonEnc);
        setTeleopSettings(leftRearTalon);
        setTeleopSettings(rightRearTalon);

        setupTalons();

//        Robot.TRANSMISSION_SOLENOID.setGear(true);
    }

    public void setupTalons()
    {

        leftFrontTalonEnc.setSensorPhase(false);
        rightFrontTalonEnc.setSensorPhase(true);

        leftRearTalon.follow(leftFrontTalonEnc);
        rightRearTalon.follow(rightFrontTalonEnc);
    }

    /**
     * This method makes sure that the non-encoder talons are following their encoder-equipped brethren.
     */
    public void setAutonSettings()
    {
        setupTalons();
        // Set high gear
    }

    public void setPID()
    {
        setPID(kP, kI, kD);
    }

    /**
     * Sets the PID for left AND right motors. If the descriptions below confuse you, go look up a better
     * explanation of PID.
     *
     * @param kP Proportional constant. Makes the motor go faster proportional to the error.
     * @param kI Integral constant. Makes the motor go faster proportional to the integral of the error.
     * @param kD Derivative constant. Makes the motor go faster proportional to the derivative of the error.
     */
    public void setPID(double kP, double kI, double kD)
    {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;

        leftFrontTalonEnc.config_kP(0, kP, Constants.INIT_TIMEOUT);
        leftFrontTalonEnc.config_kI(0, kI, Constants.INIT_TIMEOUT);
        leftFrontTalonEnc.config_kD(0, kD, Constants.INIT_TIMEOUT);

        rightFrontTalonEnc.config_kP(0, kP, Constants.INIT_TIMEOUT);
        rightFrontTalonEnc.config_kI(0, kI, Constants.INIT_TIMEOUT);
        rightFrontTalonEnc.config_kD(0, kD, Constants.INIT_TIMEOUT);
    }

    /**
     * Sets the PID for left AND right motors. If the descriptions below confuse you, go look up a better
     * explanation of PID.
     *
     * @param kP    Proportional constant. Makes the motor go faster proportional to the error.
     * @param kI    Integral constant. Makes the motor go faster proportional to the integral of the error
     * @param kD    Derivative constant. Makes the motor go faster proportional to the derivative of the error
     * @param iZone Integral Zone. If the integral of the error is bigger than this, the integral is reset to 0.
     */
    public void setPID(double kP, double kI, double kD, int iZone)
    {
        setPID(kP, kI, kD);

        leftRearTalon.config_IntegralZone(0, iZone, Constants.INIT_TIMEOUT);
        rightRearTalon.config_IntegralZone(0, iZone, Constants.INIT_TIMEOUT);
    }

    /**
     * Drive the robot. The equation leftWheel=-rightWheel must be true for the robot to setElevatorPV straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's position. In teleop, it's percent voltage.
     *
     * @param leftWheel   Units for the left side of drivetrain
     * @param rightWheel  Units for the right side of drivetrain
     * @param controlMode The mode that the motors are being driven in
     */

    public void runMotors(ControlMode controlMode, float leftWheel, float rightWheel) // double z
    {
        // setting slaves as the talons w/ encoders is the only way it works ¯\_(ツ)_/¯
        leftRearTalon.follow(leftRearTalon);
        rightRearTalon.follow(rightRearTalon);

        leftFrontTalonEnc.set(controlMode, leftWheel);
        rightFrontTalonEnc.set(controlMode, rightWheel);
    }

    /**
     * Drive the robot using ControlMode.PercentOutput. The equation leftWheel=-rightWheel must be true for the robot to setElevatorPV straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's position. In teleop, it's percent voltage.
     *
     * @param leftWheel  Units for the left side of drivetrain
     * @param rightWheel Units for the right side of drivetrain
     */
    public void runMotors(float leftWheel, float rightWheel) // double z
    {
        drive.tankDrive(leftWheel, rightWheel, true);
    }

    public double turningFactor()
    {
        return Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY() - OI.JOYSTICK_DRIVE_RIGHT.getY());
    }

    public void disableTalons()
    {
        leftFrontTalonEnc.set(ControlMode.Disabled, 0);
        leftRearTalon.set(ControlMode.Disabled, 0);
        rightFrontTalonEnc.set(ControlMode.Disabled, 0);
        rightRearTalon.set(ControlMode.Disabled, 0);
    }

    /**
     * Used to gradually increase the speed of the robot.
     *
     * @param out The percent voltages of each wheel
     * @return the speed of the robot
     */
    private FloatPair getSpeedTank(FloatPair out)
    {
        float joystickLevel;
        // Get the base speed of the robot
        if(negative) { joystickLevel = (float) OI.JOYSTICK_DRIVE_RIGHT.getY(); }
        else { joystickLevel = (float) OI.JOYSTICK_DRIVE_LEFT.getY(); }

        // Only increase the speed by a small amount
        float diff = joystickLevel - lastLeft;
        if(diff > 0.1F) { joystickLevel = lastLeft + 0.1F; }
        else if(diff < -0.1F) { joystickLevel = lastLeft - 0.1F; }
        lastLeft = joystickLevel;
        out.left = joystickLevel;

        if(negative) { joystickLevel = (float) OI.JOYSTICK_DRIVE_LEFT.getY(); }
        else { joystickLevel = (float) OI.JOYSTICK_DRIVE_RIGHT.getY(); }

        diff = joystickLevel - lastRight;
        if(diff > 0.1F) { joystickLevel = lastRight + 0.1F; }
        else if(diff < -0.1F) { joystickLevel = lastRight - 0.1F; }
        lastRight = joystickLevel;
        out.right = joystickLevel;

        // Sets the speed to 0 if the speed is less than 0.05 and larger than -0.05
        if(Math.abs(out.left) < 0.05F) { out.left = 0.0F; }
        if(Math.abs(out.right) < 0.05F) { out.right = 0.0F; }

        return out;
    }

    private FloatPair getSpeedTank()
    {
        return getSpeedTank(SPEED_CONTAINER);
    }

    public void drive()
    {
        FloatPair speed = getSpeedTank();
        SmartDashboard.putNumber("speedL", -speed.left);
        SmartDashboard.putNumber("speedR", -speed.right);

        Nameable currentMode = SendableDriveStrategyType.getInstance().getCurrentMode();

        if(!(currentMode instanceof DriveStrategyType))
        {
            throw new IllegalArgumentException("currentMode is of wrong type!"); // Note this acts as a return statement
        }
        DriveStrategyType strategyType = (DriveStrategyType) currentMode;
        if(negative)
        {
            strategyType.getDriveStrategy().drive(speed.left, speed.right);
        }
        else
        {
            strategyType.getDriveStrategy().drive(-speed.left, -speed.right);
        }
    }

    public float getTanSpeed()
    {
        return Math.abs(getTanVel());
    }

    /**
     * Get tangent velocity
     *
     * @return The tangent velocity
     */
    public float getTanVel()
    {
        return (getLeftVel() + getRightVel()) / 2;
    }

    /**
     * @return Velocity as read by left encoder in Feet per Second
     */
    public float getLeftVel() { return getLeftRawVel() * Constants.EVEL_TO_FPS_DT; }

    /**
     * @return Velocity as read by right encoder in Feet per Second
     */

    public float getRightVel() { return getRightRawVel() * Constants.EVEL_TO_FPS_DT; }

    public int getRightRawVel() { return rightFrontTalonEnc.getSelectedSensorVelocity(0); }

    public int getLeftRawVel() { return leftFrontTalonEnc.getSelectedSensorVelocity(0); }

    /**
     * @return Position as read by right encoder in Feet per Second
     */
    public float getRightPos() { return rightFrontTalonEnc.getSelectedSensorPosition(0) * Constants.EPOS_TO_FEET_DT; }

    /**
     * @return Position as read by left encoder in Feet per Second
     */
    public float getLeftPos() { return leftFrontTalonEnc.getSelectedSensorPosition(0) * Constants.EPOS_TO_FEET_DT; }


    @Override
    public void updateDashboard()
    {
        SmartDashboard.putNumber("Left Speed (ft/s)", getLeftVel());
        SmartDashboard.putNumber("Left Pos (ft)", getLeftVel());

        SmartDashboard.putNumber("Right Speed (ft/s)", getRightVel());
        SmartDashboard.putNumber("Right Pos (ft)", getRightPos());


        pidTuner.updateDashboard();
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
        leftFrontTalonEnc.config_kF(0, kF, Constants.INIT_TIMEOUT);
        rightFrontTalonEnc.config_kF(0, kF, Constants.INIT_TIMEOUT);
    }

    public enum DriveStrategyType implements Nameable
    {
        VOLTAGE("VOLTAGE", (joystickLeft, joystickRight) -> {
            Robot.DRIVE_TRAIN.runMotors(joystickLeft, joystickRight);
        });

        private final String name;
        private final DriveStrategy driveStrategy;

        DriveStrategyType(String name, DriveStrategy driveStrategy)
        {
            this.name = name;
            this.driveStrategy = driveStrategy;
        }

        @Override
        public String getName()
        {
            return name;
        }

        public DriveStrategy getDriveStrategy()
        {
            return driveStrategy;
        }
    }

    private interface DriveStrategy
    {
        void drive(float joystickLeft, float joystickRight);
    }

    /**
     * A data structure to store a pair of floats.
     */
    private static class FloatPair
    {
        public float left;
        public float right;

        public FloatPair(float left, float right)
        {
            this.left = left;
            this.right = right;
        }

        public FloatPair() { }

        @Override
        public String toString()
        {
            return new StringBuilder(47).append("Pair: { \"left\": \"")
                                        .append(String.format("%.05f", left)).append("\", \"right\": \"")
                                        .append(String.format("%.05f", right)).append("\" }").toString();
        }

        @Override
        public int hashCode()
        {
            return (Float.floatToIntBits(left) * 31) + (Float.floatToIntBits(right) * 7);
        }

        @Override
        public boolean equals(Object o)
        {
            if(this == o) { return true; }
            if(o instanceof FloatPair)
            {
                FloatPair pair = (FloatPair) o;
                return left == pair.left && right == pair.right;
            }
            return false;
        }
    }

}
