package org.poo.cards;

public final class Skyjack extends Ability {


    public Skyjack(final Minion m) {
        super(true, "Skyjack", m);
    }

    /**
     *
     * @param target
     */
    public void useAbility(final Minion target) {
        int temp = this.getMinion().getHealth();
        this.getMinion().setHealth(target.getHealth());
        target.setHealth(temp);
    }
}
