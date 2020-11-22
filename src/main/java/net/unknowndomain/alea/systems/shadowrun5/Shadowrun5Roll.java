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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.unknowndomain.alea.dice.D6;
import net.unknowndomain.alea.messages.MsgBuilder;
import net.unknowndomain.alea.messages.ReturnMsg;
import net.unknowndomain.alea.pools.DicePool;
import net.unknowndomain.alea.roll.GenericRoll;

/**
 *
 * @author journeyman
 */
public class Shadowrun5Roll implements GenericRoll
{
    
    public enum Modifiers
    {
        VERBOSE,
        PUSH_THE_LIMIT,
        SECOND_CHANCE
    }
    
    private final DicePool<D6> dicePool;
    private final Integer limit;
    private final Set<Modifiers> mods;
    
    public Shadowrun5Roll(Integer dice, Modifiers ... mod)
    {
        this(dice, Arrays.asList(mod));
    }
    
    public Shadowrun5Roll(Integer trait, Integer limit, Modifiers ... mod)
    {
        this(trait, limit, Arrays.asList(mod));
    }
    
    public Shadowrun5Roll(Integer dice, Collection<Modifiers> mod)
    {
        this(dice, null, mod);
    }
    
    public Shadowrun5Roll(Integer dice, Integer limit, Collection<Modifiers> mod)
    {
        this.mods = new HashSet<>();
        this.mods.addAll(mod);
        if (mods.contains(Modifiers.PUSH_THE_LIMIT))
        {
            this.dicePool = new DicePool<>(D6.INSTANCE, dice, 6);
        }
        else
        {
            this.dicePool = new DicePool<>(D6.INSTANCE, dice);
        }
        this.limit = limit;
    }
    
    @Override
    public ReturnMsg getResult()
    {
        List<Integer> resultsPool = this.dicePool.getResults();
        List<Integer> res = new ArrayList<>();
        res.addAll(resultsPool);
        Shadowrun5Results results = buildIncrements(res);
        Shadowrun5Results results2 = null;
        if (mods.contains(Modifiers.SECOND_CHANCE) && (results.getMiss() > 0) )
        {
            DicePool<D6> reroll;
            if (mods.contains(Modifiers.PUSH_THE_LIMIT))
            {
                reroll = new DicePool<>(D6.INSTANCE, results.getMiss(), 10);
            }
            else
            {
                reroll = new DicePool<>(D6.INSTANCE, results.getMiss());
            }
            boolean done = false;
            res = new ArrayList<>();
            res.addAll(reroll.getResults());
            res.addAll(results.getHitResults());
            results2 = results;
            results = buildIncrements(res);
        }
        return formatResults(results, results2);
    }
    
    private ReturnMsg formatResults(Shadowrun5Results results, Shadowrun5Results results2)
    {
        MsgBuilder mb = new MsgBuilder();
        
        mb.append("Hits: ").append(results.getHits());
        if ((limit != null) && (results.getHits() > limit) && (!mods.contains(Modifiers.PUSH_THE_LIMIT)))
        {
            mb.append(" => ").append(limit);
        }
        mb.appendNewLine();
        mb.append("Miss: ").append(results.getMiss()).append("( ");
        mb.append("Ones: ").append(results.getOnes()).append(" )\n");
        if (results.isCriticalGlitch())
        {
            mb.append("Critical ");
        }
        if (results.isGlitch())
        {
            mb.append("Glitch").appendNewLine();
        }
        if (mods.contains(Modifiers.VERBOSE))
        {
            mb.append("Results: ").append(" [ ");
            for (Integer t : results.getResults())
            {
                mb.append(t).append(" ");
            }
            mb.append("]\n");
            if (results2 != null)
            {
                mb.append("Prev : {\n");
                mb.append("    Hits: ").append(results2.getHits());
                if ((results2.getHits() > limit) &&  !mods.contains(Modifiers.PUSH_THE_LIMIT))
                {
                    mb.append(" => ").append(limit);
                }
                mb.appendNewLine();
                mb.append("    Miss: ").append(results2.getMiss()).append("( ");
                mb.append("Ones: ").append(results2.getOnes()).append(" )\n");
                if (results2.isCriticalGlitch())
                {
                    mb.append("    Critical ");
                }
                if (results2.isGlitch())
                {
                    if (!results2.isCriticalGlitch())
                    {
                        mb.append("    ");
                    }
                    mb.append("Glitch").appendNewLine();
                }
                mb.append("    Results: ").append(" [ ");
                for (Integer t : results2.getResults())
                {
                    mb.append(t).append(" ");
                }
                mb.append("]\n");
                mb.append("}\n");
            }
        }
        return mb.build();
    }
    
    private Shadowrun5Results buildIncrements(List<Integer> res)
    {
        res.sort((Integer o1, Integer o2) ->
        {
            return -1 * o1.compareTo(o2);
        });
        Shadowrun5Results results = new Shadowrun5Results(res);
        int max = res.size();
        for (int i = 0; i < max; i++)
        {
            int temp = res.remove(0);
            if (temp >= 5)
            {
                results.addHit(temp);
            }
            else if (temp > 1)
            {
                results.addMiss();
            }
            else
            {
                results.addOne();
            }
        }
        return results;
    }
}
