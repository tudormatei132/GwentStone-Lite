package org.poo.cards;

import lombok.Getter;
import lombok.Setter;

public abstract class Ability {
    @Getter
    private boolean mustBeCastOnEnemies;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Minion minion;

    /**
     *
     * @param target
     */
    public abstract void useAbility(Minion target);

    public Ability(final boolean mustBeCastOnEnemies, final String name, final Minion minion) {
        this.mustBeCastOnEnemies = mustBeCastOnEnemies;
        this.name = name;
        this.minion = minion;
    }

    /**
     *
     * @return
     */
    public boolean castOnEnemies() {
        return mustBeCastOnEnemies;
    }

    /**
     *
     * @param mustBeCastOnEnemies
     */
    public void setMustBeCastOnEnemies(final boolean mustBeCastOnEnemies) {
        this.mustBeCastOnEnemies = mustBeCastOnEnemies;
    }
}
