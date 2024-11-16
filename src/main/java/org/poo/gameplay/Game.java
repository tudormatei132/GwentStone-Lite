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
     *
     * @param c
     * @return
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
     *
     * @param playerDecks
     * @param deck
     * @param nrCards
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
     *
     * @param input
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
     *
     * @param start
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
     *
     * @param a
     * @param mapper
     * @return
     */
    public ObjectNode printCommand(final ActionsInput a, final ObjectMapper mapper) {
        ObjectNode on = mapper.createObjectNode();
        on.put("command", a.getCommand());
        if (a.getPlayerIdx() != 0) {
            on.put("playerIdx", a.getPlayerIdx());
        }
        return on;
    }

    /**
     *
     * @param input
     * @param output
     */
    public void startGame(final Input input, final ArrayNode output) {
        extractDecks(input);
        ArrayList<GameInput> game = input.getGames();
        for (GameInput g : game) {
            games++;
            round = 1;
            board.resetBoard();
            StartGameInput start = g.getStartGame();
            setPlayers(start);
            currentPlayer = start.getStartingPlayer() - 1;
            startingPlayer = currentPlayer;
            players[0].drawCard();
            players[1].drawCard();
            for (ActionsInput a : g.getActions()) {
                currentAction = a;
                CommandHandler.getInstance().executeCommand(a.getCommand(), this);
            }
        }

    }

}
