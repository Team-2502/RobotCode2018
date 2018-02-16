package com.team2502.robot2018.utils;

import com.team2502.robot2018.utils.baseoverloads.GenericHIDF;
import com.team2502.robot2018.utils.baseoverloads.JoystickButtonF;

/**
 * A wrapper for Joystick Button
 */
public class DebouncedJoystickButton extends JoystickButtonF
{

    boolean activeCache = false;

    /**
     * Create a joystick button for triggering commands.
     *
     * @param joystick     The GenericHID object that has the button (e.g. Joystick, KinectStick,
     *                     etc)
     * @param buttonNumber The button number (see {@link GenericHID#getRawButton(int) }
     */
    public DebouncedJoystickButton(GenericHIDF joystick, int buttonNumber)
    {
        super(joystick, buttonNumber);
    }

    /**
     * @deprecated Use getDebounced()
     * @return
     */
    @Override
    public boolean get()
    {
        boolean status = super.get();
        boolean toReturn = false;
        if(status && activeCache)
        {
            toReturn = true;
        }
        activeCache = status;
        return toReturn;
    }

    public boolean getRaw()
    {
        return super.get();
    }

}
