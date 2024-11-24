package org.poo.gameplay;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.DecksInput;
import org.poo.fileio.Input;
import org.poo.fileio.StartGameInput;

import java.util.Random;

import org.poo.cards.Hero;
import org.poo.cards.Deck;
import org.poo.cards.Minion;
import lombok.Getter;
import lombok.Setter;

public final class Game {
    private final Player[] players;
    private Board board;
    private final ArrayList<Deck> p1decks = new ArrayList<Deck>();
    private final ArrayList<Deck> p2decks = new ArrayList<Deck>();
    @Getter @Setter
    private int currentPlayer;
    @Getter @Setter
    private int startingPlayer;
    @Getter @Setter
    private int round;
    @Getter
    private ArrayNode output;
    @Getter
    private ActionsInput currentAction;
    @Getter
    private ObjectMapper mapper;
    @Getter @Setter
    private int p1wins, p2wins, games;
    public Game() {
        players = new Player[2];
    }

    public Game(final Board b, ArrayNode output) {
        players = new Player[2];
        board = b;
        this.output = output;
        mapper = new ObjectMapper();
        games = 0;
        p1wins = 0;
        p2wins = 0;
    }

    public ArrayList<Deck> getP2decks() {
        return p2decks;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<Deck> getP1decks() {
        return p1decks;
    }

    public Player[] getPlayers() {
        return players;
    }


    /**
     * creates a new minion based off a card input
     * @param c the CardInput
     * @return a new Minion with the stats from CardInput
     */
    public Minion inputToCard(final CardInput c) {
        if (c.getName().equals("Goliath") || c.getName().equals("Warden")) {
            return new Minion(c.getMana(), c.getHealth(), c.getAttackDamage(),
                    c.getDescription(), c.getName(), c.getColors(), true);
        }
        return new Minion(c.getMana(), c.getHealth(), c.getAttackDamage(),
                c.getDescription(), c.getName(), c.getColors(), false);

    }

    /**
     * will create the list of decks of one of the players from the input
     * @param playerDecks the list of decks of a player
     * @param deck the list of decks given in the input
     * @param nrCards number of cards in the deck
     */
    public void formNewDecks(final ArrayList<Deck> playerDecks,
                             final ArrayList<ArrayList<CardInput>> deck, final int nrCards) {

        for (ArrayList<CardInput> temp : deck) {
            ArrayList<Minion> cardsToBeAdded = new ArrayList<>();
            for (int i = 0; i < nrCards; i++) {
                cardsToBeAdded.add(inputToCard(temp.get(i)));
                if (Constants.FRONT_CARDS.contains(cardsToBeAdded.get(i).getName())) {
                    cardsToBeAdded.get(i).setFront(true);
                } else {
                    cardsToBeAdded.get(i).setAbility();
                }
            }
            Deck aux = new Deck(cardsToBeAdded, nrCards);
            playerDecks.add(aux);
        }

    }

    /**
     * will create the list of decks for both players, by calling
     * formNewDecks()
     * @param input the initial input, used to get the decks
     */
    public void extractDecks(final Input input) {
        DecksInput d1 = input.getPlayerOneDecks(), d2 = input.getPlayerTwoDecks();
        ArrayList<ArrayList<CardInput>> deck1 = d1.getDecks();
        ArrayList<ArrayList<CardInput>> deck2 = d2.getDecks();


        // get the players' decks
        formNewDecks(p1decks, deck1, d1.getNrCardsInDeck());
        formNewDecks(p2decks, deck2, d2.getNrCardsInDeck());

    }

    /**
     * sets the idx of the players, their current decks and Hero
     * @param start the startGameInput
     */
    public void setPlayers(final StartGameInput start) {
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



    /**
     * handles the operations that must be done at the start of every game
     * @param g the input for the game
     */
    public void startGame(final GameInput g) {
        games++;
        round = 1;
        board.resetBoard();
        StartGameInput start = g.getStartGame();
        setPlayers(start);
        currentPlayer = start.getStartingPlayer() - 1;
        startingPlayer = currentPlayer;
        players[0].drawCard();
        players[1].drawCard();
    }

    /**
     * will execute all the given commands
     * @param input the given commands
     * @param output the output node that will be checked
     */
    public void executeGames(final Input input, final ArrayNode output) {
        extractDecks(input);
        ArrayList<GameInput> game = input.getGames();
        for (GameInput g : game) {
            startGame(g);
            for (ActionsInput a : g.getActions()) {
                currentAction = a;
                CommandHandler.getInstance().executeCommand(a.getCommand(), this);
            }
        }

    }
}

