package gameplay;

import java.util.ArrayList;

public class Tank extends Minion{
    public Tank(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors) {
        super(mana, health, attackDamage, description, name, colors);
        this.setFront(true);
    }

    public boolean mustBeAttacked() {
        return true;
    }

}
