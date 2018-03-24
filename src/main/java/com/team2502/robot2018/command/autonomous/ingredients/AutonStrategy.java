package com.team2502.robot2018.command.autonomous.ingredients;

/**
 * Let drive team choose what they want to achieve in auto
 * <p>
 * TODO: Implement multi-cube autos
 */
public enum AutonStrategy
{
    SWITCH("Switch"),
    SCALE("Scale"),
    SCALE_SWITCH("Scale then switch"),
    STRAIGHT("Straight");

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
