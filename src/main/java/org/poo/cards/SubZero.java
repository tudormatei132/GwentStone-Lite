package org.poo.cards;

import org.poo.gameplay.Row;

public final class SubZero extends HeroAbility {

    public SubZero() {
        super(true);
    }

    /**
     * sets isFrozen for every minion on the given row
     * @param row the target row
     */
    public void useAbility(final Row row) {
        for (Minion m : row.getRow()) {
            m.setFrozen(true);
        }
    }
}
