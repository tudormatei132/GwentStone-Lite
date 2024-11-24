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
     * used to implement the Singleton pattern
     * @return the Board instance
     */
    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }



    /**
     * tries to place a card on the board
     * @param p the player that tries to place the card
     * @param idx the hand index of the card
     * @return -2 if the players doesn't have enough mana
     * -1 if the row is full or 0 if operation was successful
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
     * will be used to print every card, from every row, to the output node
     * @param mapper the mapper from Game
     * @return an ArrayNode which has a list for every row with each card's details
     */
    public ArrayNode printBoard(final ObjectMapper mapper) {
        ArrayNode res = mapper.createArrayNode();
        for (int i = 0; i < Constants.ROWS_NO; i++) {
            res.add(rows[i].printRow(mapper));
        }
        return res;
    }

    /**
     * resets the board at the end of a game
     */
    public void resetBoard() {
        for (int i = 0; i < Constants.ROWS_NO; i++) {
            rows[i].resetRow();
        }
    }

    /**
     * checks if an attack can be made and performs it
     * if the requirements are met
     * @param c1 coordinates of the attacker
     * @param c2 coordinates of the attacked card
     * @return the error message or null if the attack happened
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
     * checks if an ability can be used and uses it
     * if the requirements are met
     * @param c1 coordinates of the attacker
     * @param c2 coordinates of the attacked card
     * @return the error message or null if the ability was cast
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
     * reset the state of every card of the player
     * @param player the player whose turn just ended
     */
    public void resetCards(final int player) {
        for (int i = (player + 1) % 3; i <= (player + 1) % 3 + 1; i++) {
            rows[i].resetCards();
        }
    }

    /**
     * gets the card at a given position
     * @param x the row number
     * @param y the index on the row
     * @return the Minion at that position if it was a valid one
     */
    public Minion getCardAtPosition(final int x, final int y) {
        if (rows[x].getRow().size() > y) {
            return rows[x].getRow().get(y);
        }
        return null;
    }

    /**
     * a card will try to perform an attack on the enemy hero
     * @param c1 the coordinates of the attacker card
     * @param hero the attacked Hero
     * @return an error message as a String if the attack couldn't be performed
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
     * will try to use a hero's ability
     * @param row the targeted row
     * @param p the player that tries to cast the hero ability
     * @return an error message if there's one
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
     * will check every row for frozen cards and will be used to
     * add them to the output node
     * @param mapper the mapper from the game class
     * @return an ArrayNode that is made of lists of the details of frozen cards from every row
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
