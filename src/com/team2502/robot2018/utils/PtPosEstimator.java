package com.team2502.robot2018.utils;

public class PtPosEstimator
{
    public static int errCnt = 0;

    /// The estimated position of the observer
    public static Trilateration.Point est_position;
    /// The maximum deviation of the trilaterated point from the estimated position before it is rejected.
    public static double maxTrilaterateError = 144;

    /**
     * Sort the 3 best nearest measurements.
     * This makes it easy to match them up to the 3 landmarkers.
     */
    public static void sort_distances()
    {
        if(Trilateration.ranges[2] < Trilateration.ranges[0])
        {
            double tmp = Trilateration.ranges[0];
            Trilateration.ranges[0] = Trilateration.ranges[2];
            Trilateration.ranges[2] = tmp;
        }
        if(Trilateration.ranges[1] < Trilateration.ranges[0])
        {
            double tmp = Trilateration.ranges[0];
            Trilateration.ranges[0] = Trilateration.ranges[1];
            Trilateration.ranges[1] = tmp;
        }
        if(Trilateration.ranges[2] < Trilateration.ranges[1])
        {
            double tmp = Trilateration.ranges[2];
            Trilateration.ranges[2] = Trilateration.ranges[1];
            Trilateration.ranges[1] = tmp;
        }
    }

    /**
     * A helper to compute the distance (squared) between the observer and landmark point
     *
     * @param p2 Landmark point
     * @returns the distance, squared.
     * <p>
     * This is used to sort the points.
     */
    public static double dist2(Trilateration.Point p2)
    {
        return (est_position.x - p2.x) * (est_position.x - p2.x) + (est_position.y - p2.y) * (est_position.y - p2.y);
    }


    /**
     * Sort the 3 landmarks, nearest to farthest way.
     * This makes it easy to match them up to the 3 distances.  The 3 landmarks were selected in an earlier
     * state.
     */
    public static void sort_points()
    {
        if(dist2(Trilateration.c_point) < dist2(Trilateration.a_point))
        {
            Trilateration.Point tmp = Trilateration.a_point;
            Trilateration.a_point = Trilateration.c_point;
            Trilateration.c_point = tmp;
        }
        if(dist2(Trilateration.b_point) < dist2(Trilateration.a_point))
        {
            Trilateration.Point tmp = Trilateration.a_point;
            Trilateration.a_point = Trilateration.b_point;
            Trilateration.b_point = tmp;
        }
        if(dist2(Trilateration.c_point) < dist2(Trilateration.b_point))
        {
            Trilateration.Point tmp = Trilateration.c_point;
            Trilateration.c_point = Trilateration.b_point;
            Trilateration.b_point = tmp;
        }
    }


    /**
     * Estimates a position from landmarks and measured distances
     *
     * @returns estimated position
     * <p>
     * Note: there are times that other approached can produce better results in various special cases.
     * For instance, if we know that a wall is on an x axis, (or y-axis) we can improve our
     * x position just from that.  That distance measure is not good for trilateration.
     */
    public static Trilateration.Point estimatePositionFromLandmarks()
    {
        // 1) match up the measured distances to the points
        //   a) sort the measured distances
        sort_distances();

        //   b) find three points closest to current estimated position
        Map.lookUpNearbyLandmarks();

        //   c) sort the points
        // Note: this is not done here as the points are sorted for us by lookUpNearbyLandmarks()
        //sort_points();

        // The points may be in the wrong order at this moment -- that is, they might be wound
        // counter-clockwise rather than clockwise.  We could look for winding and reorder, but
        // it is easier to call trilaterate() twice.

        // compute a position estimate using trilateration
        Trilateration.Point est_trilaterate = Trilateration.trilaterate();

        // check result - is the trilateral estimate valid?  If not, try swaping the values around
        // to get the right winding, and recalculate
        if(est_trilaterate.x < 0 || est_trilaterate.y < 0)
        {
            // Swap the points
            double tmpd = Trilateration.ranges[1];
            Trilateration.ranges[1] = Trilateration.ranges[2];
            Trilateration.ranges[2] = tmpd;
            Trilateration.Point tmpp = Trilateration.b_point;
            Trilateration.b_point = Trilateration.c_point;
            Trilateration.c_point = tmpp;

            // Recalculate the estimate
            est_trilaterate = Trilateration.trilaterate();
        }

        // check result - is the trilateral estimate valid and close-enough to our estimated position?
        // no: throw it out, and set it to our estimated position
        if(est_trilaterate.x < 0 || est_trilaterate.y < 0 || dist2(est_trilaterate) > maxTrilaterateError)
        {
            errCnt++;
            return est_position;
        }

        // the estimated position looks good, return it
        return est_trilaterate;
    }
}
