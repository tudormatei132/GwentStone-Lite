package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Coordinates;

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


    public String useAttack(Coordinates c1, Coordinates c2) {

        if (Constants.Player1Rows.contains(c1.getX()) == Constants.Player1Rows.contains(c2.getX())) {
            return "Attacked card does not belong to the enemy.";
        }
        Minion attacker = rows[c1.getX()].getRow().get(c1.getY());
        Minion attacked = rows[c2.getX()].getRow().get(c2.getY());

        if (attacker.isAbleToAttack() == 0) {
            if (!attacked.isTank()) {
                // if the attacked card is not a tank, then check
                // if there is one in the front row of the player
                // that got attacked
                if (Constants.Player1Rows.contains(c1.getX())) {
                    if (rows[1].existsTank())
                        return "Attacked card is not of type 'Tank’.";
                } else {
                    if (rows[2].existsTank())
                        return "Attacked card is not of type 'Tank’.";
                }
            }
            attacker.attack(attacked);
            rows[c2.getX()].removeCard(c2.getY());
            return null;
        }
        if (attacker.isAbleToAttack() == -1)
            return "Attacker card is frozen.";
        return "Attacker card has already attacked this turn.";

    }

    public String useAbility(Coordinates c1, Coordinates c2) {
        Minion caster = rows[c1.getX()].getRow().get(c1.getY());
        if (caster.isFrozen())
            return "Attacker card is frozen.";

        if (caster.getHasAttacked())
            return "Attacker card has already attacked.";

        boolean castOnEnemies = caster.getAbility().isMustBeCastOnEnemies();
        if (castOnEnemies & Constants.Player1Rows.contains(c1.getX()) ==
                Constants.Player1Rows.contains(c2.getX())) {
            return "Attacked card does not belong to the enemy.";
        }

        if (!castOnEnemies & Constants.Player1Rows.contains(c1.getX()) !=
            Constants.Player1Rows.contains(c2.getX())) {
            return "Attacked card does not belong to the current player.";
        }
        Minion target = rows[c2.getX()].getRow().get(c2.getY());
        if (castOnEnemies && !target.isTank()) {
            if (Constants.Player1Rows.contains(c1.getX())) {
                if (rows[1].existsTank())
                    return "Attacked card is not of type 'Tank’.";
            } else {
                if (rows[2].existsTank())
                    return "Attacked card is not of type 'Tank’.";
            }
        }


        caster.useAbility(target);
        rows[c2.getX()].removeCard(c2.getY());
        return null;
    }


    public void resetCards() {
        for (int i = 0; i < 4; i++)
            rows[i].resetCards();
    }

    public Minion getCardAtPosition(int x, int y) {
        if (rows[x].getRow().size() > y)
            return rows[x].getRow().get(y);
        return null;
    }

}
