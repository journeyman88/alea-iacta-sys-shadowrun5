/*
 * Copyright 2020 Marco Bignami.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.unknowndomain.alea.systems.shadowrun5;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author journeyman
 */
public class Shadowrun5Results
{
    private final List<Integer> results;
    private int hits = 0;
    private int miss = 0;
    private int ones = 0;
    private List<Integer> hitResults = new ArrayList<>();
    
    public Shadowrun5Results(List<Integer> results)
    {
        List<Integer> tmp = new ArrayList<>(results.size());
        tmp.addAll(results);
        this.results = Collections.unmodifiableList(tmp);
    }
    
    public boolean isGlitch()
    {
        BigDecimal check = new BigDecimal(hits + miss);
        check = check.divide(new BigDecimal(2), 0, RoundingMode.CEILING);
        return ones > check.intValue();
    }
    
    public boolean isCriticalGlitch()
    {
        return (hits == 0) && isGlitch();
    }
    
    public void addMiss()
    {
        addMiss(1);
    }
    
    public void addMiss(int value)
    {
        miss += value;
    }
    
    public void addOne()
    {
        addOne(1);
    }
    
    private void addOne(int value)
    {
        ones += value;
        miss += value;
    }
    
    public void addHit(Integer dice)
    {
        addHits(1, dice);
    }
    
    private void addHits(int value, Integer dice)
    {
        hits += value;
        hitResults.add(dice);
    }

    public int getHits()
    {
        return hits;
    }

    public List<Integer> getResults()
    {
        return results;
    }

    public int getMiss()
    {
        return miss;
    }

    public int getOnes()
    {
        return ones;
    }

    public List<Integer> getHitResults()
    {
        return hitResults;
    }

}
