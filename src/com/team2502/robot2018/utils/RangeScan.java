package com.team2502.robot2018.utils;

public class RangeScan
{
    public static double minima = 1000 * 1000 * 1000;
    public static int numRanges;

    /**
     * Process the range sample
     *
     * @param range The received range measurement
     *              <p>
     *              This finds the ranges to 3 closest points.
     *              A point is a minima in the signal stream;
     *              To prevent multiple ranges for the same point we reset scanning for a minima after the distance goes
     *              back out by 10%
     */
    public static void rangeScan_add(double range)
    {
        // Has it gone back 10%?
        if(range >= minima * 1.1)
        {
            // Emit the range
            rangeScan_emit();

            // Start scanning for the next minima
            minima = 1000 * 1000 * 1000;
            return;
        }
        if(range < minima && range > 0.1)
        {
            // This range is close, so track its value
            minima = range;
        }
    }

    /**
     * Place the minima into the list fo three closest range points
     */
    static void rangeScan_emit()
    {
        // Do we have three points?
        if(numRanges < 3) { Trilateration.ranges[numRanges++] = minima; }
        else
        {
            // See if the range should be kept or not
            if(minima < Trilateration.ranges[0]) { Trilateration.ranges[0] = minima; }
            else if(minima < Trilateration.ranges[1]) { Trilateration.ranges[1] = minima; }
            else if(minima < Trilateration.ranges[2]) { Trilateration.ranges[2] = minima; }
        }
    }

    /**
     * This is called when the end of the range streaem has been reached.
     */
    public static void rangeScan_end()
    {
        rangeScan_emit();
    }
}
