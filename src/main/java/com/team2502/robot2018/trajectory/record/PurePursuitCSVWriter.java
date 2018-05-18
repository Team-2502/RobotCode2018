package com.team2502.robot2018.trajectory.record;

import com.team2502.robot2018.AutoStartLocationSwitcher;
import com.team2502.robot2018.Robot;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import com.team2502.robot2018.trajectory.record.PurePursuitFrame;
import edu.wpi.first.wpilibj.DriverStation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class PurePursuitCSVWriter
{
    private final String filePath;
    private final File file;
    private FileWriter fileWriter;

    public PurePursuitCSVWriter()
    {
        this("/home/lvuser/" + AutoStartLocationSwitcher.getSelectedPosition().name + "_" + Robot.autonStrategySelector.getSelected().getName() + "_" + Date.from(Instant.now()).toString().replace(' ', '-') + ".csv");
    }

    public PurePursuitCSVWriter(String filePath)
    {
        this.filePath = filePath;
        file = new File(filePath);
        try
        {
            file.createNewFile();
            fileWriter = new FileWriter(file);
        }
        catch(IOException e)
        {
            DriverStation.reportError("Could not create PurePursuitCSVWriter!", e.getStackTrace());
        }
    }

    /**
     * This exists for the unit test. This way, we can deal with the IOException in a special way,
     * since DS.reportError doesn't work on normal computers
     *
     * @param filePath Path to a file
     * @param blank Unused; mere existence indicates that we will throw an IOException if necessary
     * @throws IOException If we had issue creating the file
     */
    public PurePursuitCSVWriter(String filePath, boolean blank) throws IOException
    {
        this.filePath = filePath;
        file = new File(filePath);
        file.createNewFile();
        fileWriter = new FileWriter(file);
    }

    /**
     * Add the waypoints to the CSV and <b>create heading row for the PP frames</b>
     *
     * @param waypointList A list of waypoints (i.e your path)
     * @throws IOException If file writer is unhappy
     */
    public void addWaypoints(List<Waypoint> waypointList)
    {
        try
        {
            // How many waypoints are there?
            fileWriter.append(waypointList.size() + "\n");
            fileWriter.append("x, y\n");

            for(Waypoint waypoint : waypointList)
            {
                // Strip commands from waypoint because involve wpilib
                double x = waypoint.getLocation().x;
                double y = waypoint.getLocation().y;
                fileWriter.append(x + ", " + y + "\n");
            }

            fileWriter.append("time, x, y, lookahead, heading, goal_point_x, goal_point_y, path_circle_radius, path_circle_x, path_circle_y, path_segment_num, closestPoint_x, closestPoint_y, dCP\n");
        }
        catch(IOException e)
        {
            DriverStation.reportError("Could not add waypoints", e.getStackTrace());
        }
    }


    public void addFrame(PurePursuitFrame frame)
    {
        try
        {
            fileWriter.append(frame.toCSV().trim() + "\n");
        } catch(IOException e)
        {
            DriverStation.reportError("Could not add PP Frame", e.getStackTrace());
        }
    }

    public void flush()
    {
        try
        {
            fileWriter.flush();
        }
        catch(IOException e)
        {
            DriverStation.reportError("Could not flush file contents", e.getStackTrace());
        }
    }

    public void close()
    {
        try
        {
            fileWriter.close();
        }
        catch(IOException e)
        {
            DriverStation.reportError("Could not close file", e.getStackTrace());
        }
    }
}

