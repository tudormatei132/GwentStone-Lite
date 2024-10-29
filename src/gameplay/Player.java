package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Player {
    private int mana;
    private Deck currentDeck;
    private ArrayList<Card> hand;
    private Hero hero;
    private int no;

    public Player(int no) {
        this.mana = 0;
        this.currentDeck = new Deck();
        this.hand = new ArrayList<Card>();
        this.no = no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }

    public int getFrontRow() {
        return this.no % 2 + 1;
    }

    public int getBackRow() {
        return (this.no + 2) % 4;
    }



    public int getMana() {
        return this.mana;
    }

    public Deck getCurrentDeck() {
        return this.currentDeck;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setCurrentDeck(Deck currentDeck, Random random) {


        for (Card c : currentDeck.getDeck()) {
            this.currentDeck.getDeck().add(c);
        }


        Collections.shuffle(this.currentDeck.getDeck(), random);
    }

    public void addMana(int round) {
        this.mana += round;
        if (this.mana > 10)
            this.mana = 10;
    }

    public boolean hasEnoughMana(Card card) {
        if (this.mana > card.getMana()) {
            this.mana -= card.getMana();
            return true;
        } else {
            System.out.println("Not enough mana");
            return false;
        }
    }
    public void drawCard() {
        if (!currentDeck.getDeck().isEmpty()) {}
            hand.add(currentDeck.getDeck().remove(0));
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Hero getHero() {
        return hero;
    }



    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }


    public ObjectNode deckToOutput(ObjectMapper mapper) {
        ObjectNode on = mapper.createObjectNode();
        ArrayNode an = mapper.createArrayNode();
        an = currentDeck.printDeck(mapper);
        on.put("output",  an);
        return on;

    }
}