package org.poo.cards;

import org.poo.gameplay.Row;

public final class SubZero extends HeroAbility {

    public SubZero() {
        super(true);
    }

    /**
     *
     * @param row
     */
    public void useAbility(final Row row) {
        for (Minion m : row.getRow()) {
            m.setFrozen(true);
        }
    }
}
