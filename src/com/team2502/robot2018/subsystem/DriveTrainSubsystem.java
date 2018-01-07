package com.team2502.robot2018.subsystem;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.team2502.robot2018.OI;
import com.team2502.robot2018.RobotMap;
import com.team2502.robot2018.command.teleop.DriveCommand;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * Example Implementation, Many changes needed.
 */
public class DriveTrainSubsystem extends Subsystem
{
    private static final Pair<Double, Double> SPEED_CONTAINER = new Pair<Double, Double>();

    public final WPI_TalonSRX leftFrontTalon;
    public final WPI_TalonSRX leftRearTalonEnc;
    public final WPI_TalonSRX rightFrontTalon;
    public final WPI_TalonSRX rightRearTalonEnc;
    public final DifferentialDrive drive;
    public final SpeedControllerGroup spgLeft;
    public final SpeedControllerGroup spgRight;


    private double lastLeft;
    private double lastRight;
    private boolean isNegativePressed;
    private boolean negative;

    public DriveTrainSubsystem()
    {
        lastLeft = 0.0D;
        lastRight = 0.0D;

        leftFrontTalon = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_FRONT_LEFT);
        leftRearTalonEnc = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_BACK_LEFT_ENC);
        rightFrontTalon = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_FRONT_RIGHT);
        rightRearTalonEnc = new WPI_TalonSRX(RobotMap.Motor.DRIVE_TRAIN_BACK_RIGHT_ENC);

        spgLeft = new SpeedControllerGroup(leftFrontTalon, leftRearTalonEnc);
        spgRight = new SpeedControllerGroup(rightFrontTalon, rightRearTalonEnc);

        drive = new DifferentialDrive(spgLeft, spgRight);

        drive.setSafetyEnabled(true);
    }

    public void stop()
    {
        drive.stopMotor();
    }

    public double turningFactor() { return Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY() - OI.JOYSTICK_DRIVE_RIGHT.getY());}

    @Override
    protected void initDefaultCommand() { setDefaultCommand(new DriveCommand()); }

    /**
     * Used to gradually increase the speed of the robot.
     *
     * @param out The object to store the data in
     * @return the speed of the robot
     */
    private Pair<Double, Double> getSpeed(Pair<Double, Double> out)
    {
        double joystickLevel;
        // Get the base speed of the robot
        if(negative) { joystickLevel = -OI.JOYSTICK_DRIVE_RIGHT.getY(); }
        else { joystickLevel = -OI.JOYSTICK_DRIVE_LEFT.getY(); }

        // Only increase the speed by a small amount
        double diff = joystickLevel - lastLeft;
        if(diff > 0.1D) { joystickLevel = lastLeft + 0.1D; }
        else if(diff < 0.1D) { joystickLevel = lastLeft - 0.1D; }

        lastLeft = joystickLevel;
        out.left = joystickLevel;

        if(negative) { joystickLevel = -OI.JOYSTICK_DRIVE_LEFT.getY(); }
        else { joystickLevel = -OI.JOYSTICK_DRIVE_RIGHT.getY(); }

        diff = joystickLevel - lastRight;
        if(diff > 0.1D) { joystickLevel = lastRight + 0.1D; }
        else if(diff < 0.1D) { joystickLevel = lastRight - 0.1D; }

        lastRight = joystickLevel;
        out.right = joystickLevel;

        // Sets the speed to 0 if the speed is less than 0.05 or larger than -0.05
        if(Math.abs(out.left) < 0.05D) { out.left = 0.0D; }
        if(Math.abs(out.right) < 0.05D) { out.right = 0.0D; }

        return out;
    }

    private Pair<Double, Double> getSpeed() { return getSpeed(SPEED_CONTAINER); }

    public void drive()
    {
        Pair<Double, Double> speed = getSpeed();

        //reverse drive
        if(OI.JOYSTICK_DRIVE_LEFT.getRawButton(RobotMap.UNDEFINED) && !isNegativePressed) { negative = !negative; }

        isNegativePressed = OI.JOYSTICK_DRIVE_LEFT.getRawButton(RobotMap.UNDEFINED);

        if(negative) { drive.tankDrive(-speed.left, -speed.right, true); }
        else { drive.tankDrive(speed.left, speed.right, true); }
    }

    public double avgVel()
    {
        return Math.abs((leftRearTalonEnc.getEncVelocity() + rightRearTalonEnc.getEncVelocity()) / 2);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Pair<L, R>
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

        public Pair() {}

        @Override
        public String toString()
        {
            return new StringBuilder(100 + nameL.length() + nameR.length()).append("Pair<").append(nameL).append(',')
                                                                           .append(nameR).append("> { \"left\": \"").append(left).append("\", \"right\": \"").append(right)
                                                                           .append("\" }").toString();
        }

        @Override
        public int hashCode() { return left.hashCode() * 13 + (right == null ? 0 : right.hashCode()); }

        @Override
        public boolean equals(Object o)
        {
            if(this == o) { return true; }
            if(o instanceof Pair)
            {
                Pair pair = (Pair) o;
                return (left != null ? left.equals(pair.left) : pair.left == null)
                       && (left != null ? left.equals(pair.left) : pair.left == null);
            }
            return false;
        }
    }

}