package com.team2502.robot2018.trajectory;


import com.team2502.robot2018.utils.MathUtils;

/**
 * A linear segment joining two {@link MotionState1D}s.
 */
public class MotionSegment
{
    private final MotionState1D start;
    private final MotionState1D end;

    public MotionSegment(MotionState1D start, MotionState1D end)
    {
        this.start = start;
        this.end = end;
    }

    public float length()
    {
        return getEnd().getPosition() - getStart().getPosition();
    }

    public MotionState1D getStart()
    {
        return start;
    }

    public MotionState1D getEnd()
    {
        return end;
    }

    public boolean valid()
    {
        return MathUtils.epsilonEquals(start.extrapForPositionAtTime(end.getTime()),end.getPosition());
    }

    public boolean containsPosition(float position)
    {
        return MathUtils.Algebra.bounded(start.getPosition(),position,end.getPosition());
    }
}
