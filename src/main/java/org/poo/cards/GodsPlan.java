package org.poo.cards;

public final class GodsPlan extends Ability {


    public GodsPlan(final Minion m) {
        super(false, "God's Plan", m);
    }

    /**
     *
     * @param target
     */
    public void useAbility(final Minion target) {
        target.reduceHealth(-2);
    }
}
