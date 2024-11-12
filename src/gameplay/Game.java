package gameplay;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.*;
import java.util.Random;

public class Game {
    private Player[] players;
    private Board board;
    private int currentPlayer, round;
    private ArrayList<Deck> p1decks = new ArrayList<Deck>(), p2decks = new ArrayList<Deck>();
    public Game() {
        players = new Player[2];
    }
    public Game(Board b) {
        players = new Player[2];
        board = b;
    }

    public Minion inputToCard(CardInput c) {
        if (c.getName().equals("Goliath") || c.getName().equals("Warden"))
            return new Minion(c.getMana(), c.getHealth(), c.getAttackDamage(),
                              c.getDescription(), c.getName(), c.getColors(), true);
        return new Minion(c.getMana(), c.getHealth(), c.getAttackDamage(),
                c.getDescription(), c.getName(), c.getColors(), false);

    }



    public void formNewDecks(ArrayList<Deck> playerDecks,
                            ArrayList<ArrayList<CardInput>> deck, int nrCards) {

        for (ArrayList<CardInput> temp : deck) {
            ArrayList<Minion> cardsToBeAdded = new ArrayList<>();
            for (int i = 0; i < nrCards; i++) {
                cardsToBeAdded.add(inputToCard(temp.get(i)));
                if (Constants.frontCards.contains(cardsToBeAdded.get(i).getName()))
                    cardsToBeAdded.get(i).setFront(true);
                else
                    cardsToBeAdded.get(i).setFront(false);
                cardsToBeAdded.get(i).setAbility();
            }
            Deck aux = new Deck(cardsToBeAdded, nrCards);
            playerDecks.add(aux);
        }

    }

    public void extractDecks(Input input) {
        DecksInput d1 = input.getPlayerOneDecks(), d2 = input.getPlayerTwoDecks();
        ArrayList<ArrayList<CardInput>> deck1 = d1.getDecks();
        ArrayList<ArrayList<CardInput>> deck2 = d2.getDecks();



        // get the players' decks
        formNewDecks(p1decks, deck1, d1.getNrCardsInDeck());
        formNewDecks(p2decks, deck2, d2.getNrCardsInDeck());

    }



    public void SetPlayers(StartGameInput start) {
        players[0] = new Player(1);
        players[1] = new Player(2);
        Random random = new Random(start.getShuffleSeed());
        players[0].setCurrentDeck(p1decks.get(start.getPlayerOneDeckIdx()), random);
        random = new Random(start.getShuffleSeed());
        players[1].setCurrentDeck(p2decks.get(start.getPlayerTwoDeckIdx()), random);
        players[0].setHero(new Hero(inputToCard(start.getPlayerOneHero())));
        players[1].setHero(new Hero(inputToCard(start.getPlayerTwoHero())));
        players[0].getHero().setHeroAbility();
        players[1].getHero().setHeroAbility();
    }




    public ObjectNode printCommand(ActionsInput a, ObjectMapper mapper) {
        ObjectNode on = mapper.createObjectNode();
        on.put("command", a.getCommand());
        if (a.getPlayerIdx() != 0)
            on.put("playerIdx", a.getPlayerIdx());
        return on;
    }

    public void startGame(Input input, ArrayNode output) {
        extractDecks(input);

        int games = 0;
        int p1wins = 0, p2wins = 0;
        ArrayList<GameInput> game = input.getGames();
        ObjectMapper mapper = new ObjectMapper();
        for (GameInput g : game) {
            games++;
            round = 1;
            board.resetBoard();
            StartGameInput start = g.getStartGame();
            SetPlayers(start);
            currentPlayer = start.getStartingPlayer() - 1;
            players[0].drawCard();
            players[1].drawCard();
            for (ActionsInput a : g.getActions()) {
                String command = a.getCommand();
                int idx = a.getPlayerIdx();
                ObjectNode node;
                switch (command) {
                    case "getPlayerDeck": {
                        node = printCommand(a, mapper);
                        node.put("output", players[idx - 1].getCurrentDeck().printDeck(mapper));

                        output.add(node);
                    }
                    break;

                    case "getPlayerHero": {
                        node = printCommand(a, mapper);
                        ObjectNode on = players[idx - 1].getHero().print(mapper);
                        on.remove("attackDamage");
                        node.put("output", on);
                        output.add(node);
                    }
                    break;

                    case "getPlayerTurn": {
                        node = printCommand(a, mapper);
                        node.put("output", currentPlayer + 1);

                        output.add(node);
                    }
                    break;

                    case "endPlayerTurn": {
                        board.resetCards(currentPlayer + 1);
                        currentPlayer = (currentPlayer + 1) % 2;
                        if (currentPlayer == start.getStartingPlayer() - 1) {
                            // when it's the starting player's turn again, a round has finished
                            round++;
                            players[0].addMana(round);
                            players[1].addMana(round);
                            players[0].drawCard();
                            players[1].drawCard();
                            players[0].getHero().setHasAttacked(false);
                            players[1].getHero().setHasAttacked(false);
                        }
                    }
                    break;

                    case "placeCard": {
                        idx = a.getHandIdx();
                        int res = board.placeCard(players[currentPlayer], idx);
                        if (res < 0) {

                            node = printCommand(a, mapper);
                            node.put("handIdx", idx);
                            if (res == -2)
                                node.put("error", "Not enough mana to place card on table.");
                            else {
                                node.put("error", "Cannot place card on table since row is full.");
                            }
                            output.add(node);
                        }

                    }
                    break;

                    case "getCardsInHand": {
                        node = printCommand(a, mapper);
                        node.put("output", players[idx - 1].printHand(mapper));
                        output.add(node);

                    }
                    break;

                    case "getCardsOnTable": {
                        node = printCommand(a, mapper);
                        node.put("output", board.printBoard(mapper));
                        output.add(node);
                    }
                    break;

                    case "getPlayerMana": {
                        node = printCommand(a, mapper);
                        node.put("output", players[idx - 1].getMana());
                        output.add(node);
                    }
                    break;

                    case "cardUsesAttack": {
                        Coordinates attacker, attacked;
                        attacker = a.getCardAttacker();
                        attacked = a.getCardAttacked();
                        String result = board.useAttack(attacker, attacked);
                        if (result != null) {
                            node = printCommand(a, mapper);
                            node.put("cardAttacker", mapper.valueToTree(attacker));
                            node.put("cardAttacked", mapper.valueToTree(attacked));
                            node.put("error", result);
                            output.add(node);
                        }
                    }
                    break;

                    case "getCardAtPosition": {
                        int x = a.getX(), y = a.getY();
                        node = printCommand(a, mapper);
                        node.put("x", x);
                        node.put("y", y);
                        Minion card = board.getCardAtPosition(x, y);
                        if (card != null) {
                            ObjectNode on = card.print(mapper);
                            node.put("output", on);
                        } else {
                            node.put("output", "No card available at that position.");
                        }
                        output.add(node);
                    }
                    break;

                    case "cardUsesAbility": {
                        Coordinates attacker, attacked;
                        attacker = a.getCardAttacker();
                        attacked = a.getCardAttacked();
                        String result = board.useAbility(attacker, attacked);
                        if (result != null) {
                            node = printCommand(a, mapper);
                            node.put("cardAttacker", mapper.valueToTree(attacker));
                            node.put("cardAttacked", mapper.valueToTree(attacked));
                            node.put("error", result);
                            output.add(node);
                        }
                    }
                    break;

                    case "useAttackHero": {
                        Coordinates attacker;
                        attacker = a.getCardAttacker();
                        String result = board.useAttack(attacker, players[(currentPlayer + 1) % 2].getHero());
                        if (result != null) {
                            node = printCommand(a, mapper);
                            node.put("cardAttacker", mapper.valueToTree(attacker));
                            node.put("error", result);
                            output.add(node);
                        } else {
                            if (players[(currentPlayer + 1) % 2].getHero().getHealth() <= 0) {
                                if (currentPlayer == 1) {
                                    node = mapper.createObjectNode();
                                    node.put("gameEnded", "Player two killed the enemy hero.");
                                    p2wins++;
                                } else {
                                    node = mapper.createObjectNode();
                                    node.put("gameEnded", "Player one killed the enemy hero.");
                                    p1wins++;
                                }
                                output.add(node);
                            }
                        }

                    }
                    break;

                    case "useHeroAbility": {
                        int row = a.getAffectedRow();
                        String result = board.useHeroAbility(row, players[currentPlayer]);
                        if (result != null) {
                            node = printCommand(a, mapper);
                            node.put("affectedRow", row);
                            node.put("error", result);
                            output.add(node);
                        }
                    }
                    break;

                    case "getFrozenCardsOnTable": {
                        node = mapper.createObjectNode();
                        node.put("command", "getFrozenCardsOnTable");
                        node.put("output", board.getFrozenCards(mapper));
                        output.add(node);
                    }
                    break;

                    case "getTotalGamesPlayed": {
                        node = printCommand(a, mapper);
                        node.put("output", games);
                        output.add(node);
                    }
                    break;

                    case "getPlayerOneWins": {
                        node = printCommand(a, mapper);
                        node.put("output", p1wins);
                        output.add(node);
                    }
                    break;

                    case "getPlayerTwoWins": {
                        node = printCommand(a, mapper);
                        node.put("output", p2wins);
                        output.add(node);
                    }
                    break;
                }

            }
        }

    }

}
