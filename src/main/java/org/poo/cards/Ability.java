package org.poo.cards;

import lombok.Getter;
import lombok.Setter;

public abstract class Ability {
    @Getter @Setter
    private boolean mustBeCastOnEnemies;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Minion minion;

    /**
     * will be used to cast different abilities
     * @param target the target minion
     */
    public abstract void useAbility(Minion target);

    public Ability(final boolean mustBeCastOnEnemies, final String name, final Minion minion) {
        this.mustBeCastOnEnemies = mustBeCastOnEnemies;
        this.name = name;
        this.minion = minion;
    }

    /**
     *
     * @return returns true if the ability must be cast on an enemy
     */
    public boolean castOnEnemies() {
        return mustBeCastOnEnemies;
    }


}
