package org.poo.abilities;


import lombok.Getter;
import org.poo.gameplay.Row;

public abstract class HeroAbility {
    @Getter
    private boolean mustCastOnEnemies;

    /**
     * will be implemented by every ability class
     * and will have an impact on the stats of the cards
     * on the given row
     * @param row the targeted row
     */
    public abstract void useAbility(Row row);

    public HeroAbility(final boolean mustCastOnEnemies) {
        this.mustCastOnEnemies = mustCastOnEnemies;
    }
}
