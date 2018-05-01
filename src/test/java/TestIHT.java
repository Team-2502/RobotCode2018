import com.team2502.robot2018.utils.InterpolationMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the InterpolationMap
 * <p>
 * Not meant to be used in robot code, just on our computrons.
 * <p>
 * Run with the `-ea` option in the JVM
 *
 * @see InterpolationMap
 */
public class TestIHT
{

    @Test
    public void testLineIntegration()
    {
//        ImmutableVector2d a = new ImmutableVector2d(3, 4);
//        ImmutableVector2d b = new ImmutableVector2d(-2, -1);
//
//        MathUtils.Geometry.Line some_line = new MathUtils.Geometry.Line(a, b);
//
//        assert some_line.integrate(0, 1) == 1.5 : "Expected 1.5, got " + some_line.integrate(0, 1);
//
//        assert some_line.integrate(0, 5) == 17.5 : "Expected 17.5, got " + some_line.integrate(0, 5);
//        assert some_line.integrate(-10, 0) == -40 : "Expected -40, got " + some_line.integrate(-10, 0);
//
//        System.out.println("Line tests were fine");
    }

    @Test
    public void interpolatingIntegrationTest()
    {
        InterpolationMap map = new InterpolationMap(0D, 0D);
        map.put(1D, 1D);
        map.put(2D, 1D);
        map.put(3D, 0D);

        assertEquals(0.5, map.integrate(0, 1), 1E-6);
        assertEquals(1.5F, map.integrate(0, 2), 1E-6);
        assertEquals(2F, map.integrate(0, 3), 1E-6);
        assertEquals(2F, map.integrate(0, 10), 1E-6);
        assertEquals(0F, map.integrate(-10, 0), 1E-6);
    }
}
