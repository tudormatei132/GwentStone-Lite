package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Board {
    private static Board instance = null;
    private Row[] rows;

    private Board() {
        rows = new Row[4];
        for(int i = 0; i < 4; i++) {
            this.rows[i] = new Row();
        }

    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }

    public void placeCard(Player p, Minion c) {
        if (p.hasEnoughMana(c)) {
            if (c.isFront()) {
                this.rows[p.getFrontRow()].placeCard(c);
            } else {
                this.rows[p.getBackRow()].placeCard(c);
            }
            p.addMana(-c.getMana());
            p.getHand().remove(c);
        }

    }

    public ArrayNode printBoard(ObjectMapper mapper) {
        ArrayNode res = mapper.createArrayNode();
        for(int i = 0; i < 4; i++) {
            res.add(rows[i].printRow(mapper));
        }
        return res;
    }

}
