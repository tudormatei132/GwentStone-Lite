package org.poo.cards;

public final class WeakKnees extends Ability {

    public WeakKnees(final Minion m) {
        super(true, "Weak Knees", m);
    }

    /**
     * lowers the attack damage of a target minion by 2
     * @param target the targeted minion
     */
    public void useAbility(final Minion target) {
        target.setAttackDamage(Math.max(target.getAttackDamage() - 2, 0));
    }
}
