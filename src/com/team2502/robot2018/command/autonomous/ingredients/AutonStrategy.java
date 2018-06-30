package com.team2502.robot2018.command.autonomous.ingredients;

public enum AutonStrategy
{
    SWITCH("Switch"),
    SCALE("Scale"),
    SCALE_WAIT("Delayed Scale"),
    SWITCH_SCALE("Switch then Scale"),
    STRAIGHT("Straight"),
    DEEP_SCALE("Far Scale"),
    ONLY_SAME_SIDE("Same side left, drive straight"),
    DANGEROUS("Double switch UNDESTD");

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
