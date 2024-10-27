package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class Player {
    private int mana;
    private ArrayList<Card> currentDeck;
    private Random random;
    private int no;

    public Player() {
        this.mana = 0;
        this.currentDeck = new ArrayList();
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

    public Player(int mana) {
        this.mana = mana;
    }

    public int getMana() {
        return this.mana;
    }

    public ArrayList<Card> getCurrentDeck() {
        return this.currentDeck;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public void setCurrentDeck(ArrayList<Card> currentDeck, long seed) {
        Iterator var4 = currentDeck.iterator();

        while(var4.hasNext()) {
            Card c = (Card)var4.next();
            this.currentDeck.add(c);
        }

        this.random = new Random(seed);
        Collections.shuffle(this.currentDeck);
    }

    public void addMana(int round) {
        this.mana += round;
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
}