package org.poo.cards;

public final class Shapeshift extends Ability {


    public Shapeshift(final Minion m) {
        super(true, "Shapeshift", m);
    }

    /**
     * performs a swap between the target's health and attack damage
     * @param target the target minion
     */
    public void useAbility(final Minion target) {
        int temp = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(temp);
    }
}
