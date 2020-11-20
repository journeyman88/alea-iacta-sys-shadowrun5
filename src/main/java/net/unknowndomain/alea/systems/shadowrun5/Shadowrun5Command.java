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
import java.util.Set;
import java.util.regex.Matcher;
import net.unknowndomain.alea.command.HelpWrapper;
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
import org.javacord.api.entity.message.MessageBuilder;
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
                        .required()
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
    public MessageBuilder execCommand(String cmdLine)
    {
        MessageBuilder retVal = new MessageBuilder();
        Matcher prefixMatcher = PREFIX.matcher(cmdLine);
        if (prefixMatcher.matches())
        {
            String params = prefixMatcher.group(CMD_PARAMS);
            if (params == null || params.isEmpty())
            {
                return HelpWrapper.printHelp(prefixMatcher.group(CMD_NAME), CMD_OPTIONS, true);
            }
            LOGGER.debug(cmdLine);
            try
            {
                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = parser.parse(CMD_OPTIONS, params.split(" "));
                if (cmd.hasOption(CMD_HELP))
                {
                    return HelpWrapper.printHelp(prefixMatcher.group(CMD_NAME), CMD_OPTIONS, true);
                }
                
                Set<Shadowrun5Roll.Modifiers> mods = new HashSet<>();
                
                if (cmd.hasOption(PUSH_PARAM))
                {
                    mods.add(Shadowrun5Roll.Modifiers.PUSH_THE_LIMIT);
                }
                if (cmd.hasOption(SECOND_PARAM))
                {
                    mods.add(Shadowrun5Roll.Modifiers.SECOND_CHANCE);
                }
                if (cmd.hasOption(CMD_VERBOSE))
                {
                    mods.add(Shadowrun5Roll.Modifiers.VERBOSE);
                }
                String n = cmd.getOptionValue(NUMBER_PARAM);
                GenericRoll roll;
                if (cmd.hasOption(LIMIT_PARAM))
                {
                    String l = cmd.getOptionValue(LIMIT_PARAM);
                    roll = new Shadowrun5Roll(Integer.parseInt(n), Integer.parseInt(l), mods);
                }
                else
                {
                    roll = new Shadowrun5Roll(Integer.parseInt(n), mods);
                }
                retVal = roll.getResult();
            } 
            catch (ParseException | NumberFormatException ex)
            {
                retVal = HelpWrapper.printHelp(prefixMatcher.group(CMD_NAME), CMD_OPTIONS, true);
            }
        }
        return retVal;
    }
    
}
