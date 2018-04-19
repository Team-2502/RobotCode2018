package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.*;
import com.team2502.robot2018.command.teleop.DriveCommand;
import com.team2502.robot2018.sendables.Nameable;
import com.team2502.robot2018.sendables.PIDTunable;
import com.team2502.robot2018.sendables.SendableDriveStrategyType;
import com.team2502.robot2018.sendables.SendablePIDTuner;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;

import static com.team2502.robot2018.Constants.Physical.DriveTrain.FEET_TO_EPOS_DT;
import static com.team2502.robot2018.Constants.Physical.DriveTrain.FPS_TO_EVEL_DT;

/**
 * Example Implementation, Many changes needed.
 */
public class DriveTrainSubsystem extends Subsystem implements DashboardData.DashboardUpdater, PIDTunable
{
    private static final FloatPair SPEED_CONTAINER = new FloatPair();
    private static final float ACCELERATION_DIFF = 0.5F;
    private static final float DIFF_COMPARISON = 0.15F;
    private final WPI_TalonSRX leftFrontTalonEnc;
    private final WPI_TalonSRX leftRearTalon;
    private final WPI_TalonSRX rightFrontTalonEnc;
    private final WPI_TalonSRX rightRearTalon;
    /**
     * Represents our drivetrain
     */
    private final DifferentialDrive drive;
    /**
     * Represents the left side of the drivetrain
     */
    private final SpeedControllerGroup spgLeft;
    /**
     * Represents the right side of the drivetrain
     */
    private final SpeedControllerGroup spgRight;
    /**
     * Allows the PID of the drivetrain to be tuned from shuffleboard
     */
    private final SendablePIDTuner pidTuner;
    private double kP = 1D;
    private double kI = 0.0;
    private double kD = 0;
    private double kF = 0.2;

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

    /**
     * Stops the drivetrain
     */
    public void stop() { drive.stopMotor(); }

    /**
     * Prepare the talon for driving in teleop
     *
     * @param talon the talon in question
     */
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
     * Load purepursuit points into the talons, where purepursuit points are in feet
     *
     * @param trajLeft  The purepursuit for the left side
     * @param trajRight The purepursuit for the right side
     */
    public void loadTrajectoryPoints(Trajectory trajLeft, Trajectory trajRight, double dir)
    {
        setMotionProfileSettings();
        loadTrajectoryPoints(trajLeft, leftFrontTalonEnc, dir);
        loadTrajectoryPoints(trajRight, rightFrontTalonEnc, dir);
    }

    /**
     * Sets auton settings and makes the talons update faster
     */
    private void setMotionProfileSettings()
    {
        DriverStation.getInstance().reportWarning("Setting MP Settings", false);
        setAutonSettings();
        leftFrontTalonEnc.changeMotionControlFramePeriod(Constants.SRXProfiling.PERIOD_MS / 2);
        rightFrontTalonEnc.changeMotionControlFramePeriod(Constants.SRXProfiling.PERIOD_MS / 2);

        leftFrontTalonEnc.config_kF(0, 0.2687473379, Constants.INIT_TIMEOUT);
        rightFrontTalonEnc.config_kF(0, 0.2687473379, Constants.INIT_TIMEOUT);
    }

    /**
     * Make the talons update at their normal rate. Doing so reduces CAN bus utilization.
     */
    public void resetTalonControlFramePeriod()
    {
        leftFrontTalonEnc.changeMotionControlFramePeriod(20);
        rightFrontTalonEnc.changeMotionControlFramePeriod(20);
    }

    /**
     * Update the given status with the status of the left side
     * <p>
     * Hopefully, the status of the left and right side should be the same
     *
     * @param status The status reference to update
     */
    public void updateStatus(MotionProfileStatus status)
    {
        leftFrontTalonEnc.getMotionProfileStatus(status);
    }

    public void clearMotionProfileHasUnderrun()
    {
        leftFrontTalonEnc.clearMotionProfileHasUnderrun(Constants.LOOP_TIMEOUT);
        rightFrontTalonEnc.clearMotionProfileHasUnderrun(Constants.LOOP_TIMEOUT);
    }

    /**
     * Load some purepursuit points into a particular talon
     *
     * @param traj  The purepursuit points in question, with points in feet
     * @param talon The talon in question
     */
    private void loadTrajectoryPoints(Trajectory traj, WPI_TalonSRX talon, double dir)
    {
        DriverStation.reportWarning("Clearing stuff", false);
        talon.clearMotionProfileTrajectories();
        talon.clearMotionProfileHasUnderrun(Constants.LOOP_TIMEOUT);
        talon.configMotionProfileTrajectoryPeriod(Constants.SRXProfiling.BASE_TRAJ_PERIOD, Constants.INIT_TIMEOUT);

        System.out.println("Pushing 1 traj point . . .");
        for(int i = 0; i < traj.segments.length; i++)
        {
            Trajectory.Segment segment = traj.get(i);
            TrajectoryPoint point = new TrajectoryPoint();

            // Trajectory headings are in radians, but SRX wants them in degrees
            point.headingDeg = Pathfinder.r2d(segment.heading);

            point.isLastPoint = i + 1 == traj.segments.length;

            point.timeDur = Constants.SRXProfiling.PERIOD;

            //todo: shift by current pos
            point.position = fakeToRealEncUnits((float) segment.position * FEET_TO_EPOS_DT) * dir + talon.getSelectedSensorPosition(0);
            point.velocity = fakeToRealEncUnits((float) segment.velocity * FPS_TO_EVEL_DT) * dir;

            point.zeroPos = Constants.SRXProfiling.USE_RELATIVE_COORDS && i == 0;

            point.profileSlotSelect0 = 0;
            point.profileSlotSelect1 = 0;

            DriverStation.reportWarning("Clearing stuff", false);
            talon.pushMotionProfileTrajectory(point);

        }

    }

    private void loadTrajectoryPoints(TrajectoryPoint[] profile, WPI_TalonSRX talon)
    {
        talon.clearMotionProfileTrajectories();
        talon.clearMotionProfileHasUnderrun(Constants.LOOP_TIMEOUT);
        talon.configMotionProfileTrajectoryPeriod(Constants.SRXProfiling.BASE_TRAJ_PERIOD, Constants.INIT_TIMEOUT);

        for(TrajectoryPoint point : profile)
        {
            //if we are using absolute coordinates
            if(!Constants.SRXProfiling.USE_RELATIVE_COORDS)
            {
                // then shift our robot's position by the necesary amount
                point.position = point.position + talon.getSelectedSensorPosition(0);
            }
            talon.pushMotionProfileTrajectory(point);
        }
    }

    /**
     * Pushes points from the top level SRX buffer into the bottom level one
     * <p>
     * This must be called repeatedly in order for stuff to work
     */
    public void processMotionProfileBuffer()
    {
        leftFrontTalonEnc.processMotionProfileBuffer();
        rightFrontTalonEnc.processMotionProfileBuffer();
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

        Robot.TRANSMISSION_SOLENOID.setHighGear(false);
    }

    /**
     * Set the encoder settings for the encoder talons, and make the other talons follow the encoder talons
     */
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
        Robot.TRANSMISSION_SOLENOID.setHighGear(true);
        // Set high gear
    }

    /**
     * Update the PID
     */
    @Override
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
    private void runMotors(ControlMode controlMode, float leftWheel, float rightWheel) // double z
    {
        // setting slaves as the talons w/ encoders is the only way it works ¯\_(ツ)_/¯
        leftRearTalon.follow(leftFrontTalonEnc);
        rightRearTalon.follow(rightFrontTalonEnc);

        leftFrontTalonEnc.set(controlMode, leftWheel);
        rightFrontTalonEnc.set(controlMode, rightWheel);
    }

    /**
     * Run the drivetrain at a percent voltage
     *
     * @param leftWheel  Percent voltage to left side
     * @param rightWheel Percent voltage to right side
     * @see DriveTrainSubsystem#runMotors(ControlMode, float, float)
     */
    public void runMotorsVoltage(float leftWheel, float rightWheel)
    {
        runMotors(ControlMode.PercentOutput, leftWheel, rightWheel);
    }

    /**
     * Run the drivetrain at a particular speed
     *
     * @param leftWheel  Speed of the left side (ft/s)
     * @param rightWheel Speed of the right side (ft/s)
     * @see DriveTrainSubsystem#runMotors(ControlMode, float, float)
     */
    public void runMotorsVelocity(float leftWheel, float rightWheel)
    {
        float left = fakeToRealEncUnits(leftWheel * Constants.Physical.DriveTrain.FPS_TO_EVEL_DT);
        float right = fakeToRealEncUnits(rightWheel * Constants.Physical.DriveTrain.FPS_TO_EVEL_DT);
        Robot.writeLog("left: %.2f, right: %.2f", 1, left, right);
        runMotors(ControlMode.Velocity, left, right);
    }

    /**
     * Run the drivetrain at a particular speed in raw enc units
     *
     * @param leftWheel  Speed of the left side (enc units/ 100 ms)
     * @param rightWheel Speed of the right side (enc units/ 100 ms)
     * @see DriveTrainSubsystem#runMotors(ControlMode, float, float)
     * @see DriveTrainSubsystem#runMotorsVelocity(float, float)
     */
    public void runMotorsRawVelocity(float leftWheel, float rightWheel)
    {
        runMotors(ControlMode.Velocity, leftWheel, rightWheel);
    }

    /**
     * Run the drivetrain to a particular distance
     *
     * @param leftWheel  Distance of the left side (ft)
     * @param rightWheel Distance of the right side (ft)
     * @see DriveTrainSubsystem#runMotors(ControlMode, float, float)
     */
    public void runMotorsPosition(float leftWheel, float rightWheel)
    {
        float left = fakeToRealEncUnits(leftWheel * Constants.Physical.DriveTrain.FEET_TO_EPOS_DT);
        float right = fakeToRealEncUnits(rightWheel * Constants.Physical.DriveTrain.FEET_TO_EPOS_DT);
        runMotors(ControlMode.Position, left, right);
    }

    /**
     * Run the drivetrain to a particular distance in raw enc units
     *
     * @param leftWheel  Distance of the left side (enc units)
     * @param rightWheel Distance of the right side (enc units)
     * @see DriveTrainSubsystem#runMotors(ControlMode, float, float)
     * @see DriveTrainSubsystem#runMotorsPosition(float, float) (float, float)
     */
    public void runMotorsRawPosition(float leftWheel, float rightWheel)
    {
        runMotors(ControlMode.Position, leftWheel, rightWheel);
    }


    /**
     * Drive the robot using ControlMode.PercentOutput. The equation leftWheel=-rightWheel must be true for the robot to setElevatorPV straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's {@link ControlMode#Position}. In teleop, it's {@link ControlMode#PercentOutput}.
     *
     * @param leftWheel  Units for the left side of drivetrain
     * @param rightWheel Units for the right side of drivetrain
     */
    private void runMotorsTankDrive(float leftWheel, float rightWheel) // double z
    {
        drive.tankDrive(leftWheel, rightWheel, true);
    }

    /**
     * @return Difference between left and right joystick. Lets you know how much the driver is trying to turn.
     */
    public double turningFactor()
    {
        return Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY() - OI.JOYSTICK_DRIVE_RIGHT.getY());
    }

    /**
     * Disable talons, stopping them.
     *
     * @see DriveTrainSubsystem#stop()
     * @deprecated
     */
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
        joystickLevel = (float) OI.JOYSTICK_DRIVE_LEFT.getY();

        //TODO: Eliminate this redundant code
        // Only increase the speed by a small amount
        float diff = joystickLevel - lastLeft;
        if(diff > DIFF_COMPARISON) { joystickLevel = lastLeft + ACCELERATION_DIFF; }
        else if(diff < -DIFF_COMPARISON) { joystickLevel = lastLeft - ACCELERATION_DIFF; }
        lastLeft = joystickLevel;
        out.left = joystickLevel;

        joystickLevel = (float) OI.JOYSTICK_DRIVE_RIGHT.getY();

        diff = joystickLevel - lastRight;
        if(diff > DIFF_COMPARISON) { joystickLevel = lastRight + ACCELERATION_DIFF; }
        else if(diff < -DIFF_COMPARISON) { joystickLevel = lastRight - ACCELERATION_DIFF; }
        lastRight = joystickLevel;
        out.right = joystickLevel;

        // Sets the speed to 0 if the speed is less than 0.05 and larger than -0.05
        if(Math.abs(out.left) < 0.05F) { out.left = 0.0F; }
        if(Math.abs(out.right) < 0.05F) { out.right = 0.0F; }

        return out;
    }

    /**
     * Used to gradually increase the speed of the robot in teleop
     *
     * @return
     */
    private FloatPair getSpeedTank()
    {
        return getSpeedTank(SPEED_CONTAINER);
    }

    /**
     * Drive the robot in teleop
     */
    public void drive()
    {
        FloatPair speed = getSpeedTank();
        SmartDashboard.putNumber("speedL", -speed.left);
        SmartDashboard.putNumber("speedR", -speed.right);

        Nameable currentMode = SendableDriveStrategyType.INSTANCE.getCurrentMode();

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

    /**
     * @return Tangent speed
     * @see DriveTrainSubsystem#getTanVel()
     */
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
    public float getLeftVel()
    { return fakeToRealWheelRev(getLeftRawVel() * Constants.Physical.DriveTrain.FAKE_EVEL_TO_FPS_DT); }

    /**
     * Assuming we are in a PID loop, return the average error for the 2 sides of the drivetrain
     *
     * @return the average error
     */
    public double getAvgEncLoopError()
    {
        return (leftFrontTalonEnc.getClosedLoopError(0) + rightFrontTalonEnc.getClosedLoopError(0)) / 2;
    }

    /**
     * @return Turns "fake" units into real wheel revolutions
     */
    public float fakeToRealWheelRev(float wheelRev)
    {
        if(Robot.TRANSMISSION_SOLENOID.isHigh()) { return wheelRev / Constants.Physical.DriveTrain.WHEEL_REV_TO_ENC_REV_HIGH; }
        else { return wheelRev / Constants.Physical.DriveTrain.WHEEL_REV_TO_ENC_REV_LOW; }
    }

    /**
     * @return Turns "fake" units into real encoder units
     */
    public float fakeToRealEncUnits(float rawUnits)
    {
        return Robot.TRANSMISSION_SOLENOID.isHigh() ? rawUnits * Constants.Physical.DriveTrain.WHEEL_REV_TO_ENC_REV_HIGH : rawUnits * Constants.Physical.DriveTrain.WHEEL_REV_TO_ENC_REV_LOW;
    }

    /**
     * @return Velocity as read by right encoder in ft/s
     */
    public float getRightVel() { return fakeToRealWheelRev(getRightRawVel() * Constants.Physical.DriveTrain.FAKE_EVEL_TO_FPS_DT); }

    /**
     * @return Right side velocity in enc units / 100 ms
     */
    public int getRightRawVel() { return rightFrontTalonEnc.getSelectedSensorVelocity(0); }

    /**
     * @return Left side velocity in enc units / 100 ms
     */
    public int getLeftRawVel() { return leftFrontTalonEnc.getSelectedSensorVelocity(0); }

    /**
     * @return Position as read by right encoder in Feet per Second
     */
    public float getRightPos() { return fakeToRealWheelRev(getRightPosRaw() * Constants.Physical.DriveTrain.EPOS_TO_FEET_DT); }

    /**
     * @return Position as read by left encoder in Feet per Second
     */
    public float getLeftPos() { return fakeToRealWheelRev(getLeftPosRaw() * Constants.Physical.DriveTrain.EPOS_TO_FEET_DT); }

    /**
     * @return Left side position in enc units
     */
    public float getLeftPosRaw() { return leftFrontTalonEnc.getSelectedSensorPosition(0);}

    /**
     * @return Right side position in enc units
     */
    public float getRightPosRaw() { return rightFrontTalonEnc.getSelectedSensorPosition(0);}

    /**
     * @param inches
     * @return
     * @deprecated think this is wrong
     */
    public float inchesToEncUnits(float inches)
    {
        float feet = inches / 12;

        // TODO: is this right???
        return fakeToRealEncUnits(feet * Constants.Physical.DriveTrain.FEET_TO_EPOS_DT);
//        return -99;
    }


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

    /**
     * Enable, disable, or hold a motion profiling position
     *
     * @param motionProfilingState Something from the enum {@link SetValueMotionProfile}
     */
    public void setMotionProfilingState(SetValueMotionProfile motionProfilingState)
    {
        runMotors(ControlMode.MotionProfile, motionProfilingState.value, motionProfilingState.value);
    }

    /**
     * Drive strategies that may be used
     */
    public enum DriveStrategyType implements Nameable
    {
        VOLTAGE("VOLTAGE", (joystickLeft, joystickRight) -> {
            Robot.DRIVE_TRAIN.drive.tankDrive(joystickLeft, joystickRight);
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

    @FunctionalInterface
    private interface DriveStrategy
    {
        /**
         * Given left and right joystick y levels, run the motors
         *
         * @param joystickLeft  Left joy y level
         * @param joystickRight Right joy y level
         */
        void drive(float joystickLeft, float joystickRight);
    }

    /**
     * A data structure to store a pair of floats.
     */
    private static class FloatPair //TODO: Replace with ImmutableVector2f
    {
        public float left;
        public float right;

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