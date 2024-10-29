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
    public Game(Player p1, Player p2, Board b) {
        players = new Player[] {p1, p2};
        board = b;
    }

    public Card inputToCard(CardInput c) {
        return new Card(c.getMana(), c.getHealth(), c.getAttackDamage(),
                c.getDescription(), c.getName(), c.getColors());

    }



    public void formNewDecks(ArrayList<Deck> playerDecks,
                            ArrayList<ArrayList<CardInput>> deck, int nrCards) {

        for (ArrayList<CardInput> temp : deck) {
            ArrayList<Card> cardsToBeAdded = new ArrayList<Card>();
            for (int i = 0; i < nrCards; i++) {
                cardsToBeAdded.add(inputToCard(temp.get(i)));
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
        round = 1;
        ArrayList<GameInput> game = input.getGames();
        ObjectMapper mapper = new ObjectMapper();
        for (GameInput g : game) {
            StartGameInput start = g.getStartGame();
            SetPlayers(start);
            currentPlayer = start.getStartingPlayer();
            players[0].drawCard(); //TODO: Change it so it happens every round
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
                        node.put("output", currentPlayer);

                        output.add(node);
                    }
                    break;

                    case "endPlayerTurn": {
                        currentPlayer = (currentPlayer + 1) % 2;
                        if (currentPlayer == start.getStartingPlayer()) {
                            // when it's the starting player's turn again, a round has finished
                            round++;
                            players[0].addMana(round);
                            players[1].addMana(round);
                            players[0].drawCard();
                            players[1].drawCard();
                        }
                    }
                    break;

                    case "placeCard": {

                    }
                    break;
                }

            }
        }

    }

}
