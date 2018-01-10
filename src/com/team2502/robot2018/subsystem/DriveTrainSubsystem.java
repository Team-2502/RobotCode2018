package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.DashboardData;
import com.team2502.robot2018.OI;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.DriveCommand;
import com.team2502.robot2018.utils.DifferentialDriveF;
import com.team2502.robot2018.utils.SpeedControllerGroupF;
import com.team2502.robot2018.utils.WPI_TalonSRXF;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import logger.Log;

/**
 * Example Implementation, Many changes needed.
 */
public class DriveTrainSubsystem extends Subsystem implements DashboardData.DashboardUpdater
{
    private static final FloatPair SPEED_CONTAINER = new FloatPair();

    public final WPI_TalonSRXF leftFrontTalon;
    public final WPI_TalonSRXF leftRearTalonEnc;
    public final WPI_TalonSRXF rightFrontTalon;
    public final WPI_TalonSRXF rightRearTalonEnc;
    public final DifferentialDriveF drive;
    public final SpeedControllerGroupF spgLeft;
    public final SpeedControllerGroupF spgRight;

    private float lastLeft;
    private float lastRight;
    private boolean isNegativePressed;
    private boolean negative;

    public DriveTrainSubsystem()
    {
        lastLeft = 0.0F;
        lastRight = 0.0F;

        leftFrontTalon = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_FRONT_LEFT);
        leftRearTalonEnc = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_BACK_LEFT);
        rightFrontTalon = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_FRONT_RIGHT);
        rightRearTalonEnc = new WPI_TalonSRXF(RobotMap.Motor.DRIVE_TRAIN_BACK_RIGHT);

        // Add encoders (ask nicely for encoders on drivetrain)
        leftRearTalonEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.Motor.INIT_TIMEOUT);
        rightRearTalonEnc.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, RobotMap.Motor.INIT_TIMEOUT);

        spgLeft = new SpeedControllerGroupF(leftFrontTalon, leftRearTalonEnc);
        spgRight = new SpeedControllerGroupF(rightFrontTalon, rightRearTalonEnc);

        drive = new DifferentialDriveF(spgLeft, spgRight);

        drive.setSafetyEnabled(true);
        setTeleopSettings();
    }

    public void stop()
    { drive.stopMotor(); }

    private void setTeleopSettings(WPI_TalonSRXF talon)
    {
        talon.set(ControlMode.PercentOutput, 0);
        talon.configNominalOutputForward(0, RobotMap.Motor.INIT_TIMEOUT);
        talon.configNominalOutputReverse(0, RobotMap.Motor.INIT_TIMEOUT);
        talon.configPeakOutputForward(1.0, RobotMap.Motor.INIT_TIMEOUT);
        talon.configPeakOutputReverse(-1.0, RobotMap.Motor.INIT_TIMEOUT);
    }

    public void setTeleopSettings()
    {
        setTeleopSettings(leftFrontTalon);
        setTeleopSettings(rightFrontTalon);
        setTeleopSettings(leftRearTalonEnc);
        setTeleopSettings(rightRearTalonEnc);
    }

    public void setAutonSettings()
    {
        leftRearTalonEnc.set(ControlMode.Position, 0);
        rightRearTalonEnc.set(ControlMode.Position, 0);

        leftFrontTalon.follow(leftRearTalonEnc);
        rightFrontTalon.follow(rightRearTalonEnc);

        leftRearTalonEnc.setSensorPhase(true);
        rightRearTalonEnc.setSensorPhase(true);

        if(leftRearTalonEnc.getControlMode() != ControlMode.Position || rightRearTalonEnc.getControlMode() != ControlMode.Position || leftFrontTalon.getControlMode() != ControlMode.Follower || rightFrontTalon.getControlMode() != ControlMode.Follower)
        {
            Log.warn("setAutonSettings: One or more of the talons did not retain their control mode!\n" +
                     "Using the .set(int x) method will yield undesirable results!");

        }

    }

    /**
     * Drive the robot. The equation x=-y must be true for the robot to drive straight.
     * <br>
     * Make sure to set the motors according to the control mode. In auton, it's position. In teleop, it's percent voltage.
     *
     * @param x Units for the left side of drivetrain
     * @param y Units for the right side of drivetrain
     */
    public void runMotors(float x, float y) // double z
    {
        leftFrontTalon.set(x);
        leftRearTalonEnc.set(x);

        rightFrontTalon.set(-y);
        rightRearTalonEnc.set(-y);
    }

    public float turningFactor()
    {
        return Math.abs((float) OI.JOYSTICK_DRIVE_LEFT.getY() - (float) OI.JOYSTICK_DRIVE_RIGHT.getY());
    }

    @Override
    protected void initDefaultCommand()
    {
        setDefaultCommand(new DriveCommand());
    }

    /**
     * Used to gradually increase the speed of the robot.
     *
     * @param out The object to store the data in
     *
     * @return the speed of the robot
     */
    private FloatPair getSpeed(FloatPair out)
    {
        float joystickLevel;
        // Get the base speed of the robot
        if(negative) { joystickLevel = (float) -OI.JOYSTICK_DRIVE_RIGHT.getY(); }
        else { joystickLevel = (float) -OI.JOYSTICK_DRIVE_LEFT.getY(); }

        // Only increase the speed by a small amount
        float diff = joystickLevel - lastLeft;
        if(diff > 0.1D) { joystickLevel = lastLeft + 0.1F; }
        else if(diff < 0.1D) { joystickLevel = lastLeft - 0.1F; }

        lastLeft = joystickLevel;
        out.left = joystickLevel;

        if(negative) { joystickLevel = (float) -OI.JOYSTICK_DRIVE_LEFT.getY(); }
        else { joystickLevel = (float) -OI.JOYSTICK_DRIVE_RIGHT.getY(); }

        diff = joystickLevel - lastRight;
        if(diff > 0.1D) { joystickLevel = lastRight + 0.1F; }
        else if(diff < 0.1D) { joystickLevel = lastRight - 0.1F; }

        lastRight = joystickLevel;
        out.right = joystickLevel;

        // Sets the speed to 0 if the speed is less than 0.05 or larger than -0.05
        if(Math.abs(out.left) < 0.05D) { out.left = 0.0F; }
        if(Math.abs(out.right) < 0.05D) { out.right = 0.0F; }

        return out;
    }


    private FloatPair getSpeed()
    {
        return getSpeed(SPEED_CONTAINER);
    }

    public void drive()
    {
        FloatPair speed = getSpeed();

        Log.info("Left: " + String.format("%.02f", speed.right) + "\t\t Right: " + String.format("%.02f", speed.left));

        //reverse drive
        if(OI.JOYSTICK_DRIVE_LEFT.getRawButton(RobotMap.Joystick.Button.INVERSE_DRIVER_CONTROLS) && !isNegativePressed) { negative = !negative; }

        isNegativePressed = OI.JOYSTICK_DRIVE_LEFT.getRawButton(RobotMap.Joystick.Button.INVERSE_DRIVER_CONTROLS);

        if(!negative) { drive.tankDrive(-speed.left, -speed.right, true); }
        else { drive.tankDrive(speed.left, speed.right, true); }
    }

    public float avgVel()
    {
        return Math.abs((leftRearTalonEnc.getSelectedSensorVelocity(0) + rightRearTalonEnc.getSelectedSensorVelocity(0)) / 2.0F);
    }

    public void updateDashboard()
    {
        SmartDashboard.putNumber("Left Speed (ft/s)", leftRearTalonEnc.getSelectedSensorVelocity(0) * RobotMap.Motor.VEL_TO_FPS);
        SmartDashboard.putNumber("Left Pos (ft)", leftRearTalonEnc.getSelectedSensorPosition(0) * RobotMap.Motor.POS_TO_FEET);

        SmartDashboard.putNumber("Right Speed (ft/s)", rightRearTalonEnc.getSelectedSensorVelocity(0) * RobotMap.Motor.VEL_TO_FPS);
        SmartDashboard.putNumber("Right Pos (ft)", rightRearTalonEnc.getSelectedSensorPosition(0) * RobotMap.Motor.POS_TO_FEET);
    }

    /**
     * A generic data structure to store a pair of objects.
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
     * A generic data structure to store a pair of objects.
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
