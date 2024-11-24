package org.poo.abilities;

import lombok.Getter;
import lombok.Setter;
import org.poo.cards.Minion;

public abstract class Ability {
    @Getter @Setter
    private boolean mustBeCastOnEnemies;
    @Getter
    @Setter
    private String name;

    /**
     * will be used to cast different abilities
     * @param target the target minion
     */
    public abstract void useAbility(Minion target);

    public Ability(final boolean mustBeCastOnEnemies, final String name) {
        this.mustBeCastOnEnemies = mustBeCastOnEnemies;
        this.name = name;
    }

    /**
     *
     * @return returns true if the ability must be cast on an enemy
     */
    public boolean castOnEnemies() {
        return mustBeCastOnEnemies;
    }


}
