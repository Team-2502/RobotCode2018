package com.team2502.robot2018.utils;

import com.team2502.robot2018.RobotMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;



public class Files
{
    static File file;
    static long Time = 0;
    static Object Object;
    static Map<String, Object> fileMap = new HashMap<String, Object>();

    public static String FileName = "";

    static String[] Names = {"Loop Error"};

    /**
     * @param fileName add directory (Always a .txt file)
     */
    public static void newFile(String fileName)
    {
        FileName = fileName + RobotMap.Files.FilesMade + ".csv";
        file = new File(FileName);
        RobotMap.Files.FilesMade++;
    }

    public static void setTime(long time)
    {
        Time = time;
    }
    public static void setFileName(String fileName)
    {
        FileName = fileName;
    }

    public static void setNameAndValue(String Name, Object object)
    {
        fileMap.put(Name, object);
    }

    public static void writeToFile()
    {
        long time = Time;
        WriteToFile data = new WriteToFile(FileName, true);

        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        String newTime;
        if (time < 1000)
        {
            newTime = String.valueOf(time);
        }
        else if(seconds < 60)
        {
            newTime = String.valueOf(seconds) + ": " + String.valueOf(time % 1000);
        }
        else
        {
            newTime = String.valueOf(minutes) + ": " + String.valueOf(seconds) + ": " + String.valueOf(time % 1000);
        }
        for(int i=0; i< Names.length-1; i++)
        {
            try
            {
                data.writeToFile(newTime + ", " + Names[i] + ": " + fileMap.get(Names[i]) + ", ");
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

class WriteToFile
{
    private String path;
    private boolean append_to_file = false;

    public WriteToFile (String file_path)
    {
        path = file_path;
    }
    public WriteToFile(String file_path, boolean append_value)
    {
        path = file_path;
        append_to_file = append_value;
    }

    public void writeToFile(String text) throws IOException
    {
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        print_line.printf("%s" + "%n", text);

        print_line.close();
    }
}
