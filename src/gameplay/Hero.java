package gameplay;

import java.util.ArrayList;

public class Hero extends Card {
    private String ability;

    public Hero(int mana, int health, int attackDamage, String description, String name, ArrayList<String> colors, boolean frozen) {
        super(mana, health, attackDamage, description, name, colors, frozen);
    }
}
