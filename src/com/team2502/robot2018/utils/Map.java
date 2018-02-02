package com.team2502.robot2018.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Map
{
    static final int FIRST_WALL = 2;
    // This is is a simple map of local points
    static Trilateration.Point[] map = new Trilateration.Point[] {
            new Trilateration.Point(0.0D, 100.0D),
            new Trilateration.Point(23.0D, 800.0D),
            new Trilateration.Point(0.0D, 0.0D),
            new Trilateration.Point(0.0D, 0.0D),
            new Trilateration.Point(0.0D, 0.0D),
            new Trilateration.Point(0.0D, 0.0D),
            new Trilateration.Point(0.0D, 0.0D),
            new Trilateration.Point(0.0D, 0.0D)
    };

    static int hwall_y[] = { 0, 1000, 300, 700 };
    static int vwall_x[] = { 0, 1000 };

    static double dist2(Trilateration.Point p1)
    {
        return (p1.x - PtPosEstimator.est_position.x) * (p1.x - PtPosEstimator.est_position.x) + (p1.y - PtPosEstimator.est_position.y) * (p1.y - PtPosEstimator.est_position.y);
    }

    public static class PointComparator implements Comparator<Integer>
    {
        @Override
        public int compare(Integer p1, Integer p2)
        {
            double d = dist2(map[p1]) - dist2(map[p2]);
            return d < -0.001 ? -1 : d > 0.001 ? 1 : 0;
        }

        @Override
        public boolean equals(Object obj)
        {
            return false;
        }
    }

    /**
     * This looks up the three nearest points to us.
     * Note that this is for demonstation purposes, and is a bit incomplete.
     * Large objects, like walls,  have their landmark point computed crudely here.  Arbitrary walls and
     * angles would use math just like in the geometry portion of video games.
     */
    public static void lookUpNearbyLandmarks()
    {
        // Make an array of the points
        Integer pointIndex[] = new Integer[8];

        for(int i = 0; i < 8; i++)
        {
            pointIndex[i] = i;
        }

        // Compute synthetic wall points
        map[FIRST_WALL + 0].x = vwall_x[0];
        map[FIRST_WALL + 0].y = PtPosEstimator.est_position.y;
        map[FIRST_WALL + 1].x = vwall_x[1];
        map[FIRST_WALL + 1].y = PtPosEstimator.est_position.y;

        map[FIRST_WALL + 2].x = PtPosEstimator.est_position.x;
        map[FIRST_WALL + 2].y = hwall_y[0];
        map[FIRST_WALL + 3].x = PtPosEstimator.est_position.x;
        map[FIRST_WALL + 3].y = hwall_y[1];
        map[FIRST_WALL + 4].x = PtPosEstimator.est_position.x > 100 && PtPosEstimator.est_position.x < 900 ? PtPosEstimator.est_position.x : -1;
        map[FIRST_WALL + 4].y = hwall_y[2];
        map[FIRST_WALL + 5].x = PtPosEstimator.est_position.x > 100 && PtPosEstimator.est_position.x < 900 ? PtPosEstimator.est_position.x : -1;
        map[FIRST_WALL + 5].y = hwall_y[3];

        // Sort them as distance from our estimated position
        Collections.sort(new ArrayList<>(Arrays.asList(pointIndex)));

        // keep the best three
        Trilateration.a_point = map[pointIndex[0]];
        Trilateration.b_point = map[pointIndex[1]];
        Trilateration.c_point = map[pointIndex[2]];
    }
}
