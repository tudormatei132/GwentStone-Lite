package gameplay;

public class Board {
    private static Board instance = null;
    private Row[] rows = new Row[4];

    private Board() {
        for(int i = 0; i < 4; ++i) {
            this.rows[i] = new Row();
        }

    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }

    public void PlaceCard(Player p, Card c) {
        if (p.hasEnoughMana(c)) {
            if (c.isFront()) {
                this.rows[p.getFrontRow()].placeCard(c);
            } else {
                this.rows[p.getBackRow()].placeCard(c);
            }
        }

    }
}
