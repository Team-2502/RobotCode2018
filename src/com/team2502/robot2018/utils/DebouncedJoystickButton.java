package com.team2502.robot2018.utils;

import com.team2502.robot2018.utils.baseoverloads.GenericHIDF;
import com.team2502.robot2018.utils.baseoverloads.JoystickButtonF;

/**
 * A wrapper for Joystick Button
 */
public class DebouncedJoystickButton extends JoystickButtonF
{

    boolean activeCache = false;

    // If we are calling get() for the first time we cannot debounce
    boolean debounced = false;

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
     * @return
     * @deprecated Use getDebounced()
     */
    @Override
    public boolean get()
    {
        boolean status = super.get();
        boolean toReturn = debounced ? status && activeCache : status;
        if(!debounced)
            debounced = true;
        activeCache = status;
        return toReturn;
    }

    public boolean getRaw()
    {
        return super.get();
    }

    public boolean isDebounced()
    {
        return debounced;
    }
}
