package com.team2502.robot2018.command.autonomous.ingredients;

public enum AutonStrategy
{
    SWITCH("Switch"),
    SCALE("Scale"),
    SWITCH_SCALE("Switch then Scale");

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
