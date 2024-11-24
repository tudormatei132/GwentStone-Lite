package org.poo.abilities;

import org.poo.cards.Minion;
import org.poo.gameplay.Constants;

public final class GodsPlan extends Ability {


    public GodsPlan() {
        super(false, "God's Plan");
    }

    /**
     * increases the minion's health by 2
     * @param target the target Minion
     */
    public void useAbility(final Minion target) {
        target.reduceHealth(Constants.GODS_PLAN_HEAL);
    }
}
