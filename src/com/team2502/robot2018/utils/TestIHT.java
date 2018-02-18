package com.team2502.robot2018.utils;

/**
 * Not meant to be used in robot code, just on our computrons.
 */
class TestIHT
{
    public static void main(String[] args)
    {

        InterpolationMap wheee = new InterpolationMap(1d, 1d);
        System.out.println(wheee.get(100.0)); // should be 1
        System.out.println(wheee.get(-100.0)); // should be 1

        wheee.put(1002D, 101D); // slope: 1

        System.out.println(wheee.get(60.0)); // should be 61
        System.out.println(wheee.get(-60.0)); // should be 1
        System.out.println(wheee.get(600.0)); // should be 101

        wheee.put(200D, 1D); // slope: -1


        System.out.println(wheee.get(60.0)); // should be 61
        System.out.println(wheee.get(100.0)); // should be 101
        System.out.println(wheee.get(150.0)); // should be 51


    }
}
