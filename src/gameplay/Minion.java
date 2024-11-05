package gameplay;

import java.awt.*;
import java.util.ArrayList;

public class Minion extends Card {

    private boolean isTank;



    private Ability ability = null;
    public Minion(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors, boolean isTank) {
        super(mana, health, attackDamage, description, name, colors);
        this.isTank = isTank;
    }

    public boolean mustBeAttacked() {
        return false;
    }

    public Minion copyCard() {
        Minion copy = new Minion(getMana(), getHealth(), getAttackDamage(), getDescription(), getName(), getColors(), isTank);
        copy.setFront(this.isFront());
        copy.setAbility();
        return copy;
    }

    public void setAbility() {
        if (getName().equals("The Ripper")) {
            ability = new WeakKnees(this);
            return;
        }

        if (getName().equals("Miraj")) {
            ability = new Skyjack(this);
            return;
        }

        if (getName().equals("The Cursed One")) {
            ability = new Shapeshift(this);
            return;
        }

        if (getName().equals("Disciple")) {
            ability = new GodsPlan(this);
        }
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

    public Ability getAbility() {
        return ability;
    }




    public void attack(Card c) {
        c.reduceHealth(this.getAttackDamage());
        setHasAttacked(true);
    }


    public void resetStatus() {
        setFrozen(false);
        setHasAttacked(false);
    }

    public void useAbility(Minion m) {
        ability.useAbility(m);
        setHasAttacked(true);
    }

}
