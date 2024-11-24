package org.poo.abilities;

import org.poo.gameplay.Row;
import org.poo.cards.Minion;

public final class EarthBorn extends HeroAbility {

    public EarthBorn() {
        super(false);
    }

    /**
     * increments the health of every minion for the target row
     * @param row the targeted row
     */
    public void useAbility(final Row row) {
        for (Minion m : row.getRow()) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}
