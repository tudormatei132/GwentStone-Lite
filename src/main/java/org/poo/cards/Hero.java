package org.poo.cards;


import java.util.ArrayList;
import org.poo.gameplay.Constants;
import lombok.Getter;


public final class Hero extends Card {
    @Getter
    private HeroAbility ability;

    public Hero(final int mana, final int attackDamage, final String description,
                final String name, final ArrayList<String> colors) {
        super(mana, Constants.HERO_HEALTH, attackDamage, description, name, colors);
    }


    public Hero(final Card c) {
        super(c.getMana(), Constants.HERO_HEALTH, c.getAttackDamage(),
                c.getDescription(), c.getName(), c.getColors());
    }

    /**
     * gives the corresponding ability to the heroes
     */
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

}
