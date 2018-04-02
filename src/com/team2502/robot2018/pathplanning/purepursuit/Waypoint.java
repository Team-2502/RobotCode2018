package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.joml.ImmutableVector2f;

/**
 * A human-entered waypoint that we want the robot to go to. Has parameters to set max speeds, acceleration and deceleration.
 * Also commands can be activated when the Waypoint is reached.
 */
public class Waypoint
{

    private final float maxSpeed;
    private final ImmutableVector2f location;
    private boolean forward = true;
    private Command[] commands;
    private float maxAccel;
    private float maxDeccel;

    public Waypoint(ImmutableVector2f location, float maxSpeed, float maxAccel, float maxDeccel)
    {
        this(location, maxSpeed, maxAccel, maxDeccel, true);
    }

    public Waypoint(ImmutableVector2f location, float maxSpeed, float maxAccel, float maxDeccel, boolean forward, Command... commands)
    {
        this.location = location;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.forward = forward;
        this.commands = commands;
        this.maxDeccel = maxDeccel;
        this.maxDeccel = maxDeccel;
    }

    public float getMaxDeccel()
    {
        return maxDeccel;
    }

    public float getMaxAccel()
    {
        return maxAccel;
    }

    public float getMaxSpeed()
    {
        return maxSpeed;
    }

    public ImmutableVector2f getLocation()
    {
        return location;
    }

    public boolean isForward()
    {
        return forward;
    }

    public Command[] getCommands()
    {
        return commands;
    }

    public void executeCommands(PurePursuitMovementStrategy purePursuitMovementStrategy)
    {
        for(Command command : commands)
        {
            Robot.writeLog("scheduling command", 10);
            Scheduler.getInstance().add(command);
            if(command instanceof PurePursuitReciever)
            {
                PurePursuitReciever purePursuitReciever = (PurePursuitReciever) command;
                purePursuitReciever.recieveStrategy(purePursuitMovementStrategy);
            }
        }
    }


    @Override
    public String toString()
    {
        return "Waypoint{" +
               "maxSpeed=" + maxSpeed +
               ", location=" + location +
               ", forward=" + forward +
               '}';
    }

    interface PurePursuitReciever
    {
        void recieveStrategy(PurePursuitMovementStrategy purePursuitMovementStrategy);
    }
}
