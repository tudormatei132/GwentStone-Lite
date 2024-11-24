Matei Tudor-Andrei - 324CA

# TEMA 0 - GwentStone Light

## Short description of the app
	
The application gives the result of a board game (inspired by HearthStone) using
the given commands as input and also can print the status of the game while it's
being played.

The input is given in the JSON format and the results will be printed into 
files in the same format. For the managing the input and printing the output,
the Object Mapper, ArrayNode, ObjectNode and ObjectWritter classes from
the com.fasterxml.jackson.databind library were used. The class that represents
objects that will be printed will also have a method which will return an ObjectNode
that contains the needed stats which will later be added to the output ArrayNode.

## Classes

### Gameplay Package

Contains the following classes:

#### Game

- is used to set up the games (e.g. extracting the data from the input and
creating the decks for each player, setting the starting player)
- used to also execute all the games, but without handling any command
- has variables to count different stats ( the number of games played and
the number of wins for every player)
- contains some variables that are used by the CommandHandler too,
such as the currentAction or the output node. I did this so every method
from the CommandHandler class has only 1 argument, needed because of
the way I defined the HashMap.

#### Player

- stores information about the player for one game
- the information stored is current mana, deck, hand, hero and player id
- also used to return cards from hand, get the corresponding row number
for a specific card based on the player id and the card's name
- has a method to check if the player has enough mana for an action


#### Constants
- as the name suggests, contains different constants used during coding


#### Command Handler

- it's the function that does the printing (adds to the output ArrayNode)
- calls the methods from other classes that implement the wanted functionality
- checks for any error during the commands
- it also contains a hashmap that it's used to associate a method(a consumer) to
every command string. This way, 
- all methods receive one parameter, the Game handler, that is used to get
the needed action or the output node
- implemented using the Singleton pattern

#### Board and Row 
- the classes manage the cards that were placed on the board (they will are used
to remove them when they're dead and update their stats constantly)
- the board was implemented using the Singleton pattern because there is no need
for more than one board. This implementation needed me to reset the board 
after every test, otherwise the old cards could've been visible on the board
even if they didn't exist in the next test.
### Cards package

#### Card

- containts the basic stats, common for all cards
#### Minion

- contains methods to check if the card is able to attack and to reset its state
- has a method to set the abilities if the minion should have one

#### Hero

- has a similar method to the one present in the Minion class that sets the 
Hero Ability

#### Abilities
The abilities are present in another package. Each ability has a different class
which extends a different class, either Ability or HeroAbility, based on its type.
They were implemented in a similar way to the Command pattern, the Ability class
also containing the caster Minion, but every ability extends an abstract class which
has a method named useAbility(), the "invoker" being the Hero or the Minion.
The package is named "abilties" and the useAbility() function has one parameter,
a row for the hero abilties or a minion for the minion abilities. 

The Skyjack class also contains the caster because we need to make some changes
to it. That's why the constructor has a parameter for the minion, which will be
given as "this" when the ability is going to be set for the "Mirage" minion.  

 