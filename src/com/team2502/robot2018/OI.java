package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.*;
import com.team2502.robot2018.utils.DebouncedJoystickButton;
import com.team2502.robot2018.utils.baseoverloads.JoystickF;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The Operator Interface class
 */
public final class OI
{
    public static final JoystickF JOYSTICK_DRIVE_LEFT = new JoystickF(RobotMap.Joystick.JOYSTICK_DRIVE_LEFT);
    public static final JoystickF JOYSTICK_DRIVE_RIGHT = new JoystickF(RobotMap.Joystick.JOYSTICK_DRIVE_RIGHT);
    public static final JoystickF JOYSTICK_FUNCTION = new JoystickF(RobotMap.Joystick.JOYSTICK_FUNCTION);

    private static final Button INTAKE_IN = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_IN);
    private static final Button INTAKE_OUT = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_OUT);
    private static final Button OPEN_INTAKE = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.OPEN_INTAKE);

    private static final Button ELEV_UP = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.RAISE_ELEVATOR);
    private static final Button ELEV_DOWN = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.LOWER_ELEVATOR);

    private static final Button CLIMBER = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.CLIMBER);

    private static final Button SHIFT_GEARBOX_ELEV = new DebouncedJoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.SHIFT_GEARBOX_ELEV);

    private OI() { }

    public static void init()
    {
        // Elevator buttons
        ELEV_UP.whileHeld(new ElevatorCommand(1.0));
        ELEV_DOWN.whileHeld(new ElevatorCommand( -0.4));

        // Active Intake buttons
        INTAKE_IN.whileHeld(new ActiveIntakeCommand(0.6));
        INTAKE_OUT.whileHeld(new ActiveIntakeCommand(-0.6));

        OPEN_INTAKE.whenPressed(new GrabCommand());

        // Climber button (wait to re-implement until elevator is working properly

        SHIFT_GEARBOX_ELEV.whenPressed(new ShiftElevatorCommand());
    }

    public static boolean joysThreshold(double threshold, boolean above)
    {
        if(above) { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) > threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) > threshold; }
        else { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) < threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) < threshold; }
    }
}