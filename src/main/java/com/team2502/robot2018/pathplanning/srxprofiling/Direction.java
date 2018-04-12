package com.team2502.robot2018.pathplanning.srxprofiling;

public enum Direction
{
    FORWARD(1),
    BACKWARD(-1);

    private double value;

    Direction(double direction)
    {
        this.value = direction;
    }

    public static Direction opposite(Direction dir)
    {
        switch(dir)
        {
            case BACKWARD:
                return FORWARD;
            case FORWARD:
            default:
                return BACKWARD;
        }
    }

    public double getValue()
    {
        return value;
    }
}
