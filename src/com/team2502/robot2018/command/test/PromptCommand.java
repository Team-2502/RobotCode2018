package com.team2502.robot2018.command.test;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Map;

public class PromptCommand extends Command
{

    private final Map<String, Boolean> statuses;
    private final String name;
    boolean success = false;

    public PromptCommand(String name, Map<String,Boolean> statuses)
    {
        reset();
        this.statuses = statuses;
        this.name = name;
    }

    @Override
    protected boolean isFinished()
    {
        boolean getSuccess = SmartDashboard.getBoolean("prompt_success", false);
        boolean getFailure = SmartDashboard.getBoolean("prompt_failure", false);
        if(getSuccess)
        {
            reset();
            success = true;
            return true;
        }
        if(getFailure)
        {
            reset();
            return true;
        }
        return false;
    }

    @Override
    protected void end()
    {
        statuses.put(name,success);
    }

    public boolean isSuccess()
    {
        return success;
    }

    private void reset()
    {
        SmartDashboard.putBoolean("prompt_success",false);
        SmartDashboard.putBoolean("prompt_failure",false);
    }


}
