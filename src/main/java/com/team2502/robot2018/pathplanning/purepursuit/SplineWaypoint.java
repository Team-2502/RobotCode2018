package com.team2502.robot2018.pathplanning.purepursuit;

import com.team2502.robot2018.utils.MathUtils;
import edu.wpi.first.wpilibj.command.Command;
import org.joml.ImmutableVector2f;

public class SplineWaypoint extends Waypoint
{
    private final Point slopeVec;

    public SplineWaypoint(ImmutableVector2f location, double angle, float maxSpeed, float maxAccel, float maxDeccel, Command... commands)
    {
        super(location, maxSpeed, maxAccel, maxDeccel, commands);
        // TODO: Is this what we want? Scaling the tangent vector based on maxSpeed?
        slopeVec = new Point(MathUtils.Geometry.getVector(maxSpeed, (float) angle));
    }

    public SplineWaypoint(float x, float y, double angle, float maxSpeed, float maxAccel, float maxDeccel, Command... commands)
    {
        this(new ImmutableVector2f(x, y), angle, maxSpeed, maxAccel, maxDeccel, commands);
    }

    public Point getSlopeVec()
    {
        return slopeVec;
    }

}
