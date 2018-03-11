package com.team2502.robot2018.command.autonomous.ingredients;

public enum AutonStrategy
{
    SWITCH("Switch"),
    SCALE("Scale"),
    SWITCH_SCALE("Switch then Scale"),
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
