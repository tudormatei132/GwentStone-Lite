package org.poo.gameplay;

import org.poo.cards.Minion;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.Coordinates;
import java.util.Map;
import java.util.function.Consumer;

/*
    will add to the output node the result of the given actions
 */
public final class CommandHandler {

    private static CommandHandler instance = null;
    private CommandHandler() {
    }

    /**
     * necessary for the Singleton pattern
     * @return the instance of the handler
     */
    public static CommandHandler getInstance() {
        if (instance == null) {
            instance = new CommandHandler();
        }
        return instance;
    }

    /*
    since all methods have the same parameter, I thought a map
    would be a good choice to skip some if-else statements
     */
    private final Map<String, Consumer<Game>> map = Map.ofEntries(
            Map.entry("getPlayerDeck", this::getPlayerDeck),
            Map.entry("endPlayerTurn", this::endPlayerTurn),
            Map.entry("getPlayerHero", this::getPlayerHero),
            Map.entry("getPlayerTurn", this::getPlayerTurn),
            Map.entry("placeCard", this::placeCard),
            Map.entry("getCardsInHand", this::getCardsInHand),
            Map.entry("getCardsOnTable", this::getCardsOnTable),
            Map.entry("getPlayerMana", this::getPlayerMana),
            Map.entry("cardUsesAttack", this::cardUsesAttack),
            Map.entry("cardUsesAbility", this::cardUsesAbility),
            Map.entry("getCardAtPosition", this::getCardAtPosition),
            Map.entry("useAttackHero", this::useAttackHero),
            Map.entry("useHeroAbility", this::useHeroAbility),
            Map.entry("getFrozenCardsOnTable", this::getFrozenCardsOnTable),
            Map.entry("getTotalGamesPlayed", this::getTotalGamesPlayed),
            Map.entry("getPlayerOneWins", this::getPlayerOneWins),
            Map.entry("getPlayerTwoWins", this::getPlayerTwoWins)
    );

    /**
     *  searches the HashMap for the method that corresponds to the
     *  received command and calls it
     * @param command the command sent by the game manager
     * @param game  the game manager
     *
     */
    public void executeCommand(final String command, final Game game) {
        map.get(command).accept(game);
    }

    /**
     * will be used to help to print commands that have an output
     * @param a the command that will be printed
     * @param mapper the mapper used to write into the output node
     * @return the ObjectNode which will be added to the output node, in JSON format
     */
    private ObjectNode printCommand(final ActionsInput a, final ObjectMapper mapper) {
        ObjectNode on = mapper.createObjectNode();
        on.put("command", a.getCommand());
        if (a.getPlayerIdx() != 0) {
            on.put("playerIdx", a.getPlayerIdx());
        }
        return on;
    }

    /**
     * adds the current player's deck to output
     * @param game the game manager
     */
    private void getPlayerDeck(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        int idx = game.getCurrentAction().getPlayerIdx() - 1;
        node.put("output", game.getPlayers()[idx].getCurrentDeck().printDeck(game.getMapper()));
        game.getOutput().add(node);
    }

    /**
     *  reset the state of the cards of the next player and checks if a round
     *  has finished, and gives mana and a card to each player if that's the case
     * @param game the game manager
     */
    private void endPlayerTurn(final Game game) {
        Board.getInstance().resetCards(game.getCurrentPlayer() + 1);
        game.setCurrentPlayer((game.getCurrentPlayer() + 1) % 2);
        if (game.getCurrentPlayer() == game.getStartingPlayer()) {
            // when it's the starting player's turn again, a round has finished
            game.setRound(game.getRound() + 1);
            game.getPlayers()[0].addMana(game.getRound());
            game.getPlayers()[1].addMana(game.getRound());
            game.getPlayers()[0].drawCard();
            game.getPlayers()[1].drawCard();
            game.getPlayers()[0].getHero().setHasAttacked(false);
            game.getPlayers()[1].getHero().setHasAttacked(false);
        }
    }

    /**
     * used to add the player's hero's details to the output
     * @param game the game manager
     */
    private void getPlayerHero(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        int idx = game.getCurrentAction().getPlayerIdx() - 1;
        ObjectNode on = game.getPlayers()[idx].getHero().print(game.getMapper());
        on.remove("attackDamage");
        node.put("output", on);
        game.getOutput().add(node);
    }

    /**
     * adds the current player's id to the output node
     * @param game the gama manager
     */
    private void getPlayerTurn(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getCurrentPlayer() + 1);
        game.getOutput().add(node);
    }

    /**
     * tries to place a card on the board
     * if the placeCard() method returns an error code,
     * then it will add the corresponding error to the output
     * @param game the game manager
     */
    private void placeCard(final Game game) {
        int idx = game.getCurrentAction().getHandIdx();
        int res = game.getBoard().placeCard(game.getPlayers()[game.getCurrentPlayer()], idx);
        if (res < 0) {
            ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
            node.put("handIdx", idx);
            if (res == Constants.NOT_ENOUGH_MANA_ERROR_CODE) {
                node.put("error", "Not enough mana to place card on table.");
            } else {
                node.put("error", "Cannot place card on table since row is full.");
            }
            game.getOutput().add(node);
        }
    }

    /**
     *  will add the player's hand in the output node, with the help
     *  of the Player::printHand() method
     * @param game the game manager
     */
    private void getCardsInHand(final Game game) {
        int idx = game.getCurrentAction().getPlayerIdx() - 1;
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getPlayers()[idx].printHand(game.getMapper()));
        game.getOutput().add(node);
    }

    /**
     * adds the details of the cards that are place on the board to the output
     * by calling the Board::printBoard() method
     * @param game the game manager
     */
    private void getCardsOnTable(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getBoard().printBoard(game.getMapper()));
        game.getOutput().add(node);
    }

    /**
     * adds a player's current mana to the output node
     * @param game the game manager
     */
    private void getPlayerMana(final Game game) {
        int idx = game.getCurrentAction().getPlayerIdx() - 1;
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getPlayers()[idx].getMana());
        game.getOutput().add(node);
    }

    /**
     * checks if the card was able to attack
     * if not, print the error message returned by
     * Board::useAttack()
     * @param game the game manager
     */
    private void cardUsesAttack(final Game game) {
        Coordinates attacker, attacked;
        attacker = game.getCurrentAction().getCardAttacker();
        attacked = game.getCurrentAction().getCardAttacked();
        String result = game.getBoard().useAttack(attacker, attacked);
        if (result != null) {
            ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
            // valueToTree() can be used here because the Coordinates class structure
            // wasn't modified
            node.put("cardAttacker", game.getMapper().valueToTree(attacker));
            node.put("cardAttacked", game.getMapper().valueToTree(attacked));
            node.put("error", result);
            game.getOutput().add(node);
        }
    }

    /**
     * calls Board:getCardAtPosition() and checks if a card was a returned
     * and print in JSON format, else print an error message
     * @param game the game manager
     */
    private void getCardAtPosition(final Game game) {
        int x = game.getCurrentAction().getX(), y = game.getCurrentAction().getY();
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("x", x);
        node.put("y", y);
        Minion card = game.getBoard().getCardAtPosition(x, y);
        if (card != null) {
            ObjectNode on = card.print(game.getMapper());
            node.put("output", on);
        } else {
            node.put("output", "No card available at that position.");
        }
        game.getOutput().add(node);
    }

    /**
     *  tries to use a card's ability, by calling Board:useAbility().
     *  if an error message is returned, add it to the output node
     * @param game the game manager
     */
    private void cardUsesAbility(final Game game) {
        Coordinates attacker, attacked;
        attacker = game.getCurrentAction().getCardAttacker();
        attacked = game.getCurrentAction().getCardAttacked();
        String result = game.getBoard().useAbility(attacker, attacked);
        if (result != null) {
            ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
            node.put("cardAttacker", game.getMapper().valueToTree(attacker));
            node.put("cardAttacked", game.getMapper().valueToTree(attacked));
            node.put("error", result);
            game.getOutput().add(node);
        }
    }

    /**
     *  tries to attack the hero by calling Board::useAttack()
     *  checks for a returned error message and prints it if one is found
     * @param game the game manager
     */
    private void useAttackHero(final Game game) {
        Coordinates attacker;
        attacker = game.getCurrentAction().getCardAttacker();
        String result = game.getBoard().useAttack(attacker,
                game.getPlayers()[(game.getCurrentPlayer() + 1) % 2].getHero());
        if (result != null) {
            ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
            node.put("cardAttacker", game.getMapper().valueToTree(attacker));
            node.put("error", result);
            game.getOutput().add(node);
        } else {
            if (game.getPlayers()[(game.getCurrentPlayer() + 1) % 2].getHero().getHealth() <= 0) {
                ObjectNode node;
                if (game.getCurrentPlayer() == 1) {
                    node = game.getMapper().createObjectNode();
                    node.put("gameEnded", "Player two killed the enemy hero.");
                    game.setP2wins(game.getP2wins() + 1);
                } else {
                    node = game.getMapper().createObjectNode();
                    node.put("gameEnded", "Player one killed the enemy hero.");
                    game.setP1wins(game.getP1wins() + 1);
                }
                game.getOutput().add(node);
            }
        }
    }

    /**
     *  will call Board::useHeroAbility() and will check for any error message
     * @param game the game manager
     */
    private void useHeroAbility(final Game game) {
        int row = game.getCurrentAction().getAffectedRow();
        String result = game.getBoard().useHeroAbility(row,
                        game.getPlayers()[game.getCurrentPlayer()]);
        if (result != null) {
            ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
            node.put("affectedRow", row);
            node.put("error", result);
            game.getOutput().add(node);
        }
    }

    /**
     *  prints all the frozen cards' details
     * @param game the game manager
     */
    private void getFrozenCardsOnTable(final Game game) {
        ObjectNode node = game.getMapper().createObjectNode();
        node.put("command", "getFrozenCardsOnTable");
        node.put("output", game.getBoard().getFrozenCards(game.getMapper()));
        game.getOutput().add(node);
    }

    /**
     * prints the number of games that have been played
     * @param game the game manager
     */
    private void getTotalGamesPlayed(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getGames());
        game.getOutput().add(node);
    }

    /**
     *  prints how many wins player 1 has
     * @param game the game manager
     */
    private void getPlayerOneWins(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getP1wins());
        game.getOutput().add(node);
    }

    /**
     * prints the number of games won by player 2
     * @param game the game manager
     */
    private void getPlayerTwoWins(final Game game) {
        ObjectNode node = printCommand(game.getCurrentAction(), game.getMapper());
        node.put("output", game.getP2wins());
        game.getOutput().add(node);
    }
}
