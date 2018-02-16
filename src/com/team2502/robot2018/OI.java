package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.*;
import com.team2502.robot2018.command.teleop.CommandGroups.MoveElevatorDownCommandGroup;
import com.team2502.robot2018.command.teleop.CommandGroups.MoveElevatorUpCommandGroup;
import com.team2502.robot2018.utils.baseoverloads.JoystickButtonF;
import com.team2502.robot2018.utils.baseoverloads.JoystickF;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * The Operator Interface class
 */
public final class OI
{
    public static final JoystickF JOYSTICK_DRIVE_LEFT = new JoystickF(RobotMap.Joystick.JOYSTICK_DRIVE_LEFT);
    public static final JoystickF JOYSTICK_DRIVE_RIGHT = new JoystickF(RobotMap.Joystick.JOYSTICK_DRIVE_RIGHT);
    public static final JoystickF JOYSTICK_FUNCTION = new JoystickF(RobotMap.Joystick.JOYSTICK_FUNCTION);

    public static final Button INTAKE_IN = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_IN);
    public static final Button INTAKE_OUT = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_OUT);
    public static final Button OPEN_INTAKE = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.OPEN_INTAKE);

    public static final Button ELEV_UP = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.RAISE_ELEVATOR);
    public static final Button ELEV_DOWN = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.LOWER_ELEVATOR);

    public static final Button CLIMBER = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.CLIMBER);
//    public static final Button CLIMBER_DOWN = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.UNDEFINED);


    public static final Button SHIFT_GEARBOX_ELEV = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.SHIFT_GEARBOX_ELEV);

    private OI() { }

    public static void init()
    {
        // Active Intake buttons
        INTAKE_IN.whileHeld(new ActiveIntakeCommand(0.6));
        INTAKE_OUT.whileHeld(new ActiveIntakeCommand(-0.6));

        OPEN_INTAKE.whenPressed(new GrabCommand());

        CLIMBER.whileHeld(new ClimberCommand(1));
//        CLIMBER_DOWN.whileHeld(new ClimberCommand(-1));


        SHIFT_GEARBOX_ELEV.whenPressed(new ShiftElevatorCommand());
    }

    public static boolean joysThreshold(double threshold, boolean above)
    {
        if(above) { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) > threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) > threshold; }
        else { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) < threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) < threshold; }
    }
}