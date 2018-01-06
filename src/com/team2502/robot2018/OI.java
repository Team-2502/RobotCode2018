package com.team2502.robot2018;

import edu.wpi.first.wpilibj.Joystick;

public final class OI
{
    public static final Joystick JOYSTICK_DRIVE_LEFT = new Joystick(RobotMap.Joystick.JOYSTICK_DRIVE_LEFT);
    public static final Joystick JOYSTICK_DRIVE_RIGHT = new Joystick(RobotMap.Joystick.JOYSTICK_DRIVE_RIGHT);
    public static final Joystick JOYSTICK_FUNCTION = new Joystick(RobotMap.Joystick.JOYSTICK_FUNCTION);

    static
    {
    }

    public static void init() {}

    public static boolean joysThreshold(double threshold, boolean above)
    {
        if(above)
        {
            return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) > threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) > threshold;
        }
        else
        {
            return Math.abs(OI.JOYSTICK_DRIVE_RIGHT.getY()) < threshold && Math.abs(OI.JOYSTICK_DRIVE_LEFT.getY()) < threshold;
        }
    }

    private OI() {}
}