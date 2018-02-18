package com.team2502.robot2018.trajectory;

import com.team2502.robot2018.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A conglomeration of desired {@link MotionState1D}s for a given position.
 * Truly, it is composed of {@link MotionSegment}s, which in turn link two {@link MotionState1D}s.
 */
public class MotionProfile
{
    private final List<MotionSegment> segments = new ArrayList<>();

    public MotionSegment firstStateByPosition(float position)
    {
        for(MotionSegment segment : segments)
        {
            if(MathUtils.Algebra.bounded(segment.getStart().getPosition(),position,segment.getEnd().getPosition()))
            {
                return segment;
            }
        }
        return null;
    }

    public float length()
    {
        float length = 0;
        for(MotionSegment segment : segments)
        {
            length+=segment.length();
        }
        return length;
    }

    public MotionState1D endState()
    {
        return segments.get(segments.size()-1).getEnd();
    }

    public MotionState1D startState()
    {
        return segments.get(0).getStart();
    }

    public float endTime()
    {
        return endState().getTime();
    }

    public float startTime()
    {
        return segments.get(0).getStart().getTime();
    }



}
