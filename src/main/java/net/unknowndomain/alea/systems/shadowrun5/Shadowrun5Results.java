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
import net.unknowndomain.alea.messages.MsgBuilder;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.roll.GenericResult;

/**
 *
 * @author journeyman
 */
public class Shadowrun5Results extends GenericResult
{
    private final List<SingleResult<Integer>> results;
    private int hits = 0;
    private int miss = 0;
    private int ones = 0;
    private List<SingleResult<Integer>> hitResults = new ArrayList<>();
    private Integer limit;
    private Shadowrun5Results prev;
    private boolean push = false;
    
    public Shadowrun5Results(List<SingleResult<Integer>> results)
    {
        List<SingleResult<Integer>> tmp = new ArrayList<>(results.size());
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
    
    public void addHit(SingleResult<Integer> dice)
    {
        addHits(1, dice);
    }
    
    private void addHits(int value, SingleResult<Integer> dice)
    {
        hits += value;
        hitResults.add(dice);
    }

    public int getHits()
    {
        return hits;
    }

    public List<SingleResult<Integer>> getResults()
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

    public List<SingleResult<Integer>> accessHitResults()
    {
        return hitResults;
    }

    @Override
    protected void formatResults(MsgBuilder messageBuilder, boolean verbose, int indentValue)
    {
        String indent = getIndent(indentValue);
        messageBuilder.append(indent).append("Hits: ").append(getHits());
        if ((limit != null) && (getHits() > limit) && (!push))
        {
            messageBuilder.append(" => ").append(limit);
        }
        messageBuilder.appendNewLine();
        messageBuilder.append(indent).append("Miss: ").append(getMiss()).append("( ");
        messageBuilder.append("Ones: ").append(getOnes()).append(" )\n");
        if (isCriticalGlitch() || isGlitch())
        {
            messageBuilder.append(indent);
        }
        if (isCriticalGlitch())
        {
            messageBuilder.append("Critical ");
        }
        if (isGlitch())
        {
            messageBuilder.append("Glitch").appendNewLine();
        }
        if (verbose)
        {
            messageBuilder.append(indent).append("Roll ID: ").append(getUuid()).appendNewLine();
            messageBuilder.append(indent).append("Results: ").append(" [ ");
            for (SingleResult<Integer> t : getResults())
            {
                messageBuilder.append("( ").append(t.getLabel()).append(" => ");
                messageBuilder.append(t.getValue()).append(") ");
            }
            messageBuilder.append("]\n");
            if (prev != null)
            {
                messageBuilder.append("Prev : {\n");
                prev.formatResults(messageBuilder, verbose, indentValue + 4);
                messageBuilder.append("}\n");
            }
        }
    }
    
    
    public Integer getLimit()
    {
        return limit;
    }

    public void setLimit(Integer limit)
    {
        this.limit = limit;
    }

    public Shadowrun5Results getPrev()
    {
        return prev;
    }

    public void setPrev(Shadowrun5Results prev)
    {
        this.prev = prev;
    }

    public boolean isPush()
    {
        return push;
    }

    public void setPush(boolean push)
    {
        this.push = push;
    }

}
