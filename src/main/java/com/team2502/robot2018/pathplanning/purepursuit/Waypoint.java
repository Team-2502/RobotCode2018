package com.team2502.robot2018.pathplanning.purepursuit;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.joml.ImmutableVector2f;

/**
 * A human-entered waypoint that we want the robot to go to. Has parameters to set max speeds, acceleration and deceleration.
 * Also commands can be activated when the Waypoint is reached.
 */
public class Waypoint extends Point
{

    private final float maxSpeed;
    private Command[] commands;
    private float maxAccel;
    private float maxDeccel;

    /**
     * Make a new waypoint
     *
     * @param location  Location of the waypoint
     * @param maxSpeed  Max speed at the waypoint (in ft/s)
     * @param maxAccel  Max accel at the waypoint (in ft/s^2)
     * @param maxDeccel Max decel at the waypoint (in ft/s^2)
     * @param commands  Some commands to execute once we reach this waypoint
     */
    public Waypoint(ImmutableVector2f location, float maxSpeed, float maxAccel, float maxDeccel, Command... commands)
    {
        super(location);
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.commands = commands;
        this.maxDeccel = maxDeccel;
        this.maxDeccel = maxDeccel;
    }

    public Waypoint(float x, float y, float maxSpeed, float maxAccel, float maxDeccel, Command... commands)
    {
        this(new ImmutableVector2f(x, y), maxSpeed, maxAccel, maxDeccel, commands);
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

    public Command[] getCommands()
    {
        return commands;
    }

    /**
     * Execute all the commands we're supposed to execute
     *
     * @param purePursuitMovementStrategy In case one of the commands wants the strategy for something
     */
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
               ", location=" + getLocation().x + "," + getLocation().y +
               '}';
    }

    /**
     * Something commands can extend if they want a copy of the PurePursuitMovementStrategy instance
     */
    interface PurePursuitReciever
    {
        void recieveStrategy(PurePursuitMovementStrategy purePursuitMovementStrategy);
    }
}
