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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.unknowndomain.alea.command.HelpWrapper;
import net.unknowndomain.alea.messages.ReturnMsg;
import net.unknowndomain.alea.systems.RpgSystemCommand;
import net.unknowndomain.alea.systems.RpgSystemDescriptor;
import net.unknowndomain.alea.roll.GenericRoll;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
    
    private static final String NUMBER_PARAM = "number";
    private static final String LIMIT_PARAM = "limit";
    private static final String PUSH_PARAM = "push";
    private static final String SECOND_PARAM = "second";
    private static final Options CMD_OPTIONS;
    
    static{
        CMD_OPTIONS = new Options();
        CMD_OPTIONS.addOption(
                Option.builder("n")
                        .longOpt(NUMBER_PARAM)
                        .desc("Number of dice in the pool")
                        .hasArg()
                        .argName("diceNumber")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("l")
                        .longOpt(LIMIT_PARAM)
                        .desc("Limit on the results")
                        .hasArg()
                        .argName("hitLimit")
                        .build()
        );
        
        CMD_OPTIONS.addOption(
                Option.builder("h")
                        .longOpt( CMD_HELP )
                        .desc( "Print help")
                        .build()
        );
        CMD_OPTIONS.addOption(
                Option.builder("v")
                        .longOpt(CMD_VERBOSE)
                        .desc("Enable verbose output")
                        .build()
        );
        
        OptionGroup mod = new OptionGroup();
        
        mod.addOption(
                Option.builder("p")
                        .longOpt(PUSH_PARAM)
                        .desc("Enable 'Push the Limit' mode")
                        .build()
        );
        
        mod.addOption(
                Option.builder("s")
                        .longOpt(SECOND_PARAM)
                        .desc("Enable the 'Second Chance' mode")
                        .build()
        );
        
        CMD_OPTIONS.addOptionGroup(mod);
        
    }
    
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
    protected Optional<GenericRoll> safeCommand(String cmdParams)
    {
        Optional<GenericRoll> retVal;
        try
        {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(CMD_OPTIONS, cmdParams.split(" "));
            if (cmd.hasOption(CMD_HELP))
            {
                return Optional.empty();
            }

            Set<Shadowrun5Modifiers> mods = new HashSet<>();

            if (cmd.hasOption(CMD_VERBOSE))
            {
                mods.add(Shadowrun5Modifiers.VERBOSE);
            }
            if (cmd.hasOption(PUSH_PARAM))
            {
                mods.add(Shadowrun5Modifiers.PUSH_THE_LIMIT);
            }
            GenericRoll roll;
            if (cmd.hasOption(SECOND_PARAM))
            {
                mods.add(Shadowrun5Modifiers.SECOND_CHANCE);
                roll = new Shadowrun5Reroll(mods);
            }
            else
            {
                String n = cmd.getOptionValue(NUMBER_PARAM);
                if (cmd.hasOption(LIMIT_PARAM))
                {
                    String l = cmd.getOptionValue(LIMIT_PARAM);
                    roll = new Shadowrun5Roll(Integer.parseInt(n), Integer.parseInt(l), mods);
                }
                else
                {
                    roll = new Shadowrun5Roll(Integer.parseInt(n), mods);
                }
            }
            retVal = Optional.of(roll);
        } 
        catch (ParseException | NumberFormatException ex)
        {
            retVal = Optional.empty();
        }
        return retVal;
    }
    
    @Override
    public ReturnMsg getHelpMessage(String cmdName)
    {
        return HelpWrapper.printHelp(cmdName, CMD_OPTIONS, true);
    }
    
}
