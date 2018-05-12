package com.team2502.ppsimulator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class to ease the handling of files
 */
public class FileHandler
{
    /**
     * The path to the file
     */
    private final String fileName;

    /**
     * The {@link File} object representing the file
     */
    private final File file;

    public FileHandler(String fileName)
    {
        this.fileName = fileName;
        file = new File(this.fileName);
    }

    /**
     * Make new file at certain path
     *
     * @param path path of file
     */
    public void newFile()
    {
        try
        {
            if(file.createNewFile())
            {
                System.out.println("File is created!");
            }
            else
            {
                System.out.println("File already exists.");
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Read the whole file of the file at the specified path
     *
     * @param path path of which file wanted to be read
     * @return The contents of the file (in a very large string)
     */
    private String readWholeFile(String path) throws IOException
    {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder result = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null)
        {
            result.append(line);
            result.append("\n");
        }
        fileReader.close();

        return result.toString();
    }

    /**
     * Reads the whole file
     *
     * @return A string containing the contents of the file
     */
    public String readWholeFile() throws IOException
    {
        return readWholeFile(this.fileName);
    }

    /**
     * Gets a line of file
     *
     * @param line the line number of the line to be returned
     * @return String of the line specified
     */
    public String getLine(int line)
    {
        try
        {
            return Files.readAllLines(Paths.get(fileName)).get(line);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Writes to file
     *
     * @param object object that is been written to the file
     * @param append Whether or not to append to the file
     */
    public void writeToFile(Object object, boolean append) throws IOException
    {
        FileWriter write = new FileWriter(fileName, append);
        write.write(object.toString());
        write.flush();
        write.close();
    }


    /**
     * @return fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @return Number of lines in the file
     */
    public int getNumLines()
    {
        int count = 0;
        if(file.exists())
        {
            try
            {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while(bufferedReader.readLine() != null)
                {
                    count++;
                }

                fileReader.close();
            }
            catch(IOException e) {e.printStackTrace();}
        }

        return count;
    }


}


