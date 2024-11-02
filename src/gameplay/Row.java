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

    public void placeCard(Minion card) {
        if (this.row.size() < max_length) {
            this.row.add(card);
        } else {
            System.out.println("Row is full");
        }

    }

    public void removeCard(int idx) {
        this.row.remove(idx);
    }

    public ArrayNode printRow(ObjectMapper mapper) {
        ArrayNode rowNode = mapper.createArrayNode();
        for (Minion m : row) {
            ObjectNode node = m.print(mapper);
            rowNode.add(node);
        }
        return rowNode;
    }
}
