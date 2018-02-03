package com.team2502.robot2018.utils;

import com.team2502.robot2018.RobotMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;


public class Files
{
    private static long Time = 0;

    private static String FileName = "";

    private static File file;

    /**
     * @param fileName add directory (Always a .txt file)
     */
    public static void newFile(String fileName)
    {
        FileName = fileName + RobotMap.Files.FILESMADE + ".txt";

        file = new File(FileName);

        if(file.exists())
            file.delete();
        else
            file = new File(FileName);

        RobotMap.Files.FILESMADE++;
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
        RobotMap.Files.FILEMAP.put(Name, object);
    }

    public static String getFileName() { return FileName; }

    public static void writeToFile()
    {
        long time = System.currentTimeMillis();
        WriteToFile writeToFile = new WriteToFile(getFileName());

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
        for(int i=0; i< RobotMap.Files.NAMES.length; i++)
        {
            try
            {
                writeToFile.writeToFile(newTime + ", " + RobotMap.Files.NAMES[i] + ": " + RobotMap.Files.FILEMAP.get(RobotMap.Files.NAMES[i]) + ", ");
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
    private boolean append_to_file = true;

    public WriteToFile (String file_path)
    {
        path = file_path;
    }

    public WriteToFile(String file_path, boolean append_value)
    {
        path = file_path;
        append_to_file = append_value;
    }

    public void writeToFile(Object text) throws IOException
    {
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        print_line.println(text);

        print_line.close();
    }
}

