package org.poo.abilities;

import org.poo.gameplay.Row;
import org.poo.cards.Minion;

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
