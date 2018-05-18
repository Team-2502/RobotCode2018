import com.team2502.robot2018.pathplanning.purepursuit.*;
import org.joml.ImmutableVector2f;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PathTest
{
    private Path basePath = Path.fromPoints(
            new Point(0, 0),
            new Point(2, 2),
            new Point(4, 2)
                                                     );

    @Test
    public void testClosestPoint()
    {

        Path path = clonePath();
        assertTrue(path.exists());

        ImmutableVector2f closestPoint = path.getClosestPoint(new ImmutableVector2f(1, 1));
        assertEquals(closestPoint.x, 1, 1E-6);
        assertEquals(closestPoint.y, 1, 1E-6);

        closestPoint = path.getClosestPoint(new ImmutableVector2f(0, 1));
        assertEquals(closestPoint.x, 0.5F, 1E-6);
        assertEquals(closestPoint.y, 0.5F, 1E-6);
    }

    @Test
    public void testClosestBounds()
    {
        Path path = clonePath();

        Point start = path.getStart();
        assertEquals(start.getLocation().x, 0F, 1E-6);
        assertEquals(start.getLocation().y, 0F, 1E-6);

        Point end = path.getEnd();
        assertEquals(4F, end.getLocation().x, 1E-6);
        assertEquals(2F, end.getLocation().y, 1E-6);
    }

    @Test
    public void testClosestPointDistance()
    {
        float closestPointPathDistance = clonePath().getAbsDistanceOfClosestPoint(new ImmutableVector2f(0.5F, 0.5F));
        assertEquals(Math.hypot(0.5, 0.5), closestPointPathDistance, 1E-6);
    }

    @Test
    public void testPathIteration()
    {
        Path toIterate = clonePath();

        toIterate.moveNextSegment();
        assertEquals(new Point(2, 2), toIterate.getCurrent().getFirst());
        assertFalse(toIterate.moveNextSegment());
    }

    @Test
    public void testLookaheadGoalPoint()
    {
        Path path = clonePath();

        assertEquals(path.getGoalPoint(0.1F, 1), new ImmutableVector2f(2.9F, 2F));
        assertEquals(path.getGoalPoint(0F, 0), new ImmutableVector2f(2F, 2F));
        assertEquals(path.getGoalPoint(0F, 1), new ImmutableVector2f(3F, 2F));
        assertEquals(path.getGoalPoint(-.1F, 1), new ImmutableVector2f(3.1F, 2F));

        assertEquals(path.getGoalPoint(-.1F, 1), new ImmutableVector2f(3.1F, 2F));

        // outside bounds
        assertEquals(path.getGoalPoint(.1F, 4), new ImmutableVector2f(5.9F, 2F));

        path.moveNextSegment();

        assertEquals(path.getGoalPoint(0F, 0), new ImmutableVector2f(4F, 2F));
    }

    @Test
    public void testNextSegmentsInclusive()
    {
        Path path = Path.fromPoints(new Point(0, 0), new Point(3, 3), new Point(5, 5), new Point(6, 0));
        List<PathSegment> pathSegments = path.nextSegmentsInclusive(1);

        assertEquals(2, pathSegments.size());
        assertEquals(new Point(0, 0), pathSegments.get(0).getFirst());
        assertEquals(new Point(3, 3), pathSegments.get(1).getFirst());

        path.moveNextSegment();
        pathSegments = path.nextSegmentsInclusive(3);
        assertEquals(2, pathSegments.size());
        assertEquals(new Point(3, 3), pathSegments.get(0).getFirst());
        assertEquals(new Point(5, 5), pathSegments.get(1).getFirst());

        path = Path.fromPoints(new Point(0, 0), new Point(3, 3), new Point(5, 5), new Point(6, 0));
        pathSegments = path.nextSegmentsInclusive(15);
        assertEquals(3, pathSegments.size());
        assertEquals(new Point(0, 0), pathSegments.get(0).getFirst());
        assertEquals(new Point(3, 3), pathSegments.get(1).getFirst());
        assertEquals(new Point(5, 5), pathSegments.get(2).getFirst());
    }

    @Test
    public void testPathProgression()
    {
        // TODO
    }

    @Test
    public void testSplineSegment()
    {
        SplinePathSegment test = new SplinePathSegment(
                new Point(0, 0),
                new Point(2, 2),
                new Point(30, 0),
                new Point(30, 0),
                false,
                false,
                0,
                0,
                0
        );

        System.out.println("x, y");
        for(double t = 0; t <= 1; t += 1E-2)
        {
            System.out.println(test.getX(t) + ", " + test.getY(t));
        }


    }

    private Path clonePath()
    {
        return basePath.clone();
    }
}
