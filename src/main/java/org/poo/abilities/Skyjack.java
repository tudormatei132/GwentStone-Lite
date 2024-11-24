package org.poo.abilities;

import org.poo.cards.Minion;

public final class Skyjack extends Ability {

    private Minion minion;

    public Skyjack(final Minion m) {
        super(true, "Skyjack");
        minion = m;
    }

    /**
     * swaps health between the minion that cast the ability and the targeted one
     * @param target teh target minion
     */
    public void useAbility(final Minion target) {
        int temp = minion.getHealth();
        this.minion.setHealth(target.getHealth());
        target.setHealth(temp);
    }
}
