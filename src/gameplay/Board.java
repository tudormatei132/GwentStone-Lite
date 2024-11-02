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

    public Row[] getRows() {
        return rows;
    }

    public int placeCard(Player p, int idx) {

        if (!p.hasEnoughMana(p.getCard(idx)))
            return -2;

        if (!rows[p.getRow(p.getCard(idx))].placeCard(p.getCard(idx)))
            return -1;

        p.addMana((-1) * p.getCard(idx).getMana());
        p.getHand().remove(p.getCard(idx));
        return 0; //success
    }

    public ArrayNode printBoard(ObjectMapper mapper) {
        ArrayNode res = mapper.createArrayNode();
        for(int i = 0; i < 4; i++) {
            res.add(rows[i].printRow(mapper));
        }
        return res;
    }

    public void resetBoard() {
        for(int i = 0; i < 4; i++) {
            rows[i].resetRow();
        }
    }

}
