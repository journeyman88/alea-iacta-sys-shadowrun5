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

import java.util.Locale;
import java.util.Optional;
import net.unknowndomain.alea.roll.GenericRoll;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author journeyman
 */
public class Shadowrun5Command extends RpgSystemCommand
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Shadowrun5Command.class);
    private static final RpgSystemDescriptor DESC = new RpgSystemDescriptor("Shadowrun 5th Edition", "sr5", "shadowrun-5th");
    
    public Shadowrun5Command()
    {
        
    }
    
    @Override
    public RpgSystemDescriptor getCommandDesc()
    {
        return DESC;
    }

    @Override
    protected Logger getLogger()
    {
        return LOGGER;
    }

    @Override
    protected Optional<GenericRoll> safeCommand(RpgSystemOptions options, Locale lang)
    {
        Optional<GenericRoll> retVal;
        if (options.isHelp() || !(options instanceof Shadowrun5Options) )
        {
            retVal = Optional.empty();
        }
        else
        {
            Shadowrun5Options opt = (Shadowrun5Options) options;
            GenericRoll roll; 
            if (opt.isSecondChance())
            {
                roll = new Shadowrun5Reroll(opt.getModifiers());
            }
            else
            {
                if (opt.getLimit() != null)
                {
                    roll = new Shadowrun5Roll(opt.getNumberOfDice(), opt.getModifiers());
                }
                else
                {
                    roll = new Shadowrun5Roll(opt.getNumberOfDice(), opt.getLimit(), opt.getModifiers());
                }
            }
            retVal = Optional.of(roll);
        }
        return retVal;
    }

    @Override
    public RpgSystemOptions buildOptions()
    {
        return new Shadowrun5Options();
    }
    
}
