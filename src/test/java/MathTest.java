import com.team2502.robot2018.utils.MathUtils;
import org.joml.ImmutableVector2f;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MathTest
{
    private static final double DELTA = 1E-5;
    private ImmutableVector2f e1 = new ImmutableVector2f(1, 0);

    @Test
    public void testRotation90()
    {
        ImmutableVector2f rotated90 = MathUtils.LinearAlgebra.rotate2D(e1, MathUtils.PI_F / 2);

        assertEquals(0, rotated90.x, 0.001);
        assertEquals(1, rotated90.y, 0.001);
    }

    @Test
    public void testRotation720()
    {
        ImmutableVector2f rotated720 = MathUtils.LinearAlgebra.rotate2D(e1, 0F);

        assertEquals(1, rotated720.x, 0.001);
        assertEquals(0, rotated720.y, 0.001);
    }

    @Test
    public void testPosRotationCoordinateTransform()
    {
        ImmutableVector2f robotLocation = new ImmutableVector2f(1, 1);
        float robotHeading = 7F * MathUtils.PI_F / 4;
        ImmutableVector2f absoluteCoord = new ImmutableVector2f(2, 2);

        float distance = robotLocation.distance(absoluteCoord);

        ImmutableVector2f relativeCoord = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteCoord, robotLocation, robotHeading);

        assertEquals(0, relativeCoord.x, 0.001);
        assertEquals(distance, relativeCoord.y, 0.001);
    }

    @Test
    public void testNegRotationCoordinateTransform()
    {
        ImmutableVector2f robotLocation = new ImmutableVector2f(1, 1);
        float robotHeading = MathUtils.PI_F / 4;
        ImmutableVector2f absoluteCoord = new ImmutableVector2f(2, 2);

        float distance = robotLocation.distance(absoluteCoord);

        ImmutableVector2f relativeCoord = MathUtils.LinearAlgebra.absoluteToRelativeCoord(absoluteCoord, robotLocation, robotHeading);

        assertEquals(distance, relativeCoord.x, 0.001);
        assertEquals(0, relativeCoord.y, 0.001);
    }

    @Test //fail
    public void testAbsoluteDPos45()
    {
        ImmutableVector2f dPos = MathUtils.Kinematics.getAbsoluteDPosLine(1, 1, 1F, (float) (Math.PI / 4F));

        assertEquals(-Math.sqrt(1 / 2F), dPos.x, 0.001);
        assertEquals(Math.sqrt(1 / 2F), dPos.y, 0.001);
    }

    @Test
    public void navXToRad()
    {
        // TODO: returns cw radians not ccw I think
        double rad = MathUtils.Kinematics.navXToRad(270);

        assertEquals(Math.PI / 2F, rad, 0.001);

        rad = MathUtils.Kinematics.navXToRad(270 + 360);
        assertEquals(Math.PI / 2F, rad, 0.001);

        rad = MathUtils.Kinematics.navXToRad(270 - 360);
        assertEquals(Math.PI / 2F, rad, 0.001);
    }

    /**
     * Should be a complete rotation around a circle (dpos = 0)
     */
    @Test
    public void arcDposArcStraight0Heading()
    {
        // l * pi = 1 (circumference)
        // 1/pi = l
        ImmutableVector2f absoluteDPosCurve = MathUtils.Kinematics.getAbsoluteDPosCurve(1, 1, 123, 1, 0);
        assertEquals(0, absoluteDPosCurve.x, 1);
        assertEquals(0, absoluteDPosCurve.y, 1);
    }

    @Test
    public void arcDposArcStraight45Heading()
    {
        // l * pi = 1 (circumference)
        // 1/pi = l
        ImmutableVector2f absoluteDPosCurve = MathUtils.Kinematics.getAbsoluteDPosCurve(1, 1, 123, 1, (float) (Math.PI / 4F));
        assertEquals(-Math.sqrt(1 / 2F), absoluteDPosCurve.x, 0.001);
        assertEquals(Math.sqrt(1 / 2F), absoluteDPosCurve.y, 0.001);
    }

    /**
     * Should be a complete rotation around a circle (dpos = 0)
     */
    @Test
    public void testDTheta90()
    {
        float dTheta = MathUtils.Geometry.getDThetaNavX(270, 0);
        assertEquals(3F * Math.PI / 2F, dTheta, 0.001);
    }

    /**
     * Should be a complete rotation around a circle (dpos = 0)
     */
    @Test
    public void testCCWClosest90()
    {
        Assert.assertFalse(MathUtils.Geometry.isCCWQuickest(0, 90));
    }

    /**
     * Should be a complete rotation around a circle (dpos = 0)
     */
    @Test
    public void testCCWClosest90Opp()
    {
        Assert.assertTrue(MathUtils.Geometry.isCCWQuickest(90, 0));
    }

    /**
     * Should be a complete rotation around a circle (dpos = 0)
     */
    @Test
    public void testCCWClosestStrangeAngles()
    {
        Assert.assertTrue(MathUtils.Geometry.isCCWQuickest(127, 359));
    }

    @Test
    public void testNavXBound()
    {
        assertEquals(355, MathUtils.Kinematics.navXBound(-5), 0.001);
    }

    @Test
    public void testLineIntersections()
    {
        MathUtils.Geometry.Line line = new MathUtils.Geometry.Line(new ImmutableVector2f(3.5F, 4F), new ImmutableVector2f(3.5F, 9F));
        ImmutableVector2f point = new ImmutableVector2f(3.4F, 6F);
        MathUtils.Geometry.Line perp = line.getPerp(point);
        ImmutableVector2f intersection = perp.intersection(line);
        System.out.println(intersection);
    }

    @Test
    public void testCircleLineSegmentIntersection()
    {
        ImmutableVector2f origin = new ImmutableVector2f();
        double r = 1;

        Set<ImmutableVector2f> intersections = MathUtils.Geometry.getCircleLineIntersectionPoint(new ImmutableVector2f(1, 1), new ImmutableVector2f(2, 1), origin, r);
        System.out.println("intersections = " + intersections);
        assertEquals(0, intersections.size());

        intersections = MathUtils.Geometry.getCircleLineIntersectionPoint(new ImmutableVector2f(0, 2), new ImmutableVector2f(0, -2), origin, r);
        System.out.println("intersections = " + intersections);
        assertEquals(2, intersections.size());
        assertTrue(intersections.contains(new ImmutableVector2f(0, -1)));
        assertTrue(intersections.contains(new ImmutableVector2f(0, 1)));

    }

    @Test
    public void testParametricLineDist()
    {
        MathUtils.ParametricFunction diagonal = new MathUtils.Geometry.ParametricLine(new ImmutableVector2f(0, 0),
                                                                                      new ImmutableVector2f(1, 1));

        assertEquals(new ImmutableVector2f(1, 1), MathUtils.Geometry.getClosestPointParametricFunc(diagonal, new ImmutableVector2f(2, 1)));

        MathUtils.ParametricFunction circle = new MathUtils.ParametricFunction() {
            @Override
            public double getX(double t)
            {
                return Math.cos(t) + 3;
            }

            @Override
            public double getY(double t)
            {
                return Math.sin(t);
            }
        };

        assertEquals(new ImmutableVector2f((float) Math.cos(Math.PI / 4) + 3, (float) Math.sin(Math.PI / 4)), MathUtils.Geometry.getClosestPointParametricFunc(circle, new ImmutableVector2f(3.5F, 0.5F)));
    }

    @Test
    public void testParametricArcLength()
    {
        MathUtils.ParametricFunction diagonal = new MathUtils.Geometry.ParametricLine(new ImmutableVector2f(0, 0),
                                                                                      new ImmutableVector2f(1, 1));

        assertEquals(MathUtils.ROOT_2, diagonal.getArcLength(0, 1), DELTA);

        MathUtils.ParametricFunction unitCircle = new MathUtils.ParametricFunction() {
            @Override
            public double getX(double t)
            {
                return Math.cos(t);
            }

            @Override
            public double getY(double t)
            {
                return Math.sin(t);
            }
        };

        assertEquals(MathUtils.TAU, unitCircle.getArcLength(0, MathUtils.TAU), 1E-3);
    }


}
