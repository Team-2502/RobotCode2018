package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightCommandGroup extends CommandGroup
{
    public RightCommandGroup()
    {
        if(Robot.GAME_DATA.substring(0, 2).equalsIgnoreCase("LL"))
        {

        }
        else if(Robot.GAME_DATA.substring(0,2).equalsIgnoreCase("LR"))
        {

        }
        else if(Robot.GAME_DATA.substring(0,2).equalsIgnoreCase("RL"))
        {

        }
        else if(Robot.GAME_DATA.substring(0,2).equalsIgnoreCase("RR"))
        {

        }
        else //Drive Straight
        {

        }
    }

}
