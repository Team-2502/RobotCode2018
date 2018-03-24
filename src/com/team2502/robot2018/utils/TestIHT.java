package com.team2502.robot2018.utils;

import org.joml.ImmutableVector2d;

/**
 * Tests the InterpolationMap
 * <p>
 * Not meant to be used in robot code, just on our computrons.
 * <p>
 * Run with the `-ea` option in the JVM
 *
 * @see InterpolationMap
 */
class TestIHT
{
    public static void main(String[] args)
    {
        testLineIntegration();
        interpolatingIntegrationTest();

    }

    private static void testLineIntegration()
    {
        ImmutableVector2d a = new ImmutableVector2d(3, 4);
        ImmutableVector2d b = new ImmutableVector2d(-2, -1);

        MathUtils.Geometry.Line some_line = new MathUtils.Geometry.Line(a, b);

        assert some_line.integrate(0, 1) == 1.5 : "Expected 1.5, got " + some_line.integrate(0, 1);

        assert some_line.integrate(0, 5) == 17.5 : "Expected 17.5, got " + some_line.integrate(0, 5);
        assert some_line.integrate(-10, 0) == -40 : "Expected -40, got " + some_line.integrate(-10, 0);

        System.out.println("Line tests were fine");
    }

    private static void interpolatingIntegrationTest()
    {
        InterpolationMap map = new InterpolationMap(0D, 0D);
        map.put(1D, 1D);
        map.put(2D, 1D);
        map.put(3D, 0D);


        assert map.integrate(0, 1) == 0.5 : "Expected .5, got " + map.integrate(0, 1);
        assert map.integrate(0, 2) == 1.5 : "Expected 1.5, got " + map.integrate(0, 2);
        assert map.integrate(0, 3) == 2 : "Expected 2, got " + map.integrate(0, 3);
        assert map.integrate(0, 10) == 2 : "Expected 2, got " + map.integrate(0, 10);
        assert map.integrate(-10, 0) == 0 : "Expected 0, got " + map.integrate(-10, 0);

        System.out.println("Map tests were fine");
    }
}
