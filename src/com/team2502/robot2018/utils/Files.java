package com.team2502.robot2018.utils;

import com.team2502.robot2018.RobotMap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * A class containing methods pertaining to writing a file
 */
public class Files
{
    /**
     * Time since initialized. Used for timestamping entries into the file
     */
    private static long Time = 0;

    /**
     * The path to the file that we are writing to
     */
    private static String FileName = "";

    /**
     * The file that we are writing to
     */
    private static File file;

    /**
     * @param fileName create a new file (Always a .txt file)
     */
    public static void newFile(String fileName)
    {
        FileName = fileName + RobotMap.Files.FILESMADE + ".txt";

        file = new File(FileName);

        if(file.exists())
        { file.delete(); }
        else
        { file = new File(FileName); }

        RobotMap.Files.FILESMADE++;
    }

    /**
     * Set the time (ms)
     * @param time Time in milliseconds to start timestamping from
     */
    public static void setTime(long time)
    {
        Time = time;
    }

    /**
     * Throw something into RobotMap.Files#FILEMAP
     * @param Name The "class" of the thing (in an HTML/CSS sense). See {@link RobotMap.Files#NAMES}
     * @param object the thing
     *
     * @see com.team2502.robot2018.RobotMap.Files#FILEMAP
     */
    public static void setNameAndValue(String Name, Object object)
    {
        RobotMap.Files.FILEMAP.put(Name, object);
    }

    /**
     * @return Path to file that we are editing
     */
    public static String getFileName() { return FileName; }

    /**
     * Change file we are writing to
     * @param fileName New path to file
     */
    public static void setFileName(String fileName)
    {
        FileName = fileName;
    }

    /**
     * Put everything from {@link com.team2502.robot2018.RobotMap.Files#FILEMAP} into a file
     */
    public static void writeToFile()
    {
        long time = System.currentTimeMillis();
        WriteToFile writeToFile = new WriteToFile(getFileName());

        long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
        String newTime;

        if(time < 1000)
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
        for(int i = 0; i < RobotMap.Files.NAMES.length; i++)
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

/**
 * A class containing methods pertaining to writing to files
 */
class WriteToFile
{
    private String path;
    private boolean append_to_file = true;

    /**
     * Append to a file
     * @param file_path the file to append to
     */
    public WriteToFile(String file_path)
    {
        path = file_path;
    }

    /**
     * Write to a file
     * @param file_path Path to file
     * @param append_value Whether or not to append to it
     */
    public WriteToFile(String file_path, boolean append_value)
    {
        this(file_path);
        append_to_file = append_value;
    }

    /**
     * Put things in the file
     * @param text The stuff to put in the file
     * @throws IOException If stuff goes wrong
     */
    public void writeToFile(Object text) throws IOException
    {
        FileWriter write = new FileWriter(path, append_to_file);
        PrintWriter print_line = new PrintWriter(write);

        print_line.println(text);

        print_line.close();
    }
}

