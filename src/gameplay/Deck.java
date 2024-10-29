package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> deck;
    private int cards;
    public Deck() {
        deck = new ArrayList<>();
        cards = 0;
    }
    public Deck(ArrayList<Card> deck, int cards) {
        this.deck = deck;
        this.cards = cards;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public int getCards() {
        return cards;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void setCards(int cards) {
        this.cards = cards;
    }





    public ArrayNode printDeck(ObjectMapper mapper) {
        ArrayNode res = mapper.createArrayNode();

        for (Card c : deck) {
            ObjectNode on = c.print(mapper);
            res.add(on);
        }
        return res;
    }

}
