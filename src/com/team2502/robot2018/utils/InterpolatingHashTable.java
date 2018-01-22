package com.team2502.robot2018.utils;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.*;

//TODO: Single precision floats

/**
 * Make a new interpolating hash table. You need 2 key/value pairs to interpolate properly.
 * An interpolating hash table is like a crappy kind of regression.
 * You put in (x, f(x)) pairs of the function that you know for sure,
 * and linear regression is used to find the pairs you didn't explicitly put in.
 */
public class InterpolatingHashTable
{

    private HashMap<Number, Number> table;

    /**
     * @param firstKey   The first key to put into the hash table.
     * @param firstValue The first value to put into the hash table.
     */
    public InterpolatingHashTable(Number firstKey, Number firstValue)
    {
        table = new HashMap<Number, Number>();
        table.put(firstKey, firstValue);
    }

    /**
     * Make a new interpolating hash table
     *
     * @param initTable The table to start with
     */
    public InterpolatingHashTable(HashMap<Number, Number> initTable)
    {
        if(initTable.keySet().isEmpty())
        {
            throw new ValueException("Your initial table for the InterpolatingHashTable musn't be empty!");
        }
        else
        {
            table = initTable;
        }
    }

    /**
     * Add a pair that you know is correct
     * @param key The "x" value that you put in
     * @param value The "f(x)" value that you got out
     */
    public void put(Number key, Number value)
    {
        table.put(key, value);
    }

    /**
     * Use linear regression to estimate what your "f(x)" will give when evaluated at the number `key`
     * @param key The number to evaluate "f(x)" at
     * @return The estimated value of "f(key)"
     */
    public Number get(Number key)
    {
        Set<Number> keyset = table.keySet();

        if(keyset.size() == 1)
        {
            return table.get(keyset.toArray()[0]);
        }

        Number upperBound = null;
        Number lowerBound = null;


        ArrayList<Number> keys = new ArrayList<Number>(keyset);
        keys.sort(Comparator.comparingDouble(a -> (int) a));

        int i = 0;

        for(Number a_key : keys)
        {

            if(key.floatValue() < a_key.floatValue())
            {
                upperBound = a_key;
                if(i > 0) {lowerBound = keys.get(i - 1);}
                break;
            }
            i++;
        }

        if (upperBound == null) // i.e all the keys are smaller
        {
            return table.get(keys.get(keys.size() - 1)).doubleValue(); // get the f(x) for the biggest x. we can't do real interpolation
        }
        else if (lowerBound == null) // i.e all the keys are bigger
        {
            return table.get(keys.get(0)).doubleValue(); // get the f(x) for the smallest x. we can't do real interpolation.
        }
        else // we can do real interpolation
        {
            double dx = upperBound.doubleValue() - lowerBound.doubleValue();
            double dy = table.get(upperBound).doubleValue() - table.get(lowerBound).doubleValue();

            double slope = dy/dx; // remember? rise over run. change in y over change in x.

            // the following equation comes from the point slope form of a line
            // reminder: it's y - y1 = m (x - x1)
            // here, m is slope
            // upperBound is x1
            // table.get(upperBound) is y1
            double value = slope * key.doubleValue() - slope * upperBound.doubleValue() + table.get(upperBound).doubleValue();
            return value;
        }
    }
}
