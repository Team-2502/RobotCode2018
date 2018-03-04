package com.team2502.robot2018.utils;

/**
 * Not meant to be used in robot code, just on our computrons.
 */
class TestIHT
{
    public static void main(String[] args)
    {

        InterpolationMap wheee = new InterpolationMap(2d, 2d);

        System.out.println(wheee.get(100.0)); // should be 2
        System.out.println(wheee.integrate(0, 1)); // should be 2

        wheee.put(0D, 0D);

        System.out.println(wheee.get(100.0)); // should be 2 (flattening after max)
        System.out.println(wheee.get(1D)); // should be 1
        System.out.println(wheee.integrate(0, 2)); // should be 1
        System.out.println(wheee.integrate(0, 1)); // should be .5


    }
}
