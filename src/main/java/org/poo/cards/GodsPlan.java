package org.poo.cards;

public final class GodsPlan extends Ability {


    public GodsPlan(final Minion m) {
        super(false, "God's Plan", m);
    }

    /**
     * increases the minion's health by 2
     * @param target the target Minion
     */
    public void useAbility(final Minion target) {
        target.reduceHealth(-2);
    }
}
