package org.poo.cards;

public final class WeakKnees extends Ability {

    public WeakKnees(final Minion m) {
        super(true, "Weak Knees", m);
    }

    /**
     *
     * @param target
     */
    public void useAbility(final Minion target) {
        target.setAttackDamage(Math.max(target.getAttackDamage() - 2, 0));
    }
}
