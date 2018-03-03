package com.team2502.robot2018.trajectory;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.joml.ImmutableVector2f;

public class Waypoint
{

    private final float maxSpeed;
    private final ImmutableVector2f location;
    private boolean forward = true;
    private Command[] commands;

    public Waypoint(ImmutableVector2f location, float maxSpeed)
    {
        this(location, maxSpeed, true);
    }

    public Waypoint(ImmutableVector2f location, float maxSpeed, boolean forward, Command... commands)
    {
        this.location = location;
        this.maxSpeed = maxSpeed;
        this.forward = forward;
        this.commands = commands;
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
