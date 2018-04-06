package com.team2502.robot2018.command.autonomous.ingredients;

/**
 * Let drive team choose what they want to achieve in auto
 * <p>
 * TODO: Implement multi-cube autos
 */
public enum AutonStrategy
{
    SWITCH("Switch"),
    CENTER_STRATEGY("Switch-Switch-Vault"),
    SWITCH_TWICE("Switch-Switch"), // Used for center
    SCALE("Scale"),
    SCALE_TWICE("Scale-Scale"),
    SCALE_SWITCH("Scale-Switch"),
    STRAIGHT("Cross Auto Line"),

    FCC("Force Cross Country");

    private final String name;

    AutonStrategy(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
