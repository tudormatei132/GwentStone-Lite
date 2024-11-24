package org.poo.cards;


import org.poo.gameplay.Row;


public final class BloodThirst extends HeroAbility {
    public BloodThirst() {
        super(false);
    }

    /**
     * increments every minion's attack damage on a given row
     * @param row the targeted row
     */
    public void useAbility(final Row row) {
        for (Minion m : row.getRow()) {
            m.setAttackDamage(m.getAttackDamage() + 1);
        }
    }
}
