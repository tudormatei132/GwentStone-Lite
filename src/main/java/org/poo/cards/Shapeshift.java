package org.poo.cards;

public final class Shapeshift extends Ability {


    public Shapeshift(final Minion m) {
        super(true, "Shapeshift", m);
    }

    /**
     *
     * @param target
     */
    public void useAbility(final Minion target) {
        int temp = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(temp);
    }
}
