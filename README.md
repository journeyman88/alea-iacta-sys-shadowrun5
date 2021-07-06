# alea-iacta-sys-shadowrun5
A RPG system module for Alea Iacta Est implementing 7th Sea - 2nd Edition

## Description
This command will roll a pool of several d6, then according to the 5th edition rule will evaluate Hits, Miss (of which will count the ones), and also will communicate eventual Glitches and Critical Glitches. If a limit is provided it will also limit the success number to that (outside of modifiers).

### Roll modifiers
Passing these parameters, the associated modifier will be enabled:

* `-v` : Will enable a more verbose mode that will show a detailed version of every result obtained in the roll.
* `-p` : Will enable a the 'Push the Limit' mode, enabling the Rule of Six and ignoring an eventual limit.
* `-s` : Will enable a the 'Second Chance' mode, rerolling all dice in the previous roll that generated a Miss. Requires that the last roll of the user was of this system and that was done in the last 2 minutes.

## Help print
```
Shadowrun 5th Edition [ shadowrun-5th | sr5 ]

Usage: sr5 -n <numberOfDice>
   or: sr5 -s

Description:
This command will roll a pool of several d6, then according to the 5th edition
rule will evaluate Hits, Miss (of which will count the ones), and also will
communicate eventual Glitches and Critical Glitches. If a limit is provided it
will also limit the success number to that (outside of modifiers).

Options:
  -n, --number=diceNumber   Number of dice in the pool
  -l, --limit=hitLimit      Limit on the results
  -p, --push                Enable 'Push the Limit' mode
  -s, --second              Enable the 'Second Chance' mode
  -h, --help                Print the command help
  -v, --verbose             Enable verbose output
```
