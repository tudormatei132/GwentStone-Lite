package org.poo.gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.poo.cards.Deck;
import org.poo.cards.Hero;
import org.poo.cards.Minion;
import org.poo.cards.Card;
import lombok.Getter;
import lombok.Setter;
// player class is used to store the information about the player
// for one game. The stats and the list of decks will be stored by the 'Game' class.
public final class Player {
    @Getter
    @Setter
    private int mana;
    @Getter
    @Setter
    private Deck currentDeck;
    @Getter
    @Setter
    private ArrayList<Minion> hand;
    @Getter
    @Setter
    private Hero hero;
    @Getter
    @Setter
    private int no;

    public Player(final int no) {
        this.mana = 1;
        this.currentDeck = new Deck();
        this.hand = new ArrayList<>();
        this.no = no;
    }



    public int getRow(final Minion minion) {
        if (minion.isFront()) {
            return ((no % 2 + 1));
        } else {
            return ((no + 2) % 4);
        }
    }

    /**
     *
     * @param currentDeck
     * @param random
     */
    public void setCurrentDeck(final Deck currentDeck, final Random random) {


        for (Minion c : currentDeck.getDeck()) {
            Minion copy = c.copyCard();
            this.currentDeck.getDeck().add(copy);
        }


        Collections.shuffle(this.currentDeck.getDeck(), random);
    }

    /**
     * used to add or deduct mana
     * @param amount
     */
    public void addMana(final int amount) {
        this.mana += Math.min(amount, 10);
    }

    /**
     *
     * @param card
     * @return
     */
    public boolean hasEnoughMana(final Card card) {
        if (this.mana >= card.getMana()) {
            return true;
        }
        return false;
    }

    /**
     *
     */
    public void drawCard() {
        if (currentDeck.getDeck().size() > 0) {
            hand.add(currentDeck.getDeck().remove(0));
        }
    }



    /**
     *
     * @param mapper
     * @return
     */
    public ArrayNode printHand(final ObjectMapper mapper) {
        ArrayNode handNode = mapper.createArrayNode();
        for (Minion c : hand) {
            ObjectNode node = c.print(mapper);
            handNode.add(node);
        }
        return handNode;
    }

    /**
     *
     * @param idx
     * @return
     */
    public Minion getCard(final int idx) {
        if (hand.size() >= 0) {
            return hand.get(idx);
        }
        return null;
    }

    /**
     *
     * @param row
     */
    public void useHeroAbility(final Row row) {
        hero.getAbility().useAbility(row);
        this.mana -= hero.getMana();
        hero.setHasAttacked(true);
    }
}
