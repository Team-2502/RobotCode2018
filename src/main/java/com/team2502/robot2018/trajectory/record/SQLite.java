package com.team2502.robot2018.trajectory.record;

import com.team2502.robot2018.Robot;
import com.team2502.robot2018.trajectory.Waypoint;
import org.joml.ImmutableVector2f;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class SQLite
{


    // How tables will look

    // Recording(Recording_ID, DateTime)
    // Recording_Event(Recording_Event_ID, Recording_Event_Time, String event)
    // PurePursuitFrame(PurePursuitFrame_ID, Recording_ID, robotX, robotY, lookahead, curvature, robotDX, robotDY, time)
    // PurePursuitFrame_Waypoint(PurePursuitFrame_Waypoint_ID, PurePursuitFrame_ID, Waypoint_X, Waypoint_Y)

    private File recordDir;
    private File databaseFile;
    private Connection connection;
    private static SQLite instance;

    private int currentRecordingPrimaryKey = -1;

    public static void init() throws FileNotFoundException, SQLException, ClassNotFoundException
    {
        instance = new SQLite();
        instance.createNeededFiles();
        instance.createTables();
    }

    public static SQLite getInstance()
    {
        return instance;
    }

    private SQLite() {}

    private void createNeededFiles() throws FileNotFoundException
    {
        recordDir = new File(Robot.SAVE_DIR, "record");
        if(!recordDir.exists())
        {
            throw new FileNotFoundException("record directory is not found!");
        }
        databaseFile = new File(recordDir, "trajectory_recordings.db");
        try
        {
            databaseFile.createNewFile();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void createTables() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getAbsolutePath());
        connection.setAutoCommit(false);
        System.out.println("Opened database successfully");

        Statement statement = connection.createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS Recording" +
                     "(Recording_ID INT PRIMARY KEY NOT NULL," +
                     " Recording_Time_Start LONG NOT NULL)";

        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS PurePursuitFrame" +
              "(PurePursuitFrame_ID INT PRIMARY KEY NOT NULL," +
              " Recording_ID INT NOT NULL," +
              " Robot_X REAL NOT NULL," +
              " Robot_Y REAL NOT NULL," +
              " Robot_Lookahead REAL NOT NULL," +
              " Robot_Heading REAL NOT NULL," +
              " Robot_Curvature REAL NOT NULL," +
              " Robot_Used_Speed REAL NOT NULL," +
              " Robot_Actual_Speed REAL NOT NULL," +
              " PurePursuitFrame_Time LONG NOT NULL" +
              ")";

        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS PurePursuitFrame_Waypoint" +
              "(PurePursuitFrame_Waypoint_ID INT PRIMARY KEY NOT NULL," +
              " PurePursuitFrame_ID INT NOT NULL," +
              " Waypoint_X REAL NOT NULL," +
              " Waypoint_Y REAL NOT NULL" +
              ")";

        statement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Recording_Event" +
              "(Recording_Event_ID INT PRIMARY KEY NOT NULL," +
              " Recording_ID INT NOT NULL," +
              " Event_Desc TEXT," +
              "Recording_Event_Time LONG NOT NULL" +
              ")";

        statement.execute(sql);

        statement.close();
    }

    public void startNewRecording()
    {
        String sql = "INSERT INTO Recording (Recording_Time_Start) VALUES (?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, System.currentTimeMillis());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next())
            {
                currentRecordingPrimaryKey = generatedKeys.getInt(1); // should be the primary key
            }
            generatedKeys.close();
            preparedStatement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    // TODO: should probably use Enums
    public void addEventMarker(String description) throws RecordingNotStartedException
    {

        if(currentRecordingPrimaryKey == -1)
        {
            throw new RecordingNotStartedException();
        }

        String sql = "INSERT INTO Recording_Event (Recording_ID,Event_Desc,Recording_Event_Time) VALUES (?,?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentRecordingPrimaryKey);
            preparedStatement.setString(2, description);
            preparedStatement.setLong(3, System.currentTimeMillis());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void addFrame(PurePursuitFrame frame) throws RecordingNotStartedException
    {

        if(currentRecordingPrimaryKey == -1)
        {
            throw new RecordingNotStartedException();
        }

        int currentFrameID = -1;
        String sql = "INSERT INTO PurePursuitFrame (Recording_ID,Robot_X,Robot_Y,Robot_Lookahead, " +
                     "Robot_Curvature, Robot_Used_Speed, Robot_Actual_Speed, PurePursuitFrame_Time, Robot_Heading) VALUES (?,?,?,?,?,?,?,?,?)";
        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentRecordingPrimaryKey);
            preparedStatement.setFloat(2, frame.getRobotX());
            preparedStatement.setFloat(3, frame.getRobotY());
            preparedStatement.setFloat(4, frame.getLookahead());
            preparedStatement.setFloat(5, frame.getCurvature());
            preparedStatement.setFloat(6, frame.getSpeedUsed());
            preparedStatement.setFloat(7, frame.getActualSpeed());
            preparedStatement.setLong(8, frame.getTime());
            preparedStatement.setFloat(9, frame.getRobotHeading());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next())
            {
                currentFrameID = generatedKeys.getInt(1); // should be the primary key
            }
            generatedKeys.close();
            preparedStatement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

        int finalCurrentFrameID = currentFrameID;

        for(Waypoint waypoint : frame.getWaypoints())
        {
            sql = "INSERT INTO PurePursuitFrame_Waypoint(PurePursuitFrame_ID,Waypoint_X,Waypoint_Y) VALUES (?,?,?)";
            try
            {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, finalCurrentFrameID);
                ImmutableVector2f location = waypoint.getLocation();
                preparedStatement.setFloat(2, location.x);
                preparedStatement.setFloat(3, location.y);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }

    }

    public void close()
    {
        try
        {
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public class RecordingNotStartedException extends Exception
    {

    }
}
