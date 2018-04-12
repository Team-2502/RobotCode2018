package com.team2502.robot2018.pathplanning.srxprofiling;

import com.ctre.phoenix.motion.TrajectoryPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MotionProfile implements StreamableProfile
{
    private static final List<MotionProfile> profiles = new ArrayList<>();
    private final List<MotionProfileConstituent> constituents;

    private TrajectoryPoint[] leftPoints;
    private TrajectoryPoint[] rightPoints;

    /**
     * Given some constituent pieces, make a motion profile
     *
     * @param constituents The {@link MotionProfileConstituent}s that make up this motion profile
     */
    public MotionProfile(MotionProfileConstituent... constituents)
    {
        this.constituents = Arrays.asList(constituents);
        profiles.add(this);
    }

    /**
     * Initialize all motion profiles that were created
     *
     * @see MotionProfile#init()
     */
    public static void initialize()
    {
        for(MotionProfile profile : profiles)
        {
            profile.init();
        }
    }

    public List<MotionProfileConstituent> getConstituents()
    {
        return constituents;
    }

    /**
     * Initialize the motion profile by putting all the left and right {@link TrajectoryPoint}s in their arrays.
     */
    private void init()
    {
        ArrayList<TrajectoryPoint> leftPointList = new ArrayList<>();
        ArrayList<TrajectoryPoint> rightPointList = new ArrayList<>();
        for(MotionProfileConstituent constituent : constituents)
        {
            if(!constituent.isInitialized())
            {
                constituent.init();
            }
            leftPointList.addAll(Arrays.asList(constituent.getLeftPoints()));
            rightPointList.addAll(Arrays.asList(constituent.getRightPoints()));
        }
        leftPointList.toArray(leftPoints);
        rightPointList.toArray(rightPoints);
    }

    public TrajectoryPoint[] getLeftPoints()
    {
        return leftPoints;
    }

    public TrajectoryPoint[] getRightPoints()
    {
        return rightPoints;
    }
}
