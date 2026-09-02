/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.unknowndomain.alea.systems.shadowrun5;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.unknowndomain.alea.systems.RpgSystemOptions;
import net.unknowndomain.alea.systems.annotations.RpgSystemData;
import net.unknowndomain.alea.systems.annotations.RpgSystemOption;


/**
 *
 * @author journeyman
 */
@RpgSystemData(bundleName = "net.unknowndomain.alea.systems.shadowrun5.RpgSystemBundle")
public class Shadowrun5Options extends RpgSystemOptions
{
    @RpgSystemOption(name = "number", shortcode = "n", description = "shadowrun5.options.number", argName = "diceNumber")
    private Integer numberOfDice;
    @RpgSystemOption(name = "limit", shortcode = "l", description = "shadowrun5.options.limit", argName = "hitLimit")
    private Integer limit;
    @RpgSystemOption(name = "push", shortcode = "p", description = "shadowrun5.options.push")
    private boolean pushTheLimit;
    @RpgSystemOption(name = "second", shortcode = "s", description = "shadowrun5.options.second")
    private boolean secondChance;
    
    
    @Override
    public boolean isValid()
    {
        return !(isHelp() || (isPushTheLimit() && isSecondChance()));
    }

    public Integer getNumberOfDice()
    {
        return numberOfDice;
    }

    public Integer getLimit()
    {
        return limit;
    }
    
    public boolean isPushTheLimit()
    {
        return pushTheLimit;
    }

    public boolean isSecondChance()
    {
        return secondChance;
    }

    public Collection<Shadowrun5Modifiers> getModifiers()
    {
        Set<Shadowrun5Modifiers> mods = new HashSet<>();
        if (isVerbose())
        {
            mods.add(Shadowrun5Modifiers.VERBOSE);
        }
        if (isPushTheLimit())
        {
            mods.add(Shadowrun5Modifiers.PUSH_THE_LIMIT);
        }
        if (isSecondChance())
        {
            mods.add(Shadowrun5Modifiers.SECOND_CHANCE);
        }
        return mods;
    }
    
}