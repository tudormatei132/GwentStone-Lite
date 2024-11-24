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


    /**
     * gives the row the minion will be placed on based on
     * player id and minion.front
     * @param minion the minion that needs to be placed
     * @return the row that will be used to place the minion on
     */
    public int getRow(final Minion minion) {
        if (minion.isFront()) {
            return ((no % 2 + 1));
        } else {
            return ((no + 2) % Constants.ROWS_NO);
        }
    }

    /**
     * will copy the given deck so it won't be modified after a game in which it was used
     * @param deck the deck that will be copied
     * @param random the shuffle seed
     */
    public void setCurrentDeck(final Deck deck, final Random random) {


        for (Minion c : deck.getDeck()) {
            Minion copy = c.copyCard();
            this.currentDeck.getDeck().add(copy);
        }


        Collections.shuffle(this.currentDeck.getDeck(), random);
    }

    /**
     * used to add or deduct mana
     * @param amount the amount of mana we want to add
     */
    public void addMana(final int amount) {
        this.mana += Math.min(amount, Constants.MAX_MANA_GAIN);
    }

    /**
     * checks if the player has enough mana
     * @param card the card that the player wants to be placed
     * @return true if the player has enough mana, false otherwise
     */
    public boolean hasEnoughMana(final Card card) {
        if (this.mana >= card.getMana()) {
            return true;
        }
        return false;
    }

    /**
     * draws a card for the deck if it's not empty
     */
    public void drawCard() {
        if (currentDeck.getDeck().size() > 0) {
            hand.add(currentDeck.getDeck().remove(0));
        }
    }



    /**
     * used to add the cards in the hand of the player to the output
     * @param mapper the mapper from the game class
     * @return an array node made of a list of cards in JSON format
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
     * returns the card at the given index in the player's hand
     * @param idx the hand index of the card
     * @return the idx-th card in the hand of the player
     */
    public Minion getCard(final int idx) {
        if (hand.size() >= 0) {
            return hand.get(idx);
        }
        return null;
    }

    /**
     * uses the HeroAbility and also deducts mana
     * @param row the target row
     */
    public void useHeroAbility(final Row row) {
        hero.getAbility().useAbility(row);
        this.mana -= hero.getMana();
        hero.setHasAttacked(true);
    }
}
