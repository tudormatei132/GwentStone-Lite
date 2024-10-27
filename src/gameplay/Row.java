package gameplay;

import java.util.ArrayList;

public class Row {
    private ArrayList<Card> row = new ArrayList();
    private final int max_length = 5;

    public Row() {
    }

    public void placeCard(Card card) {
        if (this.row.size() < 5) {
            this.row.add(card);
        } else {
            System.out.println("Row is full");
        }

    }

    public void removeCard(int idx) {
        this.row.remove(idx);
    }
}
