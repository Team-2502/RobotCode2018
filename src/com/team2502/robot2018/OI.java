package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.*;
import com.team2502.robot2018.command.test.group.FullSystemsTestCommand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The Operator Interface class
 */
public final class OI
{
    public static final Joystick JOYSTICK_DRIVE_LEFT = new Joystick(RobotMap.Joystick.JOYSTICK_DRIVE_LEFT);
    public static final Joystick JOYSTICK_DRIVE_RIGHT = new Joystick(RobotMap.Joystick.JOYSTICK_DRIVE_RIGHT);
    public static final Joystick JOYSTICK_FUNCTION = new Joystick(RobotMap.Joystick.JOYSTICK_FUNCTION);

    private static final Button INTAKE_IN = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_IN);
    private static final Button INTAKE_OUT_SLOW = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_OUT_SLOW);
    private static final Button INTAKE_OUT_FAST = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_OUT_FAST);
    private static final Button OPEN_INTAKE = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.OPEN_INTAKE);

    private static final Button ELEV_UP = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.RAISE_ELEVATOR);
    private static final Button ELEV_DOWN = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.LOWER_ELEVATOR);

    private static final Button CLIMBER = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.CLIMBER);

    private static final Button TOGGLE_TRANSMISSION = new JoystickButton(JOYSTICK_DRIVE_RIGHT, RobotMap.Joystick.Button.TOGGLE_TRANSMISSION);

    private static final Button DEPLOY_BUTTERFLY = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.DEPLOY_BUTTERFLY);

    private static final Button SHIFT_GEARBOX_ELEV = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.SHIFT_GEARBOX_ELEV);

    private static final Button RUN_TESTS = new JoystickButton(JOYSTICK_DRIVE_LEFT, RobotMap.Joystick.Button.RUN_DEBUG_TESTS);

    private static final Button CALIBRATE_ELEV_ENCODER = new JoystickButton(JOYSTICK_DRIVE_RIGHT, RobotMap.Joystick.Button.CALIBRATE_ELEV_ENCODER);

    private static final Button TOGGLE_CARRIAGE_BRAKE = new JoystickButton(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.TOGGLE_CARRIAGE_BRAKE);

    static
    {
        // Elevator buttons
        ELEV_UP.whileHeld(new ElevatorCommand(1.0));
        ELEV_DOWN.whileHeld(new ElevatorCommand(-0.5));

        // Active Intake buttons
        INTAKE_IN.whileHeld(new ActiveIntakeCommand(1.0));
        INTAKE_OUT_SLOW.whileHeld(new ActiveIntakeCommand(-0.5));
        INTAKE_OUT_FAST.whileHeld(new ActiveIntakeCommand(-1.0));

        OPEN_INTAKE.whenPressed(new ToggleIntakeCommand());

        // Climber button (wait to re-implement until elevator is working properly
        CLIMBER.whileHeld(new ClimberCommand(1.0));

        // Pneumatics
        SHIFT_GEARBOX_ELEV.whenPressed(new ShiftElevatorCommand());
        DEPLOY_BUTTERFLY.whenPressed(new ButterflySetCommand(true));
        DEPLOY_BUTTERFLY.whenReleased(new ButterflySetCommand(false));
        TOGGLE_TRANSMISSION.whenPressed(new TransmissionCommand());

        RUN_TESTS.whenPressed(new FullSystemsTestCommand());

        CALIBRATE_ELEV_ENCODER.whenPressed(new QuickCommand(Robot.ELEVATOR::calibrateEncoder));
        TOGGLE_CARRIAGE_BRAKE.whenPressed(new ToggleCarriageBrake());
    }

    public static void init() {}

    public static boolean joysThreshold(double threshold, boolean above)
    {
        if(above) { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) > threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) > threshold; }
        else { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) < threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) < threshold; }
    }
}