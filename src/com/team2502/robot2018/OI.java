package com.team2502.robot2018;

import com.team2502.robot2018.command.teleop.ElevatorCommand;
import com.team2502.robot2018.command.teleop.QuickCommand;
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

    public static final Button INTAKE_IN = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_IN);
    public static final Button INTAKE_OUT = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.INTAKE_OUT);
    public static final Button OPEN_INTAKE = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.OPEN_INTAKE);

    public static final Button ELEV_UP = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.RAISE_ELEVATOR);
    public static final Button ELEV_DOWN = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.LOWER_ELEVATOR);

    public static final Button CLIMBER = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.CLIMBER);


    public static final Button SHIFT_GEARBOX_ELEV = new JoystickButtonF(JOYSTICK_FUNCTION, RobotMap.Joystick.Button.SHIFT_GEARBOX_ELEV);

    private OI() { }

    public static void init()
    {
        // Elevator buttons

        // This is supposed to automatically engage
//        ELEV_UP.whenPressed(new QuickCommand(Robot.ELEVATOR, Robot.ELEVATOR::unlockElevator));

        ELEV_UP.whileHeld(new ElevatorCommand());
//        ELEV_UP.whenReleased(new QuickCommand(Robot.ELEVATOR, Robot.ELEVATOR::lockElevator));

//        ELEV_DOWN.whenPressed(new QuickCommand(Robot.ELEVATOR, Robot.ELEVATOR::unlockElevator));
        ELEV_DOWN.whileHeld(new QuickCommand(Robot.ELEVATOR, () -> Robot.ELEVATOR.moveElevator(-0.2)));
//        ELEV_DOWN.whenReleased(new QuickCommand(Robot.ELEVATOR, Robot.ELEVATOR::lockElevator));

        // Active Intake buttons
        INTAKE_IN.whileHeld(new QuickCommand(Robot.ACTIVE_INTAKE, () -> Robot.ACTIVE_INTAKE.runIntake(0.3)));
        INTAKE_IN.whenReleased(new QuickCommand(Robot.ACTIVE_INTAKE, Robot.ACTIVE_INTAKE::stop));

        INTAKE_OUT.whileHeld(new QuickCommand(Robot.ACTIVE_INTAKE, () -> Robot.ACTIVE_INTAKE.runIntake(-0.3)));
        INTAKE_OUT.whenReleased(new QuickCommand(Robot.ACTIVE_INTAKE, Robot.ACTIVE_INTAKE::stop));

        OPEN_INTAKE.whenPressed(new QuickCommand(Robot.ACTIVE_INTAKE, Robot.ACTIVE_INTAKE::toggleIntake));

        CLIMBER.whileHeld(new QuickCommand(Robot.ELEVATOR, () -> Robot.ELEVATOR.moveClimber(0.4)));
        CLIMBER.whenReleased(new QuickCommand(Robot.ELEVATOR, Robot.ELEVATOR::stopClimber));

        SHIFT_GEARBOX_ELEV.whenPressed(new QuickCommand(Robot.ELEVATOR, () -> Robot.ELEVATOR.toggleLock()));
    }

    public static boolean joysThreshold(double threshold, boolean above)
    {
        if(above) { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) > threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) > threshold; }
        else { return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) < threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) < threshold; }
    }
}