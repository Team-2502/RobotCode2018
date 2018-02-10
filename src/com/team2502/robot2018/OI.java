package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.ElevatorCommand;
import com.team2502.robot2018.command.teleop.RunAMotor;
import com.team2502.robot2018.utils.JoystickButtonF;
import com.team2502.robot2018.utils.JoystickF;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The Operator Interface class. NOT the wrong spelling of I/O!
 */
public final class OI
{
    public static final JoystickF JOYSTICK_DRIVE_LEFT = new JoystickF(RobotMap.Joystick.JOYSTICK_DRIVE_LEFT);
    public static final JoystickF JOYSTICK_DRIVE_RIGHT = new JoystickF(RobotMap.Joystick.JOYSTICK_DRIVE_RIGHT);
    public static final JoystickF JOYSTICK_FUNCTION = new JoystickF(RobotMap.Joystick.JOYSTICK_FUNCTION);

    public static final Button LEFT_INTAKE_FORWARD = new JoystickButtonF(JOYSTICK_FUNCTION, 5);
    public static final Button LEFT_INTAKE_BACKWARD = new JoystickButtonF(JOYSTICK_FUNCTION, 6);
    public static final Button RIGHT_INTAKE_FORWARD = new JoystickButtonF(JOYSTICK_FUNCTION, 7);
    public static final Button RIGHT_INTAKE_BACKWARD = new JoystickButtonF(JOYSTICK_FUNCTION, 8);

    public static final Button ELEV_UP = new JoystickButtonF(JOYSTICK_DRIVE_LEFT, 10);
    public static final Button SHIFT_GEARBOX_ELEV = new JoystickButtonF(JOYSTICK_FUNCTION, 11);

    private OI() { }

    public static void init()
    {
        LEFT_INTAKE_FORWARD.whileHeld(new RunAMotor(Robot.ACTIVE_INTAKE.leftIntake, Constants.INTAKE_SPEED_PERCENT_LIMIT));
        LEFT_INTAKE_BACKWARD.whileHeld(new RunAMotor(Robot.ACTIVE_INTAKE.leftIntake, -Constants.INTAKE_SPEED_PERCENT_LIMIT));
        RIGHT_INTAKE_FORWARD.whileHeld(new RunAMotor(Robot.ACTIVE_INTAKE.rightIntake, Constants.INTAKE_SPEED_PERCENT_LIMIT));
        RIGHT_INTAKE_BACKWARD.whileHeld(new RunAMotor(Robot.ACTIVE_INTAKE.rightIntake, -Constants.INTAKE_SPEED_PERCENT_LIMIT));
        ELEV_UP.whileHeld(new ElevatorCommand());
    }

    public static boolean joysThreshold(double threshold, boolean above)
    {
        if(above) { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) > threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) > threshold; }
        else { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) < threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) < threshold; }
    }
}