package org.poo.gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

import org.poo.cards.Minion;
import lombok.Getter;
import lombok.Setter;

public final class Row {
    @Getter
    @Setter
    private ArrayList<Minion> row;

    public Row() {
        row = new ArrayList<>();
    }



    /**
     * checks if the row is full
     * if not, place the card on the row
     * @param card the card that the players wants to place
     * @return true if the row wasn't full and false otherwise
     */
    public boolean placeCard(final Minion card) {
        if (row.size() >= Constants.MAX_CARDS_ON_ROW) {
            return false;
        }
        row.add(card);
        return true;
    }

    /**
     * removes the cards that have less than 1 health
     * @param idx the index of the attacked card
     */
    public void removeCard(final int idx) {
        if (row.get(idx).getHealth() <= 0) {
            row.remove(idx);
        }
    }

    /**
     * used to print all cards on the row
     * @param mapper the mapper used to write in the output node
     * @return a list of cards placed on the row
     */
    public ArrayNode printRow(final ObjectMapper mapper) {
        ArrayNode rowNode = mapper.createArrayNode();
        for (Minion m : row) {
            ObjectNode node = m.print(mapper);
            rowNode.add(node);
        }
        return rowNode;
    }

    /**
     * removes every card from the current row
     */
    public void resetRow() {
        while (row.size() > 0) {
            row.remove(0);
        }
    }

    /**
     * resets the status of every card on the row
     */
    public void resetCards() {
        for (Minion m : row) {
            m.resetStatus();
        }
    }

    /**
     * checks if there is any tank Minion on the row
     * @return true if there is a tank on the row, false otherwise
     */
    public boolean existsTank() {
        for (Minion m : row) {
            if (m.isTank()) {
                return true;
            }
        }
        return false;
    }
}
