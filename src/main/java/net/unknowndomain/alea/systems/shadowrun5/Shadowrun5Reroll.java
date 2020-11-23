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
import net.unknowndomain.alea.dice.standard.D6;
import net.unknowndomain.alea.pools.DicePool;
import net.unknowndomain.alea.roll.GenericResult;
import net.unknowndomain.alea.roll.StatefulRoll;

/**
 *
 * @author journeyman
 */
public class Shadowrun5Reroll extends Shadowrun5Base implements StatefulRoll
{
    
    private Shadowrun5Results prev;
    
    public Shadowrun5Reroll(Shadowrun5Modifiers ... mod)
    {
        this(Arrays.asList(mod));
    }
    
    public Shadowrun5Reroll(Collection<Shadowrun5Modifiers> mod)
    {
        super(mod);
    }
    
    @Override
    public GenericResult getResult()
    {
        Shadowrun5Results results = prev;
        if (mods.contains(Shadowrun5Modifiers.SECOND_CHANCE) && (results.getMiss() > 0) && (!results.isPush()))
        {
            DicePool<D6> reroll;
            if (mods.contains(Shadowrun5Modifiers.PUSH_THE_LIMIT))
            {
                reroll = new DicePool<>(D6.INSTANCE, results.getMiss(), 10);
            }
            else
            {
                reroll = new DicePool<>(D6.INSTANCE, results.getMiss());
            }
            boolean done = false;
            List<Integer> res = new ArrayList<>();
            res.addAll(reroll.getResults());
            res.addAll(results.getHitResults());
            results = buildIncrements(res);
            results.setPrev(prev);
        }
        results.setPush(mods.contains(Shadowrun5Modifiers.PUSH_THE_LIMIT));
        results.setVerbose(mods.contains(Shadowrun5Modifiers.VERBOSE));
        return results;
    }

    @Override
    public boolean loadState(GenericResult state)
    {
        boolean retVal = false;
        if (state instanceof Shadowrun5Results)
        {
            prev = (Shadowrun5Results) state;
            limit = prev.getLimit();
            retVal = true;
        }
        return retVal;
    }
    
}
