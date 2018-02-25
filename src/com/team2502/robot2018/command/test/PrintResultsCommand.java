package com.team2502.robot2018.command.test;

import edu.wpi.first.wpilibj.command.InstantCommand;
import logger.ShuffleboardLog;

import java.util.Map;
import java.util.Set;

public class PrintResultsCommand extends InstantCommand
{
    private final Map<String, Boolean> resultsMap;

    public PrintResultsCommand(Map<String, Boolean> resultsMap)
    {
        this.resultsMap = resultsMap;
    }

    @Override
    protected void execute()
    {
        String results;

        int i = 0;
        Set<Map.Entry<String, Boolean>> entries = resultsMap.entrySet();
        StringBuilder resultsBuilder = new StringBuilder("{");
        for(Map.Entry<String, Boolean> entry : entries)
        {
            i++;
            resultsBuilder.append(entry.getKey()).append(":").append(entry.getValue());
            if(i < entries.size() - 1)
            {
                resultsBuilder.append(", ");
            }
        }
        resultsBuilder.append("}");
        results = resultsBuilder.toString();
        ShuffleboardLog.getInstance().log(results);
    }
}
