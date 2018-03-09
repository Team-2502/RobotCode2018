package com.team2502.robot2018.trajectory;

/**
 *  A dynamic lookahead based on speed
 */
public class Lookahead
{
    private final float minDistance, maxDistance;
    private final float minSpeed, maxSpeed;
    private final float dDistance;
    private final float dSpeed;

    /**
     *
     * @param minDistance The minimum lookahead distance
     * @param maxDistance The maximum lookahead distance
     * @param minSpeed The speed at which lookahead starts to grow
     * @param maxSpeed The speed at which lookahead stops growing
     */
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
