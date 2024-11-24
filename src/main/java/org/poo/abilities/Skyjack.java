package org.poo.abilities;

import org.poo.cards.Minion;

public final class Skyjack extends Ability {


    public Skyjack(final Minion m) {
        super(true, "Skyjack", m);
    }

    /**
     * swaps health between the minion that cast the ability and the targeted one
     * @param target teh target minion
     */
    public void useAbility(final Minion target) {
        int temp = this.getMinion().getHealth();
        this.getMinion().setHealth(target.getHealth());
        target.setHealth(temp);
    }
}
