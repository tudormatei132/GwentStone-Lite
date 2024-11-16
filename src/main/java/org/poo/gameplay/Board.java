package org.poo.gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.Coordinates;

import org.poo.cards.Minion;
import org.poo.cards.Hero;
import lombok.Getter;
import lombok.Setter;

public final class Board {
    private static Board instance = null;
    @Getter
    @Setter
    private Row[] rows;


    private Board() {
        rows = new Row[4];
        for (int i = 0; i < Constants.ROWS_NO; i++) {
            this.rows[i] = new Row();
        }

    }

    /**
     *
     * @return
     */
    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }



    /**
     *
     * @param p
     * @param idx
     * @return
     */
    public int placeCard(Player p, final int idx) {

        if (!p.hasEnoughMana(p.getCard(idx))) {
            return -2;
        }

        if (!rows[p.getRow(p.getCard(idx))].placeCard(p.getCard(idx))) {
            return -1;
        }

        p.addMana((-1) * p.getCard(idx).getMana());
        p.getHand().remove(p.getCard(idx));
        return 0; //success
    }

    /**
     *
     * @param mapper
     * @return
     */
    public ArrayNode printBoard(final ObjectMapper mapper) {
        ArrayNode res = mapper.createArrayNode();
        for (int i = 0; i < Constants.ROWS_NO; i++) {
            res.add(rows[i].printRow(mapper));
        }
        return res;
    }

    /**
     *
     */
    public void resetBoard() {
        for (int i = 0; i < Constants.ROWS_NO; i++) {
            rows[i].resetRow();
        }
    }

    /**
     *
     * @param c1
     * @param c2
     * @return
     */
    public String useAttack(final Coordinates c1, final Coordinates c2) {

        if (Constants.PLAYER_1_ROWS.contains(c1.getX())
                == Constants.PLAYER_1_ROWS.contains(c2.getX())) {
            return "Attacked card does not belong to the enemy.";
        }
        Minion attacker = rows[c1.getX()].getRow().get(c1.getY());

        Minion attacked = rows[c2.getX()].getRow().get(c2.getY());

        if (attacker.isAbleToAttack() == 0) {
            if (!attacked.isTank()) {
                // if the attacked card is not a tank, then check
                // if there is one in the front row of the player
                // that got attacked
                if (Constants.PLAYER_1_ROWS.contains(c1.getX())) {
                    if (rows[1].existsTank()) {
                        return "Attacked card is not of type 'Tank'.";
                    }
                } else {
                    if (rows[2].existsTank()) {
                        return "Attacked card is not of type 'Tank'.";
                    }
                }
            }
            attacker.attack(attacked);
            rows[c2.getX()].removeCard(c2.getY());
            return null;
        }
        if (attacker.isAbleToAttack() == -1) {
            return "Attacker card is frozen.";
        }
        return "Attacker card has already attacked this turn.";

    }

    /**
     *
     * @param c1
     * @param c2
     * @return
     */
    public String useAbility(final Coordinates c1, final Coordinates c2) {
        Minion caster = rows[c1.getX()].getRow().get(c1.getY());
        if (caster.isFrozen()) {
            return "Attacker card is frozen.";
        }

        if (caster.getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }

        boolean castOnEnemies = caster.getAbility().castOnEnemies();
        if (castOnEnemies & Constants.PLAYER_1_ROWS.contains(c1.getX())
                == Constants.PLAYER_1_ROWS.contains(c2.getX())) {
            return "Attacked card does not belong to the enemy.";
        }

        if (!castOnEnemies & Constants.PLAYER_1_ROWS.contains(c1.getX())
                != Constants.PLAYER_1_ROWS.contains(c2.getX())) {
            return "Attacked card does not belong to the current player.";
        }
        Minion target = rows[c2.getX()].getRow().get(c2.getY());
        if (castOnEnemies && !target.isTank()) {
            if (Constants.PLAYER_1_ROWS.contains(c1.getX())) {
                if (rows[1].existsTank()) {
                    return "Attacked card is not of type 'Tank'.";
                }
            } else {
                if (rows[2].existsTank()) {
                    return "Attacked card is not of type 'Tank'.";
                }
            }
        }


        caster.useAbility(target);
        rows[c2.getX()].removeCard(c2.getY());
        return null;
    }

    /**
     *
     * @param player
     */
    public void resetCards(final int player) {
        for (int i = (player + 1) % 3; i <= (player + 1) % 3 + 1; i++) {
            rows[i].resetCards();
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public Minion getCardAtPosition(final int x, final int y) {
        if (rows[x].getRow().size() > y) {
            return rows[x].getRow().get(y);
        }
        return null;
    }

    /**
     *
     * @param c1
     * @param hero
     * @return
     */
    public String useAttack(final Coordinates c1, final Hero hero) {

        Minion attacker = rows[c1.getX()].getRow().get(c1.getY());

        if (attacker.isAbleToAttack() == 0) {

            // if the attacked card is not a tank, then check
            // if there is one in the front row of the player
            // that got attacked
            if (Constants.PLAYER_1_ROWS.contains(c1.getX())) {
                if (rows[1].existsTank()) {
                    return "Attacked card is not of type 'Tank'.";
                }
            } else {
                if (rows[2].existsTank()) {
                    return "Attacked card is not of type 'Tank'.";
                }
            }

            attacker.attack(hero);

            return null;
        }
        if (attacker.isAbleToAttack() == -1) {
            return "Attacker card is frozen.";
        }
        return "Attacker card has already attacked this turn.";

    }

    /**
     *
     * @param row
     * @param p
     * @return
     */
    public String useHeroAbility(final int row, final Player p) {

        if (p.getMana() < p.getHero().getMana()) {
            return "Not enough mana to use hero's ability.";
        }

        if (p.getHero().getHasAttacked()) {
            return "Hero has already attacked this turn.";
        }

        if (Constants.PLAYER_1_ROWS.contains(row) == (p.getNo() == 1)) {
            if (p.getHero().getAbility().isMustCastOnEnemies()) {
                return "Selected row does not belong to the enemy.";
            }

        }

        if (Constants.PLAYER_1_ROWS.contains(row) != (p.getNo() == 1)) {
            if (!p.getHero().getAbility().isMustCastOnEnemies()) {
                return "Selected row does not belong to the current player.";
            }
        }
        p.useHeroAbility(rows[row]);

        return null;
    }

    /**
     *
     * @param mapper
     * @return
     */
    public ArrayNode getFrozenCards(final ObjectMapper mapper) {
        ArrayNode node = mapper.createArrayNode();
        for (int i = 0; i < Constants.ROWS_NO; i++) {
            for (Minion m : rows[i].getRow()) {
                if (m.isFrozen()) {
                    node.add(m.print(mapper));
                }
            }

        }
        return node;
    }
}
