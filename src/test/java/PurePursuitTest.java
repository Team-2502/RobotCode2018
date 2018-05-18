import com.team2502.robot2018.pathplanning.purepursuit.LookaheadBounds;
import com.team2502.robot2018.pathplanning.purepursuit.Path;
import com.team2502.robot2018.pathplanning.purepursuit.Waypoint;
import org.joml.ImmutableVector2f;
import org.junit.Test;

import static com.team2502.robot2018.utils.PurePursuitUtils.*;
import static org.junit.Assert.assertEquals;

public class PurePursuitTest
{

    private static final float BIG_NUMBER = 10000;
    private static final float ANY_F = -1;

    @Test
    public void testCalcWheelVelocities()
    {
        // Going in a straight line
        ImmutableVector2f wheelVelocities = calculateWheelVelocities(0, 1, 2);
        assertEquals(vec(2, 2), wheelVelocities);

        // left wheel velocity should be 0
        wheelVelocities = calculateWheelVelocities(radius(0.5F), 1, 20);
        assertEquals(vec(0, 20), wheelVelocities);

        // right wheel velocity should be 0
        wheelVelocities = calculateWheelVelocities(-radius(0.5F), 1, 20);
        assertEquals(vec(20, 0), wheelVelocities);

        // wheel speed should be approx the same
        wheelVelocities = calculateWheelVelocities(BIG_NUMBER, 1, 20);
        assertEquals(-20, wheelVelocities.x, 0.1F);
        assertEquals(20, wheelVelocities.y, 0.1F);
    }

    @Test
    public void testSpeedUsedAccel()
    {
        Path path = Path.fromPoints(
                new Waypoint(0, 0, 0, 0.5F, -0.5F),
                new Waypoint(0, 10, 1, 0.5F, -0.5F)
                                             );

        // acceleration from 0
        assertEquals(0.5F, generateSpeedUsed(0, 0, 1, path.clone()), 1E-6);
        assertEquals(1F, generateSpeedUsed(0, 0, 2, path.clone()), 1E-6);

        // acceleration from 0.5
        assertEquals(1F, generateSpeedUsed(0, 0.5F, 1, path.clone()), 1E-6);

        // acceleration from 1 (same as destination speed)
        assertEquals(1F, generateSpeedUsed(0, 1F, 1, path.clone()), 1E-6);
    }

    @Test
    public void testSpeedUsedDeccel()
    {

        Path path = Path.fromPoints(
                new Waypoint(0, 0, 1, 0.5F, -0.5F),
                new Waypoint(0, 10, 0, 0.5F, -0.5F)
                                             );

        // deccelerate to 0
        assertEquals(2F, generateSpeedUsed(0, 2, ANY_F, path.clone()), 1E-6);
        assertEquals(0F, generateSpeedUsed(10, 2, ANY_F, path.clone()), 1E-6);

        // vf^2 = vi^2 +2ax
        // 0 = .7071067690849304^2-2*0.5*.5
        assertEquals(.7071067690849304F, generateSpeedUsed(9.5F, .7071067690849304F, ANY_F, path.clone()), 1E-6);

        path = Path.fromPoints(
                new Waypoint(0, 0, 1, 0.5F, -0.5F),
                new Waypoint(0, 9.9999F, 1, 0.5F, -0.5F),
                new Waypoint(0, 10, 0, 0.5F, -0.5F)
                              );

        // deccelerate to 0
        assertEquals(2F, generateSpeedUsed(0, 2, ANY_F, path.clone()), 1E-6);
        assertEquals(0F, generateSpeedUsed(9.999F, 2, ANY_F, path.clone()), 0.1F);
        path.moveNextSegment();
        assertEquals(0F, generateSpeedUsed(9.999999F, 2, ANY_F, path.clone()), 0.1F);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpeedUsedOutOfBounds()
    {
        Path path = Path.fromPoints(
                new Waypoint(0, 0, 1, 0.5F, -0.5F),
                new Waypoint(0, 10, 0, 0.5F, -0.5F)
                                             );
        assertEquals(0F, generateSpeedUsed(14, 2, ANY_F, path.clone()), 1E-6);
    }

    @Test
    public void testLookahead()
    {
        LookaheadBounds lookahead = new LookaheadBounds(2, 4, 2, 5);
        assertEquals(2, generateLookahead(lookahead, 0, 0), 1E-6);
        assertEquals(2, generateLookahead(lookahead, 2, 0), 1E-6);
        assertEquals(3, generateLookahead(lookahead, (2 + 5) / 2F, 0), 1E-6);
        assertEquals(4, generateLookahead(lookahead, 5F, 0), 1E-6);

        // lookahead increases with closestPointDistance
        assertEquals(5, generateLookahead(lookahead, 5F, 1), 1E-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLookaheadIllegalArgNegCP()
    {
        LookaheadBounds lookahead = new LookaheadBounds(2, 4, 2, 5);
        assertEquals(5, generateLookahead(lookahead, 5F, -1), 1E-6);
    }


    private ImmutableVector2f vec(float left, float right)
    {
        return new ImmutableVector2f(left, right);
    }

    /**
     * @param r
     * @return radius to curvature
     */
    private float radius(float r)
    {
        return 1 / r;
    }
}
