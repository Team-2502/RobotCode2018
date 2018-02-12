package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.team2502.robot2018.*;
import com.team2502.robot2018.command.teleop.DriveCommand;
import com.team2502.robot2018.sendables.Nameable;
import com.team2502.robot2018.sendables.PIDTunable;
import com.team2502.robot2018.sendables.SendableDriveStrategyType;
import com.team2502.robot2018.sendables.SendablePIDTuner;
import com.team2502.robot2018.utils.baseoverloads.DifferentialDriveF;
import com.team2502.robot2018.utils.baseoverloads.SpeedControllerGroupF;
import com.team2502.robot2018.utils.baseoverloads.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Example Implementation, Many changes needed.
 */
public class DriveTrainSubsystem extends Subsystem implements DashboardData.DashboardUpdater, PIDTunable
{
    private static final FloatPair SPEED_CONTAINER = new FloatPair();

    public final WPI_TalonSRXF leftFrontTalon;
    public final WPI_TalonSRXF leftRearTalonEnc;
    public final WPI_TalonSRXF rightFrontTalon;
    public final WPI_TalonSRXF rightRearTalonEnc;
    public final DifferentialDriveF drive;
    public final SpeedControllerGroupF spgLeft;
    public final SpeedControllerGroupF spgRight;

    private final SendablePIDTuner pidTuner;

    double kP = 0.5;
    double kI = 0.001;
    double kD = 0;
    double kF = 0.53;

    private float lastLeft;
    private float lastRight;
    private boolean isNegativePressed;
    private boolean negative;

    public DriveTrainSubsystem()
    {

        setName("DriveTrainSubsystem");

        lastLeft = 0.0F;
        lastRight = 0.0F;

        leftFrontTalon = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_FRONT_LEFT);
        leftRearTalonEnc = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_BACK_LEFT);

        rightFrontTalon = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_FRONT_RIGHT);
        rightRearTalonEnc = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_BACK_RIGHT);

        // Add encoders (ask nicely for encoders on drivetrain)
        leftRearTalonEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.INIT_TIMEOUT);
        rightRearTalonEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.INIT_TIMEOUT);

        spgLeft = new SpeedControllerGroupF(leftFrontTalon, leftRearTalonEnc);
        spgRight = new SpeedControllerGroupF(rightFrontTalon, rightRearTalonEnc);

        drive = new DifferentialDriveF(spgLeft, spgRight);

        pidTuner = new SendablePIDTuner(this, this);

        drive.setSafetyEnabled(true);
        setTeleopSettings();
        DashboardData.addUpdater(this);
    }

    public void stop() { drive.stopMotor(); }

    private void setTeleopSettings(WPI_TalonSRXF talon)
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

        setTeleopSettings(leftFrontTalon);
        setTeleopSettings(rightFrontTalon);
        setTeleopSettings(leftRearTalonEnc);
        setTeleopSettings(rightRearTalonEnc);
//        leftFrontTalon.follow(leftRearTalonEnc);
//        rightFrontTalon.follow(rightRearTalonEnc);


        // Required for correct readings
        leftRearTalonEnc.setSensorPhase(false);
        rightRearTalonEnc.setSensorPhase(true);
    }

    /**
     * This method makes sure that the non-encoder talons are following their encoder-equipped brethren.
     */
    public void setAutonSettings()
    {
        leftFrontTalon.follow(leftRearTalonEnc);
        rightFrontTalon.follow(rightRearTalonEnc);

        leftRearTalonEnc.setSensorPhase(false);
        rightRearTalonEnc.setSensorPhase(true);
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

        leftRearTalonEnc.config_kP(0, kP, Constants.INIT_TIMEOUT);
        leftRearTalonEnc.config_kI(0, kI, Constants.INIT_TIMEOUT);
        leftRearTalonEnc.config_kD(0, kD, Constants.INIT_TIMEOUT);

        rightRearTalonEnc.config_kP(0, kP, Constants.INIT_TIMEOUT);
        rightRearTalonEnc.config_kI(0, kI, Constants.INIT_TIMEOUT);
        rightRearTalonEnc.config_kD(0, kD, Constants.INIT_TIMEOUT);
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

        leftRearTalonEnc.config_IntegralZone(0, iZone, Constants.INIT_TIMEOUT);
        rightRearTalonEnc.config_IntegralZone(0, iZone, Constants.INIT_TIMEOUT);
    }

    /**
     * Drive the robot. The equation leftWheel=-rightWheel must be true for the robot to moveElevator straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's position. In teleop, it's percent voltage.
     *
     * @param leftWheel   Units for the left side of drivetrain
     * @param rightWheel  Units for the right side of drivetrain
     * @param controlMode The mode that the motors are being driven in
     */
    public void runMotors(ControlMode controlMode, float leftWheel, float rightWheel) // double z
    {
//        leftFrontTalon.set(controlMode, leftWheel);
        leftRearTalonEnc.set(controlMode, leftWheel);

//        rightFrontTalon.set(controlMode, rightWheel);
        rightRearTalonEnc.set(controlMode, rightWheel);
    }

    /**
     * Drive the robot with x=0,y=0. The equation x=-y must be true for the robot to moveElevator straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's position. In teleop, it's percent voltage.
     *
     * @param controlMode The mode that the motors are being driven in
     */
    public void runMotors(ControlMode controlMode) // double z
    {
//        leftFrontTalon.set(controlMode, 0);
        leftRearTalonEnc.set(controlMode, 0);

//        rightFrontTalon.set(controlMode, 0);
        rightRearTalonEnc.set(controlMode, 0);
    }

    /**
     * Drive the robot using ControlMode.PercentOutput. The equation leftWheel=-rightWheel must be true for the robot to moveElevator straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's position. In teleop, it's percent voltage.
     *
     * @param leftWheel  Units for the left side of drivetrain
     * @param rightWheel Units for the right side of drivetrain
     */
    public void runMotors(float leftWheel, float rightWheel) // double z
    {
        runMotors(ControlMode.PercentOutput, leftWheel, rightWheel);
    }

    public double turningFactor()
    {
        return Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY() - OI.JOYSTICK_DRIVE_RIGHT.getY());
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new DriveCommand());
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
        if(negative) { joystickLevel = OI.JOYSTICK_DRIVE_RIGHT.getY(); }
        else { joystickLevel = OI.JOYSTICK_DRIVE_LEFT.getY(); }

        // Only increase the speed by a small amount
        float diff = joystickLevel - lastLeft;
        if(diff > 0.1F) { joystickLevel = lastLeft + 0.1F; }
        else if(diff < -0.1F) { joystickLevel = lastLeft - 0.1F; }
        lastLeft = joystickLevel;
        out.left = joystickLevel;

        if(negative) { joystickLevel = OI.JOYSTICK_DRIVE_LEFT.getY(); }
        else { joystickLevel = OI.JOYSTICK_DRIVE_RIGHT.getY(); }

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

    /**
     * TODO: finish!!!!
     *
     * @param out the percent voltages of each wheel.
     * @return
     * @deprecated
     */
    private FloatPair getSpeedArcade(FloatPair out)
    {
        // ( v_l + v_r ) / 2
        float vTan = OI.JOYSTICK_DRIVE_RIGHT.getY();

        // (vR - vL) / l
        float rot = OI.JOYSTICK_DRIVE_RIGHT.getX();

        throw new UnsupportedOperationException();

//        float joystickLevel;
//        // Get the base speed of the robot
//        if(negative) { joystickLevel = OI.JOYSTICK_DRIVE_RIGHT.getY(); }
//        else { joystickLevel = OI.JOYSTICK_DRIVE_LEFT.getY(); }
//
//        // Only increase the speed by a small amount
//        float diff = joystickLevel - lastLeft;
//        if(diff > 0.1F) { joystickLevel = lastLeft + 0.1F; }
//        else if(diff < -0.1F) { joystickLevel = lastLeft - 0.1F; }
//        lastLeft = joystickLevel;
//        out.left = joystickLevel;
//
//        if(negative) { joystickLevel = OI.JOYSTICK_DRIVE_LEFT.getY(); }
//        else { joystickLevel = OI.JOYSTICK_DRIVE_RIGHT.getY(); }
//
//        diff = joystickLevel - lastRight;
//        if(diff > 0.1F) { joystickLevel = lastRight + 0.1F; }
//        else if(diff < -0.1F) { joystickLevel = lastRight - 0.1F; }
//        lastRight = joystickLevel;
//        out.right = joystickLevel;
//
//        // Sets the speed to 0 if the speed is less than 0.05 and larger than -0.05
//        if(Math.abs(out.left) < 0.05F) { out.left = 0.0F; }
//        if(Math.abs(out.right) < 0.05F) { out.right = 0.0F; }
//
//        return out;
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

        // Log.debug("Left: {0,number,#.###}\t\t Right: {0,number,#.###}", speed.right, speed.left);

        //reverse moveElevator
        if((OI.JOYSTICK_DRIVE_LEFT.getRawButton(RobotMap.Joystick.Button.INVERSE_DRIVER_CONTROLS) && !isNegativePressed)) { negative = !negative; }

        isNegativePressed = OI.JOYSTICK_DRIVE_LEFT.getRawButton(RobotMap.Joystick.Button.INVERSE_DRIVER_CONTROLS);

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

    public float avgVel()
    {
        return Math.abs((leftRearTalonEnc.getSelectedSensorVelocity(0) + rightRearTalonEnc.getSelectedSensorVelocity(0)) / 2.0F);
    }


    /**
     * @return Velocity as read by left encoder in Feet per Second
     */
    public float getLeftVel() { return leftRearTalonEnc.getSelectedSensorVelocity(0) * Constants.EVEL_TO_FPS; }

    /**
     * @return Velocity as read by right encoder in Feet per Second
     */
    public float getRightVel() { return rightRearTalonEnc.getSelectedSensorVelocity(0) * Constants.EVEL_TO_FPS; }

    /**
     * @return Position as read by right encoder in Feet per Second
     */
    public float getRightPos() { return rightRearTalonEnc.getSelectedSensorPosition(0) * Constants.EPOS_TO_FEET; }

    /**
     * @return Position as read by left encoder in Feet per Second
     */
    public float getLeftPos() { return leftRearTalonEnc.getSelectedSensorPosition(0) * Constants.EPOS_TO_FEET; }


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
        setPID(this.kP, kI, kD);

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
        setPID(kP, this.kI, kD);
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
        setPID(kP, kI, this.kD);
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
        leftRearTalonEnc.config_kF(0, kF, Constants.INIT_TIMEOUT);
        rightRearTalonEnc.config_kF(0, kF, Constants.INIT_TIMEOUT);
    }

    public enum DriveStrategyType implements Nameable
    {
        VOLTAGE("VOLTAGE", (joystickLeft, joystickRight) -> {
            Robot.DRIVE_TRAIN.runMotors(joystickLeft, joystickRight);
        });
//        PID("PID", (joystickLeft, joystickRight) -> {
//
//            float leftVel = joystickLeft * Constants.MAX_FPS_SPEED * Constants.FPS_TO_EVEL;
//            float rightVel = joystickRight * Constants.MAX_FPS_SPEED * Constants.FPS_TO_EVEL;
//
////            Robot.DRIVE_TRAIN.leftRearTalonEnc.set(ControlMode.Velocity, joystickLeft * Constants.MAX_FPS_SPEED * Constants.FPS_TO_EVEL); // this
////            Robot.DRIVE_TRAIN.rightRearTalonEnc.set(ControlMode.Velocity, joystickRight * Constants.MAX_FPS_SPEED * Constants.FPS_TO_EVEL); // this
//
//            Robot.DRIVE_TRAIN.runMotors(ControlMode.Velocity, leftVel, rightVel);
//        });

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
     * A generic data structure to store a pair of objects.
     *
     * @param <L> The left value
     * @param <R> The right value
     */
    @Deprecated
    private static class Pair<L, R>
    {
        public L left;
        public R right;

        private String nameL;
        private String nameR;

        public Pair(L left, R right)
        {
            this.left = left;
            this.right = right;
            this.nameL = left.getClass().getSimpleName();
            this.nameR = right.getClass().getSimpleName();
        }

        public Pair() { }

        @Override
        public String toString()
        {
            return new StringBuilder(100 + nameL.length() + nameR.length()).append("Pair<").append(nameL).append(',')
                                                                           .append(nameR).append("> { \"left\": \"").append(left).append("\", \"right\": \"").append(right)
                                                                           .append("\" }").toString();
        }

        @Override
        public int hashCode()
        {
            return left.hashCode() * 13 + (right == null ? 0 : right.hashCode());
        }

        @Override
        public boolean equals(Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o instanceof Pair)
            {
                Pair pair = (Pair) o;
                return (left != null ? left.equals(pair.left) : pair.left == null)
                       && (left != null ? left.equals(pair.left) : pair.left == null);
            }
            return false;
        }
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

    /**
     * A data structure to store a pair of doubles.
     */
    private static class DoublePair
    {
        public double left;
        public double right;

        public DoublePair(double left, double right)
        {
            this.left = left;
            this.right = right;
        }

        public DoublePair() { }

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
            return (int) (((Double.doubleToLongBits(left) * 31) + (Double.doubleToLongBits(right) * 7)) % Integer.MAX_VALUE);
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
