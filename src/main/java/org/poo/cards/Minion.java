package org.poo.cards;

import lombok.Getter;
import lombok.Setter;

import org.poo.abilities.Ability;
import org.poo.abilities.WeakKnees;
import org.poo.abilities.Shapeshift;
import org.poo.abilities.Skyjack;
import org.poo.abilities.GodsPlan;

import java.util.ArrayList;

public final class Minion extends Card {
    @Getter @Setter
    private boolean isTank;

    @Getter
    private Ability ability = null;

    public Minion(final int mana, final int health, final int attackDamage,
                  final String description, final String name, final ArrayList<String> colors,
                  final boolean isTank) {
        super(mana, health, attackDamage, description, name, colors);
        this.isTank = isTank;
    }


    /**
     * creates a minion copy of a card
     * was used to not alter the decks
     * @return a copy of the card
     */
    public Minion copyCard() {
        Minion copy = new Minion(getMana(), getHealth(), getAttackDamage(), getDescription(),
                                 getName(), getColors(), isTank);
        copy.setFront(this.isFront());
        copy.setAbility();
        return copy;
    }

    /**
     * checks if the minion should have any ability based on its name
     * and sets it if that's the case
     */
    public void setAbility() {
        if (getName().equals("The Ripper")) {
            ability = new WeakKnees();
            return;
        }

        if (getName().equals("Miraj")) {
            ability = new Skyjack(this);
            return;
        }

        if (getName().equals("The Cursed One")) {
            ability = new Shapeshift();
            return;
        }

        if (getName().equals("Disciple")) {
            ability = new GodsPlan();
        }
    }


    /**
     *
     * @return -1 if the card's frozen, 1 if already attacked, or 0 if able to attack
     * return value is used for printing the correct error message
     */
    public int isAbleToAttack() {
        if (isFrozen()) {
            return -1;
        }
        if (getHasAttacked()) {
            return 1;
        }
        return 0; // can attack
    }



    /**
     * the current minion attacks another card
     * @param c the attacked card
     */
    public void attack(final Card c) {
        c.reduceHealth(this.getAttackDamage());
        setHasAttacked(true);
    }

    /**
     * resets the state of a minion
     */
    public void resetStatus() {
        setFrozen(false);
        setHasAttacked(false);
    }

    /**
     * the current minion uses its ability (if it has one)
     * @param m the target minion
     */
    public void useAbility(final Minion m) {
        if (ability != null) {
            ability.useAbility(m);
            setHasAttacked(true);
        }
    }

}
