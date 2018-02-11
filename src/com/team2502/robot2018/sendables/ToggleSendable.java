package com.team2502.robot2018.sendables;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.util.List;

public abstract class ToggleSendable implements Sendable
{
    private final List<Nameable> modes;
    private Nameable currentMode;

    /**
     * @param modes        a list of enums that implement nameable.
     * @param defaultMode the default modes.
     */
    public ToggleSendable(final List<Nameable> modes, final Nameable defaultMode)
    {
        this.modes = modes;
        this.currentMode = defaultMode;
    }

    public Nameable getCurrentMode()
    {
        return currentMode;
    }

    public List<Nameable> getModes()
    {
        return modes;
    }

    @Override
    public void initSendable(SendableBuilder builder)
    {
        for(Nameable nameable : modes)
        {
            builder.addBooleanProperty(nameable.getName(), () -> nameable == currentMode, value ->
            {
                if(nameable != currentMode)
                {
                    currentMode = nameable;
                }
            });
        }
    }
}
