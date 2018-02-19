package com.team2502.robot2018.utils;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.util.*;

/**
 * Make a new interpolating map. You need 2 key/value pairs to interpolate properly.
 * Interpolation is like a crappy kind of regression.
 * You put in (x, f(x)) pairs of the function that you know for sure,
 * and linear regression is used to find the pairs you didn't explicitly put in.
 */
public class InterpolationMap implements Map<Double, Double>
{

    private HashMap<Double, Double> table;

    /**
     * @param firstKey   The first key to put into the hash table.
     * @param firstValue The first value to put into the hash table.
     */
    public InterpolationMap(Double firstKey, Double firstValue)
    {
        table = new HashMap<Double, Double>();
        table.put(firstKey, firstValue);
    }

    /**
     * Make a new interpolating hash table
     *
     * @param initTable The table to start with
     */
    public InterpolationMap(HashMap<Double, Double> initTable)
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
     * If you need this you're doing it wrong
     *
     * @return How many defined values there are (i.e how many values we aren't guessing with linear regression)
     */
    @Override
    public int size()
    {
        return table.size();
    }

    @Override
    public boolean isEmpty()
    {
        return table.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return table.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return table.containsValue(value);
    }

    @Override
    public Double get(Object key) throws IllegalArgumentException
    {
        if(key.getClass() == Double.class)
        {
            return this.get((Double) key);
        }
        throw new IllegalArgumentException("The key was not an instance of the Double class");
    }

    /**
     * Add a pair that you know is correct
     *
     * @param key   The "x" value that you put in
     * @param value The "f(x)" value that you got out
     */
    public Double put(Double key, Double value)
    {
        return table.put(key, value);
    }

    @Override
    public Double remove(Object key)
    {
        return table.remove(key);
    }

    @Override
    public void putAll(Map<? extends Double, ? extends Double> m)
    {
        table.putAll(m);
    }

    @Override
    public void clear()
    {
        table.clear();
    }

    @Override
    public Set<Double> keySet()
    {
        return table.keySet();
    }

    @Override
    public Collection<Double> values()
    {
        return table.values();
    }

    @Override
    public Set<Entry<Double, Double>> entrySet()
    {
        return table.entrySet();
    }

    /**
     * Use linear regression to estimate what your "f(x)" will give when evaluated at the Double `key`
     *
     * @param key The Double to evaluate "f(x)" at
     * @return The estimated value of "f(key)"
     */
    public Double get(Double key)
    {
        Set<Double> keyset = table.keySet();

        if(keyset.size() == 1)
        {
            return table.get(keyset.toArray()[0]);
        }

        Double upperBound = null;
        Double lowerBound = null;


        ArrayList<Double> keys = new ArrayList<>(keyset);
        keys.sort(Comparator.comparingDouble(a -> (double) a));

        int i = 0;

        for(Double a_key : keys)
        {
            if(key.floatValue() < a_key.floatValue())
            {
                upperBound = a_key;
                if(i > 0) {lowerBound = keys.get(i - 1);}
                break;
            }
            i++;
        }

        if(upperBound == null) // i.e all the keys are smaller
        {
            return table.get(keys.get(keys.size() - 1)); // get the f(x) for the biggest x. we can't do real interpolation
        }
        else if(lowerBound == null) // i.e all the keys are bigger
        {
            return table.get(keys.get(0)); // get the f(x) for the smallest x. we can't do real interpolation.
        }
        else // we can do real interpolation
        {
            double dx = upperBound - lowerBound;
            double dy = table.get(upperBound) - table.get(lowerBound);

            double slope = dy / dx; // remember? rise over run. change in y over change in x.

            // the following equation comes from the point slope form of a line
            // reminder: it's y - y1 = m (x - x1)
            // here, m is slope
            // upperBound is x1
            // table.get(upperBound) is y1
            return ((slope * key) - (slope * upperBound)) + table.get(upperBound);
        }
    }
}
