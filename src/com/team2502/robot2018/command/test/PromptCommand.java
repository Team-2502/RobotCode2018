package com.team2502.robot2018.command.test;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class PromptCommand extends Command
{

    public PromptCommand()
    {
        SmartDashboard.putBoolean("prompt_ok",false);
    }

    @Override
    protected boolean isFinished()
    {
        return SmartDashboard.getBoolean("prompt_ok",false);
    }
}
