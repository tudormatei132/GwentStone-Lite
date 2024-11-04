package gameplay;

import java.util.ArrayList;

public class Minion extends Card {

    private boolean isTank;

    public Minion(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors, boolean isTank) {
        super(mana, health, attackDamage, description, name, colors);
        this.isTank = isTank;
    }

    public boolean mustBeAttacked() {
        return false;
    }

    public boolean isTank() {
        return isTank;
    }

    public void setTank(boolean tank) {
        isTank = tank;
    }

    public int isAbleToAttack() {
        if (isFrozen())
            return -1;
        if (getHasAttacked())
            return 1;
        return 0; // can attack
    }

    public void attack(Card c) {
        c.reduceHealth(this.getAttackDamage());
        setHasAttacked(true);
    }


    public void resetStatus() {
        setFrozen(false);
        setHasAttacked(false);
    }
}
