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
import java.util.List;
import net.unknowndomain.alea.random.SingleResult;
import net.unknowndomain.alea.random.dice.DicePool;
import net.unknowndomain.alea.random.dice.bag.D6;
import net.unknowndomain.alea.roll.GenericResult;

/**
 *
 * @author journeyman
 */
public class Shadowrun5Roll extends Shadowrun5Base
{
    
    private final DicePool<D6> dicePool;
    
    public Shadowrun5Roll(Integer dice, Shadowrun5Modifiers ... mod)
    {
        this(dice, Arrays.asList(mod));
    }
    
    public Shadowrun5Roll(Integer trait, Integer limit, Shadowrun5Modifiers ... mod)
    {
        this(trait, limit, Arrays.asList(mod));
    }
    
    public Shadowrun5Roll(Integer dice, Collection<Shadowrun5Modifiers> mod)
    {
        this(dice, null, mod);
    }
    
    public Shadowrun5Roll(Integer dice, Integer limit, Collection<Shadowrun5Modifiers> mod)
    {
        super(mod);
        if (mods.contains(Shadowrun5Modifiers.PUSH_THE_LIMIT))
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
    public GenericResult getResult()
    {
        List<SingleResult<Integer>> resultsPool = this.dicePool.getResults();
        List<SingleResult<Integer>> res = new ArrayList<>();
        res.addAll(resultsPool);
        Shadowrun5Results results = buildIncrements(res);
        results.setPush(mods.contains(Shadowrun5Modifiers.PUSH_THE_LIMIT));
        results.setVerbose(mods.contains(Shadowrun5Modifiers.VERBOSE));
        return results;
    }
    
}
