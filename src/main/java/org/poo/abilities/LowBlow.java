package org.poo.abilities;

import org.poo.gameplay.Row;
import org.poo.cards.Minion;

public class LowBlow extends HeroAbility {

    public LowBlow() {
        super(true);
    }

    /**
     * removes the card with most health from a given row
     * @param row the target row
     */
    @Override
    public void useAbility(final Row row) {
        int max = -1, idx = 0, i = 0;
        for (Minion m : row.getRow()) {
            if (m.getHealth() > max) {
                max = m.getHealth();
                idx = i;
            }
            i++;
        }
        if (max != -1) {
            row.getRow().remove(idx);
        }
    }
}
