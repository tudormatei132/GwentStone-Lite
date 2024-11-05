package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class Hero extends Card {
    private HeroAbility ability;
    public Hero(int mana, int attackDamage, String description, String name, ArrayList<String> colors) {
        super(mana, 30, attackDamage, description, name, colors);
    }
    public Hero(Card c) {
        super(c.getMana(), 30, c.getAttackDamage(), c.getDescription(), c.getName(), c.getColors());
    }

    public void setHeroAbility() {
        if (getName().equals("Lord Royce")) {
            ability = new SubZero();
            return;
        }

        if (getName().equals("Empress Thorina")) {
            ability = new LowBlow();
            return;
        }

        if (getName().equals("King Mudface")) {
            ability = new EarthBorn();
            return;
        }

        if (getName().equals("General Kocioraw")) {
            ability = new BloodThirst();
        }
    }

    public HeroAbility getAbility() {
        return ability;
    }



}
