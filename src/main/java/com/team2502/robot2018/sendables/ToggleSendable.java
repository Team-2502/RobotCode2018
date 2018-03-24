package com.team2502.robot2018.sendables;

import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

import java.util.List;

/**
 * A sendable that toggles between different modes
 *
 * <br>
 * You might find that {@link edu.wpi.first.wpilibj.smartdashboard.SendableChooser} fits your needs more
 *
 * @see SendableDriveStrategyType
 */
public abstract class ToggleSendable implements Sendable
{
    public final List<Nameable> modes;
    private Nameable currentMode;

    /**
     * @param modes       a list of enums that implement nameable.
     * @param defaultMode the default modes.
     */
    public ToggleSendable(final List<Nameable> modes, final Nameable defaultMode)
    {
        this.modes = modes;
        this.currentMode = defaultMode;
    }

    /**
     * @return The mode that has been selected
     */
    public Nameable getCurrentMode()
    {
        return currentMode;
    }

    /**
     * Put all the modes onto the smartdashboard
     *
     * @param builder
     */
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
