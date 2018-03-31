package com.team2502.robot2018.trajectory.record;

import java.io.File;

public class connectDB
{
    /**
     * Enabled on the Dashboard.
     */
    static boolean enabled;

    /**
     * Path to storage medium.
     */
    static File file = new File("/media/sda1");

    /**
     * Presence of a valid storage medium.
     */

    String url = "jdbc:sqlite:";
    String DBPATH = "/media/sda1/pure-pursuit";
    String DB ="/pp-record.sqlite";


    public static boolean isStorage()
    {
        return file.exists();
    }

    public static void setEnabled(boolean setEnabled)
    {
        enabled = setEnabled;
    }


    private connectDB(){
        if(enabled && isStorage()){
            File directory = new File(DBPATH);
            if (! directory.exists()){
                directory.mkdir();
            }
        }
    }

}
