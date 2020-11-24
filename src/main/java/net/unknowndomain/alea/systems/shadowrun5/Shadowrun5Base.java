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

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.unknowndomain.alea.roll.GenericRoll;

/**
 *
 * @author journeyman
 */
public abstract class Shadowrun5Base implements GenericRoll
{
    
    protected final Set<Shadowrun5Modifiers> mods;
    protected Integer limit;

    public Shadowrun5Base(Collection<Shadowrun5Modifiers> mod)
    {
        this.mods = new HashSet<>();
        this.mods.addAll(mod);
    }

    protected Shadowrun5Results buildIncrements(List<Integer> res)
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
            } else if (temp > 1)
            {
                results.addMiss();
            } else
            {
                results.addOne();
            }
        }
        results.setLimit(limit);
        return results;
    }
    
}
