package org.poo.cards;

import org.poo.gameplay.Row;

public final class EarthBorn extends HeroAbility {

    public EarthBorn() {
        super(false);
    }

    /**
     *
     * @param row
     */
    public void useAbility(final Row row) {
        for (Minion m : row.getRow()) {
            m.setHealth(m.getHealth() + 1);
        }
    }
}
