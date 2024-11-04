package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Row {
    private ArrayList<Minion> row;
    private final int max_length = 5;

    public Row() {
        row = new ArrayList<>();
    }

    public ArrayList<Minion> getRow() {
        return row;
    }

    public void setRow(ArrayList<Minion> row) {
        this.row = row;
    }

    public boolean placeCard(Minion card) {

        row.add(card);
        return true;
    }

    public void removeCard(int idx) {
        if (row.get(idx).getHealth() <= 0) {
            row.remove(idx);
        }
    }

    public ArrayNode printRow(ObjectMapper mapper) {
        ArrayNode rowNode = mapper.createArrayNode();
        for (Minion m : row) {
            ObjectNode node = m.print(mapper);
            rowNode.add(node);
        }
        return rowNode;
    }

    public void resetRow() {
        while (row.size() > 0) {
            row.remove(0);
        }
    }

    public void resetCards() {
        for (Minion m : row)
            m.resetStatus();
    }

    public boolean existsTank() {
        for (Minion m : row)
            if (m.isTank())
                return true;
        return false;
    }

}
