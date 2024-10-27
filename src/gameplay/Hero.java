package gameplay;

import java.util.ArrayList;

public class Hero extends Card {
    private String ability;
    public Hero(int mana, int attackDamage, String description, String name, ArrayList<String> colors) {
        super(mana, 30, attackDamage, description, name, colors);
    }
}
