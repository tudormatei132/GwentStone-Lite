package org.poo.cards;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class Deck {
    private ArrayList<Minion> deck;
    private int cards;

    public Deck() {
        deck = new ArrayList<>();
        cards = 0;
    }

    public Deck(final ArrayList<Minion> deck, final int cards) {
        this.deck = deck;
        this.cards = cards;
    }

    /**
     *
     * @param mapper
     * @return
     */
    public ArrayNode printDeck(final ObjectMapper mapper) {
        ArrayNode res = mapper.createArrayNode();

        for (Card c : deck) {
            ObjectNode on = c.print(mapper);
            res.add(on);
        }
        return res;
    }

}
