package com.team2502.robot2018.command.teleop;


import com.team2502.ctannotationprocessor.Undefined;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This class has been deprecated in order to discourage use of it.
 * <br>
 * It is rather unreadable
 * <p>
 * Only use this class when doing quick fixes under time constraints
 */
@Deprecated
public class QuickCommand extends InstantCommand
{
    final Runnable action;

    public QuickCommand(Runnable action)
    {
        this.action = action;
    }

    public QuickCommand(Subsystem subsystem, Runnable action)
    {
        this(action);
        requires(subsystem);
    }


    protected void execute()
    {
        action.run();
    }

}
