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
     *
     * @param card
     * @return
     */
    public boolean placeCard(final Minion card) {

        row.add(card);
        return true;
    }

    /**
     *
     * @param idx
     */
    public void removeCard(final int idx) {
        if (row.get(idx).getHealth() <= 0) {
            row.remove(idx);
        }
    }

    /**
     *
     * @param mapper
     * @return
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
     *
     */
    public void resetRow() {
        while (row.size() > 0) {
            row.remove(0);
        }
    }

    /**
     *
     */
    public void resetCards() {
        for (Minion m : row) {
            m.resetStatus();
        }
    }

    /**
     *
     * @return
     */
    public boolean existsTank() {
        for (Minion m : row) {
            if (m.isTank()) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param mapper
     * @return
     */
    public ArrayNode getFrozenCards(final ObjectMapper mapper) {
        ArrayNode rowNode = mapper.createArrayNode();
        for (Minion m : row) {
            if (m.isFrozen()) {
                rowNode.add(m.print(mapper));
            }
        }
        return rowNode;
    }

}
