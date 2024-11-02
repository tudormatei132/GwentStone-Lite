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
}
