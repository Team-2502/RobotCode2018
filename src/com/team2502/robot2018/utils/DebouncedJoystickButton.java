package com.team2502.robot2018.utils;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * A wrapper for Joystick Button
 * <p>
 * Deprecated because joysticks should not be used like this
 * except in exceptional situations.
 */
@Deprecated
public class DebouncedJoystickButton extends JoystickButton
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
    public DebouncedJoystickButton(GenericHID joystick, int buttonNumber)
    {
        super(joystick, buttonNumber);
    }


    /**
     * @return the debounced output of the button
     */
    public boolean getDebounced()
    {
        boolean status = super.get();
        boolean toReturn = debounced ? status && activeCache : status;
        debounced = true;
        activeCache = status;
        return toReturn;
    }

    /**
     * @return
     * @deprecated Use getDebounced()
     */
    public boolean get()
    {
        return super.get();
    }

    public boolean isDebounced()
    {
        return debounced;
    }
}
