package org.poo.cards;


import org.poo.gameplay.Row;

public abstract class HeroAbility {
    private boolean mustCastOnEnemies;

    /**
     *
     * @param row
     */
    public abstract void useAbility(Row row);

    /**
     *
     * @return
     */
    public boolean isMustCastOnEnemies() {
        return mustCastOnEnemies;
    }

    public HeroAbility(final boolean mustCastOnEnemies) {
        this.mustCastOnEnemies = mustCastOnEnemies;
    }
}
