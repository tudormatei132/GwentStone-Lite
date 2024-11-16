package org.poo.cards;

import lombok.Getter;
import lombok.Setter;

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

    public boolean mustBeAttacked() {
        return false;
    }

    /**
     *
     * @return
     */
    public Minion copyCard() {
        Minion copy = new Minion(getMana(), getHealth(), getAttackDamage(), getDescription(),
                                 getName(), getColors(), isTank);
        copy.setFront(this.isFront());
        copy.setAbility();
        return copy;
    }

    /**
     *
     */
    public void setAbility() {
        if (getName().equals("The Ripper")) {
            ability = new WeakKnees(this);
            return;
        }

        if (getName().equals("Miraj")) {
            ability = new Skyjack(this);
            return;
        }

        if (getName().equals("The Cursed One")) {
            ability = new Shapeshift(this);
            return;
        }

        if (getName().equals("Disciple")) {
            ability = new GodsPlan(this);
        }
    }


    /**
     *
     * @return
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
     *
     * @param c
     */
    public void attack(final Card c) {
        c.reduceHealth(this.getAttackDamage());
        setHasAttacked(true);
    }

    /**
     *
     */
    public void resetStatus() {
        setFrozen(false);
        setHasAttacked(false);
    }

    /**
     *
     * @param m
     */
    public void useAbility(final Minion m) {
        ability.useAbility(m);
        setHasAttacked(true);
    }

}
