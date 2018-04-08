package com.team2502.robot2018.trajectory.record;

import com.team2502.robot2018.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL
{

    // How tables will look

    // Recording(Recording_ID, DateTime)
    // PurePursuitFrame(PurePursuitFrame_ID, Recording_ID, robotX, robotY, lookahead, curvature, robotDX, robotDY, time)
    // PurePursuitFrame_Waypoint(PurePursuitFrame_Waypoint_ID, PurePursuitFrame_ID, Waypoint_X, Waypoint_Y)

    private File recordDir;
    private File databaseFile;
    private Connection connection;

    public MySQL() throws FileNotFoundException, SQLException, ClassNotFoundException
    {
        createNeededFiles();
        createDatabase();
    }

    private void createNeededFiles() throws FileNotFoundException
    {
        recordDir = new File(Robot.SAVE_DIR, "record");
        if( !recordDir.exists())
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

    private void createDatabase() throws ClassNotFoundException, SQLException
    {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:"+databaseFile.getAbsolutePath());
        connection.setAutoCommit(false);
        System.out.println("Opened database successfully");

        Statement statement  = connection.createStatement();

        String sql = "CREATE TABLE Recording" +
                     "(Recording_ID INT PRIMARY KEY NOT NULL," +
                     " Recording_Time_Start LONG NOT NULL)";

        statement.execute(sql);

        sql = "CREATE TABLE PurePursuitFrame" +
              "(PurePursuitFrame_ID INT PRIMARY KEY NOT NULL," +
              " Recording_ID INT NOT NULL," +
              " Robot_X REAL NOT NULL," +
              " Robot_Y REAL NOT NULL," +
              " Robot_Lookahead REAL NOT NULL," +
              " Robot_HEADING REAL NOT NULL," +
              " Robot_Curvature REAL NOT NULL," +
              " Robot_DX REAL NOT NULL," +
              " Robot_DY REAL NOT NULL," +
              " PurePursuitFrame_Time LONG NOT NULL" +
              ")";

        statement.execute(sql);
        
        sql = "CREATE TABLE PurePursuitFrame_Waypoint" +
              "(PurePursuitFrame_Waypoint_ID INT PRIMARY KEY NOT NULL," +
              " PurePursuitFrame_ID INT NOT NULL," +
              " Waypoint_X INT NOT NULL," +
              " Waypoint_Y INT NOT NULL" +
              ")";

        statement.execute(sql);
    }

}
