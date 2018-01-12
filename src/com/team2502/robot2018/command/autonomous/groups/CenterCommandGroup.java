package com.team2502.robot2018.command.autonomous.groups;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class CenterCommandGroup extends CommandGroup
{
    public CenterCommandGroup()
    {
        String AUTO_GAME_DATA = Robot.GAME_DATA.substring(0, 2);

        switch(AUTO_GAME_DATA)
        {
            case "LL":
                break;

            case "LR":
                break;

            case "RL":
                break;

            case "RR":
                break;

            default:
                break;
        }
    }

}
