package com.team2502.robot2018.command.autonomous.ingredients;

/**
 * Let drive team choose what they want to achieve in auto
 * <p>
 * TODO: Implement multi-cube autos
 */
public enum AutonStrategy
{
    STRAIGHT("Straight"),
    SWITCH("Switch"),
    SCALE("Scale"),
    DEEP_SCALE("Scale via Null Zone"),
    SCALE_WAIT("Delayed Scale"),
    SWITCH_TWICE("Switch x2 (Center)"), // Used for center
    SCALE_TWICE("Scale x2"),
    SCALE_SWITCH("Scale + Switch"),
    FCC("Force Cross Country"),
    SWITCH_SCALE("Switch then Scale"),
    ONLY_SAME_SIDE("Same side scale or drive straight");
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
