package com.team2502.robot2018.trajectory;

/**
 * Quite similar to Team 254 Lookahead.
 */
public class Lookahead
{
    private final float minDistance, maxDistance;
    private final float minSpeed, maxSpeed;
    private final float dDistance;
    private final float dSpeed;

    public Lookahead(float minDistance, float maxDistance, float minSpeed, float maxSpeed)
    {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;

        dDistance = maxDistance - minDistance;
        dSpeed = maxSpeed - minSpeed;
    }

    float getLookaheadForSpeed(float speed)
    {
        float lookahead = dDistance * (speed - minSpeed) / dSpeed + minDistance;
        return Double.isNaN(lookahead) ? minDistance : Math.max(minDistance, Math.min(maxDistance, lookahead));
    }
}
