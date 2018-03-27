package com.team2502.robot2018.command.autonomous.ingredients;

/**
 * Let drive team choose what they want to achieve in auto
 * <p>
 * TODO: Implement multi-cube autos
 */
public enum AutonStrategy
{
    SWITCH("Switch"),
    SWITCH_TWICE("Switch and then Switch again"), // Used for center
    SCALE("Scale"),
    SCALE_TWICE("Scale and then Scale again"),
    SCALE_SWITCH("Scale and then Switch"),
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
