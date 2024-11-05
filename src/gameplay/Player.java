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
    private ArrayList<Minion> hand;
    private Hero hero;
    private int no;

    public Player(int no) {
        this.mana = 1;
        this.currentDeck = new Deck();
        this.hand = new ArrayList<>();
        this.no = no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getNo() {
        return this.no;
    }



    public int getRow(Minion minion) {
        if (minion.isFront())
            return ((no % 2 + 1));
        else
            return ((no + 2) % 4);
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


        for (Minion c : currentDeck.getDeck()) {
            Minion copy = c.copyCard();
            this.currentDeck.getDeck().add(copy);
        }


        Collections.shuffle(this.currentDeck.getDeck(), random);
    }

    public void addMana(int round) {
        this.mana += Math.min(round, 10);
    }

    public boolean hasEnoughMana(Card card) {
        if (this.mana >= card.getMana())
            return true;
        return false;
    }
    public void drawCard() {
        if (currentDeck.getDeck().size() > 0) {
            hand.add(currentDeck.getDeck().remove(0));
        }
    }

    public ArrayList<Minion> getHand() {
        return hand;
    }

    public Hero getHero() {
        return hero;
    }



    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setHand(ArrayList<Minion> hand) {
        this.hand = hand;
    }




    public ArrayNode printHand(ObjectMapper mapper) {
        ArrayNode handNode = mapper.createArrayNode();
        for (Minion c : hand) {
            ObjectNode node = c.print(mapper);
            handNode.add(node);
        }
        return handNode;
    }

    public Minion getCard(int idx) {
        if (hand.size() > 0)
            return hand.get(idx);
        return null;
    }

    public void useHeroAbility(Row row) {
        hero.getAbility().useAbility(row);
        this.mana -= hero.getMana();
        hero.setHasAttacked(true);
    }


}