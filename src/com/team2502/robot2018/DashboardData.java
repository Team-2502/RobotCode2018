package com.team2502.robot2018;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import java.util.ArrayList;
import java.util.List;

public final class DashboardData
{
    /**
     * An interface to allow you to automatically update stuff
     * on the Smart Dashboard.
     */
    public interface DashboardUpdater
    {
        /**
         * Called every tick to update data on the Smart Dashboard.
         */
        void updateDashboard();
    }

    /**
     * ArrayList:
     * 12 + 16 + 4 + 4n
     *   12 bytes for the object header
     *     4 bytes for the class pointer (type)
     *     4 bytes for the flags
     *     4 bytes for the thread locking data
     *   16 bytes for the array header
     *     4 bytes for the class pointer (type)
     *     4 bytes for the flags
     *     4 bytes for the thread locking data
     *     4 bytes for the length of the array
     *   4 bytes for the length of the list
     *   4n bytes where `n` is the size of the array, 4 is the size of a pointer
     *
     * Doubly Linked List:
     * (12 + 4 + 4 + 4)n
     *   12 bytes for the object header
     *     4 bytes for the class pointer (type)
     *     4 bytes for the flags
     *     4 bytes for the thread locking data
     *   4 bytes for the pointer to the previous element
     *   4 bytes for the pointer to the next element
     *   4 bytes for the pointer to the actual data.
     *   n is the number of elements in the list
     *
     * Under 2 elements the LinkedList will be smaller in size.
     * Anything larger the ArrayList will be smaller (to an extent,
     * the ArrayList expands at a rate of (n * 1.5) so the next time
     * allocates it may be larger)
     *
     * We are allocating 4 elements as a starter. I would recommend
     * setting this to `2^(floor(log2(numberOfallocators))`. This will
     * Keep the processor happy with padding, and still allows it to
     * dynamically expand without much delay at launch or high memory usage.
     */
    private static List<DashboardUpdater> updaters = new ArrayList<>(4);

    static void update()
    {
        for(DashboardUpdater subsystem : updaters) { subsystem.updateDashboard(); }
        updateNavX();
    }

    public static void addUpdater(DashboardUpdater subsystem)
    { updaters.add(subsystem); }

    private static void updateNavX()
    {
        SmartDashboard.putNumber("NavX: Yaw", Robot.NAVX.getYaw());
        SmartDashboard.putNumber("NavX: X Displacement", Robot.NAVX.getDisplacementX());
        SmartDashboard.putNumber("NavX: Y Displacement", Robot.NAVX.getDisplacementY());
        SmartDashboard.putNumber("NavX: Z Displacement", Robot.NAVX.getDisplacementZ());
    }

     private DashboardData() { }
}
